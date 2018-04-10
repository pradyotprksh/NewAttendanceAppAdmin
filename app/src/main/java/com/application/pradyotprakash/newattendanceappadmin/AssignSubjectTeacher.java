package com.application.pradyotprakash.newattendanceappadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AssignSubjectTeacher extends AppCompatActivity {

    private static String subjectId, branchValue, semesterValue, subjectName;
    private RecyclerView facultyList;
    private List<ClassTeacher> facultyClassList;
    private SubjectTeacherRecyclerAdapter classTeacherRecyclerAdapter;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_subject_teacher);
        subjectId = getIntent().getStringExtra("subjectId");
        Toolbar mToolbar = findViewById(R.id.adminClassTeacherToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Subject Teacher for " + subjectId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        branchValue = getIntent().getStringExtra("branch");
        semesterValue = getIntent().getStringExtra("semester");
        subjectName = getIntent().getStringExtra("subjectName");
        mFirestore = FirebaseFirestore.getInstance();
        facultyList = findViewById(R.id.facultyList);
        facultyClassList = new ArrayList<>();
        facultyClassList.clear();
        classTeacherRecyclerAdapter = new SubjectTeacherRecyclerAdapter(facultyClassList, getApplicationContext());
        facultyList.setHasFixedSize(true);
        facultyList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        facultyList.setAdapter(classTeacherRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        facultyClassList.clear();
        mFirestore.collection("Faculty").orderBy("name").addSnapshotListener(AssignSubjectTeacher.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String faculty_id = doc.getDocument().getId();
                        ClassTeacher teacher = doc.getDocument().toObject(ClassTeacher.class).withId(faculty_id);
                        facultyClassList.add(teacher);
                        classTeacherRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public static String getSubjectId() {
        return subjectId;
    }

    public static String getBranchValue() {
        return branchValue;
    }

    public static String getSemesterValue() {
        return semesterValue;
    }

    public static String getSubjectName() {
        return subjectName;
    }
}
