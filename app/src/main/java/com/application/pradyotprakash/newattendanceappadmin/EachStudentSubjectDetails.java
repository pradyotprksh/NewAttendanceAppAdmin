package com.application.pradyotprakash.newattendanceappadmin;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EachStudentSubjectDetails extends AppCompatActivity {

    private static String studentId;
    private String name;
    private String branch;
    private String semester;
    private String classValue;
    private FirebaseFirestore mFirestore, mFirestore1;
    private RecyclerView mSubjectListView;
    private List<StudentSubjects> subjectList;
    private StudentSubjectRecyclerAdapter subjectRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_student_subject_details);
        studentId = getIntent().getStringExtra("student_id");
        name = getIntent().getStringExtra("name");
        Toolbar mToolbar = findViewById(R.id.adminSetupToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(name + " Subject Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mSubjectListView = findViewById(R.id.subjectList);
        subjectList = new ArrayList<>();
        subjectRecyclerAdapter = new StudentSubjectRecyclerAdapter(subjectList, EachStudentSubjectDetails.this);
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(EachStudentSubjectDetails.this));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore.collection("Student").document(studentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                branch = task.getResult().getString("branch");
                semester = task.getResult().getString("semester");
                classValue = task.getResult().getString("className");
                mFirestore1.collection("Subject").document(branch).collection(semester).addSnapshotListener(EachStudentSubjectDetails.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        subjectRecyclerAdapter.notifyDataSetChanged();
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                StudentSubjects subjects = doc.getDocument().toObject(StudentSubjects.class);
                                subjectList.add(subjects);
                                subjectRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        });
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mSubjectListView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(EachStudentSubjectDetails.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        mSubjectListView.addItemDecoration(horizontalDecoration);
    }

    public static String getStudentId() {
        return studentId;
    }
}
