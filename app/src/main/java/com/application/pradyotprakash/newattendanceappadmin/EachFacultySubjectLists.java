package com.application.pradyotprakash.newattendanceappadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class EachFacultySubjectLists extends AppCompatActivity {

    private String facultyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_faculty_subject_lists);
        facultyId = getIntent().getStringExtra("facultyId");
        Toolbar mToolbar = findViewById(R.id.faculty_subject_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Subject List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
