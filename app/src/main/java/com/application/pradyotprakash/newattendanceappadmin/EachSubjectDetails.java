package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.HashMap;

public class EachSubjectDetails extends AppCompatActivity {

    private String facultyName, subjectId, subjectName, branch, semester, facultyId;
    private TextView subjectValue, subjectTeacherValue, branchValueText, semesterValueText, subjectIdValue, removeAssignment;
    private FirebaseFirestore mFirestore, mFirestore1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_subject_details);
        facultyName = getIntent().getStringExtra("facultyName");
        subjectId = getIntent().getStringExtra("subjectId");
        subjectName = getIntent().getStringExtra("subjectName");
        branch = getIntent().getStringExtra("branch");
        semester = getIntent().getStringExtra("semester");
        facultyId = getIntent().getStringExtra("facultyId");
        Toolbar mToolbar = findViewById(R.id.adminEachClassTeacherToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Subject " + subjectName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        subjectValue = findViewById(R.id.subjectValue);
        subjectTeacherValue = findViewById(R.id.subjectTeacherValue);
        branchValueText = findViewById(R.id.branchValue);
        semesterValueText = findViewById(R.id.semesterValue);
        subjectIdValue = findViewById(R.id.subjectIdValue);
        branchValueText.setText(branch);
        subjectValue.setText(subjectName);
        subjectTeacherValue.setText(facultyName);
        semesterValueText.setText(semester);
        subjectIdValue.setText(subjectId);
        removeAssignment = findViewById(R.id.removeAssignment);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        removeAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> removeFromSubject = new HashMap<>();
                removeFromSubject.put("subjectTeacher", "Assign Subject Teacher");
                mFirestore.collection("Subject").document(branch).collection(semester).document(subjectId).update(removeFromSubject).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String, Object> removeFromFaculty = new HashMap<>();
                        removeFromFaculty.put("subjectName", FieldValue.delete());
                        removeFromFaculty.put("branch", FieldValue.delete());
                        removeFromFaculty.put("semester", FieldValue.delete());
                        removeFromFaculty.put("subjectCode", FieldValue.delete());
                        mFirestore1.collection("Faculty").document(facultyId).collection("Subjects").document(subjectId).update(removeFromFaculty).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EachSubjectDetails.this,"Subject Removed", Toast.LENGTH_SHORT).show();
                                Intent goBack = new Intent(EachSubjectDetails.this, AddSubjects.class);
                                goBack.putExtra("branch", branch);
                                goBack.putExtra("semester", semester);
                                startActivity(goBack);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
}
