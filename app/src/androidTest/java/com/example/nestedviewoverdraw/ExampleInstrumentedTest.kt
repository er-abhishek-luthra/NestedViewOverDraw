package com.example.nestedviewoverdraw

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.nestedviewoverdraw.nestedviewhierarchy.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {
    @get:Rule
    public val activityRule = ActivityScenarioRule(
        MainActivity::class.java
    )
    @Test
    fun scrollTest() {
        val startSignal = CountDownLatch(1)

        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.nestedviewoverdraw", appContext.packageName)
        activityRule.getScenario().onActivity { activity ->
            val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_view)
            onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(

                    recyclerView.getAdapter()!!.getItemCount() - 1
                )
            );
            startSignal.countDown()
        }
        startSignal.await()

    }
}