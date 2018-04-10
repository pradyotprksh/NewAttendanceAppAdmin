package com.application.pradyotprakash.newattendanceappadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class AddTimetable extends AppCompatActivity {

    private String classValue, subjectName, subjectCode, subjectTeacher;
    private android.support.v7.widget.Toolbar mToolbar;
    private AutoCompleteTextView weekOption;
    private ImageView daySpinner;
    private TextView subjectValue, subjectIdValue;
    private static final String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);
        classValue = getIntent().getStringExtra("classValue");
        subjectName = getIntent().getStringExtra("subjectName");
        subjectCode = getIntent().getStringExtra("subjectCode");
        subjectTeacher = getIntent().getStringExtra("subjectTeacher");
        mToolbar = findViewById(R.id.adminSetupToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Semester");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        subjectValue = findViewById(R.id.subjectValue);
        subjectIdValue = findViewById(R.id.subjectIdValue);
        weekOption = findViewById(R.id.select_weekday);
        daySpinner = findViewById(R.id.day_spinner);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(AddTimetable.this, android.R.layout.simple_dropdown_item_1line, days);
        weekOption.setAdapter(adapterSemester);
        daySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekOption.showDropDown();
            }
        });

    }
}
