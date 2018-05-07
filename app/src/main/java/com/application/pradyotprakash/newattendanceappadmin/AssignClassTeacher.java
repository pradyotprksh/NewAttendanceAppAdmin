package com.application.pradyotprakash.newattendanceappadmin;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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

public class AssignClassTeacher extends AppCompatActivity {

    private static String subjectId, classValueString, branchValue, semesterValue;
    private RecyclerView facultyList;
    private List<ClassTeacher> facultyClassList;
    private ClassTeacherRecyclerAdapter classTeacherRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_class_teacher);
        subjectId = getIntent().getStringExtra("subjectId");
        Toolbar mToolbar = findViewById(R.id.adminClassTeacherToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Class Teacher of " + subjectId);
        classValueString = getIntent().getStringExtra("classValueString");
        branchValue = getIntent().getStringExtra("branchValue");
        semesterValue = getIntent().getStringExtra("semesterValue");
        mFirestore = FirebaseFirestore.getInstance();
        facultyList = findViewById(R.id.facultyList);
        facultyClassList = new ArrayList<>();
        facultyClassList.clear();
        classTeacherRecyclerAdapter = new ClassTeacherRecyclerAdapter(facultyClassList, getApplicationContext());
        facultyList.setHasFixedSize(true);
        facultyList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        facultyList.setAdapter(classTeacherRecyclerAdapter);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(facultyList.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(AssignClassTeacher.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
    }

    @Override
    protected void onStart() {
        super.onStart();
        facultyClassList.clear();
        mFirestore.collection("Faculty").orderBy("name").addSnapshotListener(AssignClassTeacher.this, new EventListener<QuerySnapshot>() {
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

    public static String getClassValueString() {
        return classValueString;
    }

    public static String getBranchValue() {
        return branchValue;
    }

    public static String getSemesterValue() {
        return semesterValue;
    }
}
