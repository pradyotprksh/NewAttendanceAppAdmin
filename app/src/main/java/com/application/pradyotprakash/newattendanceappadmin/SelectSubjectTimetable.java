package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectSubjectTimetable extends AppCompatActivity {

    private static String classValue, branch, semesterValue, classTeacher;
    private android.support.v7.widget.Toolbar mToolbar;
    private static final String[] semester = new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"};
    private RecyclerView mSubjectList;
    private List<SubjectList> subjectList;
    private TimetableSubjectRecyclerAdapter subjectRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject_timetable);
        classValue = getIntent().getStringExtra("classValue");
        branch = getIntent().getStringExtra("branch");
        semesterValue = getIntent().getStringExtra("semester");
        classTeacher = getIntent().getStringExtra("classTeacher");
        mToolbar = findViewById(R.id.admin_selectsemester_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Subject");
        mSubjectList = findViewById(R.id.mSubjectList);
        subjectList = new ArrayList<>();
        subjectList.clear();
        subjectRecyclerAdapter = new TimetableSubjectRecyclerAdapter(subjectList, getApplicationContext());
        mSubjectList.setHasFixedSize(true);
        mSubjectList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSubjectList.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Subject").document(branch).collection(semesterValue).orderBy("subjectCode").addSnapshotListener(SelectSubjectTimetable.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String subject_id = doc.getDocument().getId();
                        SubjectList classValueList = doc.getDocument().toObject(SubjectList.class).withId(subject_id);
                        subjectList.add(classValueList);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public static String getClassValue() {
        return classValue;
    }

    public static String getBranch() {
        return branch;
    }

    public static String getSemesterValue() {
        return semesterValue;
    }
}
