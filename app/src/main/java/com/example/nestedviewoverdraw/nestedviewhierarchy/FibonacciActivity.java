package com.example.nestedviewoverdraw.nestedviewhierarchy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.nestedviewoverdraw.R;


public class FibonacciActivity extends Activity {
    public static final String LOG_TAG = "CachingActivityExercise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);

        Button theButtonThatDoesFibonacciStuff = (Button) findViewById(R.id.caching_do_fib_stuff);
        theButtonThatDoesFibonacciStuff.setText("Compute some Fibonacci numbers.");

        theButtonThatDoesFibonacciStuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Compute the 40th number in the fibonacci sequence, then dump to log output. Note
                // how the UI hangs each time you do this.
                Log.i(LOG_TAG, String.valueOf(computeFibonacci(40)));
            }
        });

        // It's much easier to see how your decisions affect framerate when there's something
        // changing on screen.  For entirely serious, educational purposes, a dancing pirate
        // will be included with this exercise.
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("file:///android_asset/dancing_cartoon.gif");
    }

    /**
     *  Why store things when you can recurse instead?  Don't let evidence, personal experience,
     *  or rational arguments from your peers fool you.  The elegant solution is the best solution.
     *
     * @param positionInFibSequence  The position in the fibonacci sequence to return.
     * @return the nth number of the fibonacci sequence.  Seriously, try to keep up.
     */
    public int computeFibonacci(int positionInFibSequence) {
        if (positionInFibSequence <= 2) {
            return 1;
        } else {
            return computeFibonacci(positionInFibSequence - 1)
                    + computeFibonacci(positionInFibSequence - 2);
        }
    }
}
