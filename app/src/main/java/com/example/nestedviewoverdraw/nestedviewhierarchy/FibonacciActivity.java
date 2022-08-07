package com.example.nestedviewoverdraw.nestedviewhierarchy;

import android.app.Activity;
import android.os.AsyncTask;
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
                new FibonacciAsyncTask().execute(40);
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
     *  Optimized Fibonacci Code
     */


    private class FibonacciAsyncTask extends AsyncTask<Integer,Void,Integer>{
        int[] fibonacciList ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return  computeFibonacci(integers[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.i(LOG_TAG, String.valueOf(integer));

        }

        public int computeFibonacci(int positionInFibSequence) {
            if(fibonacciList == null){
                fibonacciList = new int[positionInFibSequence+1];
            }
            if(positionInFibSequence==0){
                return 0;
            }
            else if(positionInFibSequence ==1){
                return 1;
            }
            else if(fibonacciList[positionInFibSequence]==0){
                fibonacciList[positionInFibSequence]= computeFibonacci(positionInFibSequence-1)+computeFibonacci(positionInFibSequence-2);
            }
            return fibonacciList[positionInFibSequence];
        }
    }
}
