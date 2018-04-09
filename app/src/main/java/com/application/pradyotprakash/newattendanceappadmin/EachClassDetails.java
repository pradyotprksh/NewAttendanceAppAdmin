package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EachClassDetails extends AppCompatActivity {

    private String classId, branchValue, semesterValue, classTeacher, classTeacherId;
    private TextView classValue, classTeacherValue, branchValueText, semesterValueText, removeAssignment;
    private FirebaseFirestore mFirestore, mFirestore1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_class_details);
        classId = getIntent().getStringExtra("subjectId");
        branchValue = getIntent().getStringExtra("branchValue");
        semesterValue = getIntent().getStringExtra("semesterValue");
        classTeacher = getIntent().getStringExtra("classTeacher");
        classTeacherId = getIntent().getStringExtra("classTeacherId");
        Toolbar mToolbar = findViewById(R.id.adminEachClassTeacherToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Class " + classId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        classValue = findViewById(R.id.classValue);
        classTeacherValue = findViewById(R.id.classTeacherValue);
        branchValueText = findViewById(R.id.branchValue);
        semesterValueText = findViewById(R.id.semesterValue);
        branchValueText.setText(branchValue);
        classValue.setText(classId);
        classTeacherValue.setText(classTeacher);
        semesterValueText.setText(semesterValue);
        removeAssignment = findViewById(R.id.removeAssignment);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        removeAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> removeClassTeacher = new HashMap<>();
                removeClassTeacher.put("classTeacher", "Assign Teacher");
                mFirestore.collection("Class").document(branchValue).collection(semesterValue).document(classId).update(removeClassTeacher).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String, Object> classTeacherFaculty = new HashMap<>();
                        classTeacherFaculty.put("classTeacherOf", "No Data Selected");
                        classTeacherFaculty.put("classTeacherValue", "false");
                        mFirestore1.collection("Faculty").document(classTeacherId).update(classTeacherFaculty).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EachClassDetails.this, classId + " is removed from " + classTeacher, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EachClassDetails.this, AdminAddClasses.class);
                                intent.putExtra("branch", branchValue);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }
}
