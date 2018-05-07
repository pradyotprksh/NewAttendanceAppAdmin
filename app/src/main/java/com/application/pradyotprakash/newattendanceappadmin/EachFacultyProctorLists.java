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

public class EachFacultyProctorLists extends AppCompatActivity {

    private String facultyId;
    private RecyclerView mSubjectListView;
    private List<FacultyProctors> subjectsList;
    private FacultyProctorRecyclerAdapter subjectRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_faculty_proctor_lists);
        facultyId = getIntent().getStringExtra("facultyId");
        Toolbar mToolbar = findViewById(R.id.faculty_subject_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Proctor Students List");
        mFirestore = FirebaseFirestore.getInstance();
        mSubjectListView = findViewById(R.id.facultySubjectLists);
        subjectsList = new ArrayList<>();
        subjectsList.clear();
        subjectRecyclerAdapter = new FacultyProctorRecyclerAdapter(subjectsList, getApplicationContext());
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore.collection("Faculty").document(facultyId).collection("Proctor").addSnapshotListener(EachFacultyProctorLists.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        FacultyProctors subjects = doc.getDocument().toObject(FacultyProctors.class);
                        subjectsList.add(subjects);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mSubjectListView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(EachFacultyProctorLists.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        mSubjectListView.addItemDecoration(horizontalDecoration);
    }
}
