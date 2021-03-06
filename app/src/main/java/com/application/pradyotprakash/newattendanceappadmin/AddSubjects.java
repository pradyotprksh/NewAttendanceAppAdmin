package com.application.pradyotprakash.newattendanceappadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSubjects extends AppCompatActivity {

    private String branch, semester;
    private EditText subjectName, subjectCode;
    private Button addSubejct;
    private FirebaseFirestore mFirestore;
    private RecyclerView mSubjectList;
    private List<SubjectList> subjectList;
    private SubjectRecyclerAdapter subjectRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subjects);
        Toolbar mToolbar = findViewById(R.id.adminAddSubjectToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Subjects");
        branch = getIntent().getStringExtra("branch");
        semester = getIntent().getStringExtra("semester");
        subjectName = findViewById(R.id.subjectName);
        subjectCode = findViewById(R.id.subjectId);
        addSubejct = findViewById(R.id.addSubject);
        mFirestore = FirebaseFirestore.getInstance();
        addSubejct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String subjectValue = subjectName.getText().toString().toUpperCase();
                final String subjectIdValue = subjectCode.getText().toString().toUpperCase();
                if (!TextUtils.isEmpty(subjectValue) && !TextUtils.isEmpty(subjectIdValue)) {
                    LayoutInflater li = LayoutInflater.from(AddSubjects.this);
                    View promptsView = li.inflate(R.layout.prompts2, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AddSubjects.this);
                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            HashMap<String, Object> addSubjectMap = new HashMap<>();
                                            addSubjectMap.put("subjectName", subjectValue);
                                            addSubjectMap.put("subjectCode", subjectIdValue);
                                            addSubjectMap.put("branch", branch);
                                            addSubjectMap.put("semester", semester);
                                            mFirestore.collection("Subject").document(branch).collection(semester).document(subjectIdValue).set(addSubjectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(AddSubjects.this, "Subject Added Successfully", Toast.LENGTH_SHORT).show();
                                                    subjectName.setText("");
                                                    subjectCode.setText("");
                                                }
                                            });
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(AddSubjects.this, "Enter All the Details First.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSubjectList = findViewById(R.id.mSubjectList);
        subjectList = new ArrayList<>();
        subjectList.clear();
        subjectRecyclerAdapter = new SubjectRecyclerAdapter(subjectList, getApplicationContext());
        mSubjectList.setHasFixedSize(true);
        mSubjectList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSubjectList.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Subject").document(branch).collection(semester).orderBy("subjectCode").addSnapshotListener(AddSubjects.this, new EventListener<QuerySnapshot>() {
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
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mSubjectList.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(AddSubjects.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
    }
}
