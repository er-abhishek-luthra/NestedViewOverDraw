package com.example.performancetest

import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.filters.MediumTest
import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * UiAutomator tests that triggers measure/layouts passes in the MainActivity app to compare the
 * UI performance for ConstraintLayout in comparison to traditional layouts.
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
@MediumTest
class MeasurementPerformanceTest {

    private lateinit var device: UiDevice

    @Before
    fun startNestedViewHierarchyTestActivityFromHomeScreen() {
        device = UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation())
        device.pressHome()

        val launcherPackage = launchPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT)

        val context = InstrumentationRegistry.getInstrumentation().context
        val intent = context.packageManager
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)    // Clear out any previous instances

        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT)
    }

    @Test
    @Throws(Throwable::class)
    fun testRunCalculationNestedTraditionalLayouts() {
        doCalculation("button_start_calc_traditional")
    }

    @Test
    @Throws(Throwable::class)
    fun testRunCalculationNonNestedConstraintLayout() {
        doCalculation("button_start_calc_constraint")
    }

    /**
     * Runs the calculation on a connected device or on an emulator. By clicking a button in the
     * app, the app runs measure/layout passes specific times.
     */
    private fun doCalculation(buttonIdToStart: String) {
        device.findObject(By.res(BASIC_SAMPLE_PACKAGE, buttonIdToStart)).click()
        device.wait<UiObject2>(
            Until.findObject(By.res(
                BASIC_SAMPLE_PACKAGE,
                "textview_finish")), TimeUnit.SECONDS.toMillis(15))
    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private val launchPackageName: String
        get() {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)

            val pm = InstrumentationRegistry.getInstrumentation().getContext().packageManager
            val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            return resolveInfo?.activityInfo?.packageName?:""
        }

    companion object {
        private val BASIC_SAMPLE_PACKAGE = "com.example.nestedviewoverdraw"
        private val LAUNCH_TIMEOUT = 5000L
    }
}
