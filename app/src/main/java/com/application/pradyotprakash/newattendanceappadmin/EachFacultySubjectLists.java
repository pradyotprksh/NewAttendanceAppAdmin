package com.application.pradyotprakash.newattendanceappadmin;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EachFacultySubjectLists extends AppCompatActivity {

    private String facultyId;
    private RecyclerView mSubjectListView;
    private List<FacultySubjects> subjectsList;
    private FacultySubjectRecyclerAdapter subjectRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_faculty_subject_lists);
        facultyId = getIntent().getStringExtra("facultyId");
        Toolbar mToolbar = findViewById(R.id.faculty_subject_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Subject List");
        mFirestore = FirebaseFirestore.getInstance();
        mSubjectListView = findViewById(R.id.facultySubjectLists);
        subjectsList = new ArrayList<>();
        subjectsList.clear();
        subjectRecyclerAdapter = new FacultySubjectRecyclerAdapter(subjectsList, getApplicationContext());
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore.collection("Faculty").document(facultyId).collection("Subjects").orderBy("semester").addSnapshotListener(EachFacultySubjectLists.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        FacultySubjects subjects = doc.getDocument().toObject(FacultySubjects.class);
                        subjectsList.add(subjects);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mSubjectListView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(EachFacultySubjectLists.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        mSubjectListView.addItemDecoration(horizontalDecoration);
    }
}
