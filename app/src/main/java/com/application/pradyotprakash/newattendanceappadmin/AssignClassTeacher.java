package com.application.pradyotprakash.newattendanceappadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AssignClassTeacher extends AppCompatActivity {

    private String subjectId, classValueString, branchValue, semesterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_class_teacher);
        subjectId = getIntent().getStringExtra("subjectId");
        classValueString = getIntent().getStringExtra("classValueString");
        branchValue = getIntent().getStringExtra("branchValue");
        semesterValue = getIntent().getStringExtra("semesterValue");
    }
}
