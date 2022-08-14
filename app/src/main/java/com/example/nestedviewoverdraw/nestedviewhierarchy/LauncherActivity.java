package com.example.nestedviewoverdraw.nestedviewhierarchy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nestedviewoverdraw.R;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_main);
        ViewGroup rootView = (ViewGroup) findViewById(R.id.main_rootview);

        addButton(MainActivity.class, "Recyclerview Test", rootView);
    }

    public void addButton(final Class destination, String description, ViewGroup parent) {
        Button button = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent problemIntent = new Intent(LauncherActivity.this, destination);
                startActivity(problemIntent);
            }
        });

        button.setText(description);
        parent.addView(button);
    }
}
