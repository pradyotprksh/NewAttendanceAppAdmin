package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Intent;
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

public class AdminAllFacultyList extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mStudentListView;
    private List<Faculties> studentsList;
    private FacultyRecyclerAdapter studentRecyclerAdapter;
    private FirebaseFirestore mFirestore;
    public static String branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_faculty_list);
        mToolbar = findViewById(R.id.admin_allstudentlist_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Faculty Lists");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        branch = intent.getStringExtra("branch");
        mFirestore = FirebaseFirestore.getInstance();
        mStudentListView = findViewById(R.id.admin_student_lists);
        studentsList = new ArrayList<>();
        studentsList.clear();
        studentRecyclerAdapter = new FacultyRecyclerAdapter(getApplicationContext(), studentsList);
        mStudentListView.setHasFixedSize(true);
        mStudentListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStudentListView.setAdapter(studentRecyclerAdapter);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mStudentListView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(AdminAllFacultyList.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        mStudentListView.addItemDecoration(horizontalDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentsList.clear();
        mFirestore.collection("Faculty").orderBy("id").addSnapshotListener(AdminAllFacultyList.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String faculty_id = doc.getDocument().getId();
                        Faculties students = doc.getDocument().toObject(Faculties.class).withId(faculty_id);
                        studentsList.add(students);
                        studentRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public static String adminBranch() {
        return branch;
    }

}
