package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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

public class SelectSemesterClassSubjects extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;
    private AutoCompleteTextView semesterOption;
    private ImageView semesterSpinner;
    private Button addSubjects, getSubjects;
    String branch, semesterValue;
    private static final String[] semester = new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"};
    private RecyclerView mSubjectList;
    private List<SubjectList> subjectList;
    private SubjectRecyclerAdapter subjectRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_semester_class_subjects);
        mToolbar = findViewById(R.id.admin_selectsemester_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Semester");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        branch = getIntent().getStringExtra("branch");
        semesterOption = findViewById(R.id.admin_selectclass);
        semesterSpinner = findViewById(R.id.semester_spinner);
        addSubjects = findViewById(R.id.get_class);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(SelectSemesterClassSubjects.this, android.R.layout.simple_dropdown_item_1line, semester);
        semesterOption.setAdapter(adapterSemester);
        semesterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterOption.showDropDown();
            }
        });
        addSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(semesterOption.getText().toString())) {
                    Intent timetableIndent = new Intent(SelectSemesterClassSubjects.this, AddSubjects.class);
                    timetableIndent.putExtra("semester", semesterOption.getText().toString());
                    timetableIndent.putExtra("branch", branch);
                    startActivity(timetableIndent);
                } else {
                    Toast.makeText(SelectSemesterClassSubjects.this, "Select Semester First.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getSubjects = findViewById(R.id.get_class_now);
        mSubjectList = findViewById(R.id.mSubjectList);
        subjectList = new ArrayList<>();
        subjectList.clear();
        subjectRecyclerAdapter = new SubjectRecyclerAdapter(subjectList, getApplicationContext());
        mSubjectList.setHasFixedSize(true);
        mSubjectList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSubjectList.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        getSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(semesterOption.getText().toString())) {
                    subjectList.clear();
                    mFirestore.collection("Subject").document(branch).collection(semesterOption.getText().toString()).orderBy("subjectCode").addSnapshotListener(SelectSemesterClassSubjects.this, new EventListener<QuerySnapshot>() {
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
                } else {
                    Toast.makeText(SelectSemesterClassSubjects.this, "Select Semester First.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mSubjectList.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(SelectSemesterClassSubjects.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
    }

}
