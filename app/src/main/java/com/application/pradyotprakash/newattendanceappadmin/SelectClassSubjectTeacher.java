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

public class SelectClassSubjectTeacher extends AppCompatActivity {

    private static String subjectId, branchValue, semesterValue, subjectName;
    private RecyclerView mClassList;
    private List<ClassList> classList;
    private ClassRecyclerAdapter classRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class_subject_teacher);
        Toolbar mToolbar = findViewById(R.id.adminClassTeacherToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Class");
        subjectId = getIntent().getStringExtra("subjectId");
        branchValue = getIntent().getStringExtra("branch");
        semesterValue = getIntent().getStringExtra("semester");
        subjectName = getIntent().getStringExtra("subjectName");
        mClassList = findViewById(R.id.classList);
        classList = new ArrayList<>();
        classRecyclerAdapter = new ClassRecyclerAdapter(classList, getApplicationContext());
        mClassList.setHasFixedSize(true);
        mClassList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mClassList.setAdapter(classRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Class").document(branchValue).collection(semesterValue).orderBy("classValue").addSnapshotListener(SelectClassSubjectTeacher.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String subject_id = doc.getDocument().getId();
                        ClassList classValueList = doc.getDocument().toObject(ClassList.class).withId(subject_id);
                        classList.add(classValueList);
                        classRecyclerAdapter.notifyDataSetChanged();
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
