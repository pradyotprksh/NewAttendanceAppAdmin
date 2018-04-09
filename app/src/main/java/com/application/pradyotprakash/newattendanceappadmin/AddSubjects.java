package com.application.pradyotprakash.newattendanceappadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AddSubjects extends AppCompatActivity {

    private String branch, semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subjects);
        branch = getIntent().getStringExtra("branch");
        semester = getIntent().getStringExtra("semester");

    }
}
