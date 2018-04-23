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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminAddClasses extends AppCompatActivity {

    private AutoCompleteTextView semesterOption;
    private ImageView semesterSpinner;
    private EditText classValue;
    private Button addClass, getClass;
    private String semesterValue;
    private static String branch;
    private FirebaseFirestore adminAddClassFirestore, mFirestore;
    private static final String[] semester = new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"};
    private RecyclerView mClassList;
    private List<AddClassList> classList;
    private AddClassRecyclerAdapter classRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_classes);
        Toolbar mToolbar = findViewById(R.id.adminAddClassToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Enter Class");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        branch = getIntent().getStringExtra("branch");
        semesterOption = findViewById(R.id.admin_addclass_semester);
        semesterSpinner = findViewById(R.id.semester_spinner);
        classValue = findViewById(R.id.admin_addclass_class);
        addClass = findViewById(R.id.admin_addclass_btn);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(AdminAddClasses.this, android.R.layout.simple_dropdown_item_1line, semester);
        semesterOption.setAdapter(adapterSemester);
        semesterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterOption.showDropDown();
            }
        });
        adminAddClassFirestore = FirebaseFirestore.getInstance();
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterValue = semesterOption.getText().toString();
                String classValues = classValue.getText().toString().toUpperCase();
                if (!TextUtils.isEmpty(semesterValue) && !TextUtils.isEmpty(classValues)) {
                    HashMap<String, String> adminMap = new HashMap<>();
                    adminMap.put("classValue", classValues);
                    adminMap.put("classTeacher", "Assign Teacher");
                    adminMap.put("semester", semesterValue);
                    adminMap.put("branch", branch);
                    adminAddClassFirestore.collection("Class").document(branch).collection(semesterValue).document(classValues).set(adminMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdminAddClasses.this, "All the Data Has Been Uploaded.", Toast.LENGTH_LONG).show();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(AdminAddClasses.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AdminAddClasses.this, "Fill all the details.", Toast.LENGTH_LONG).show();
                }
            }
        });
        getClass = findViewById(R.id.admin_getclass_btn2);
        mClassList = findViewById(R.id.classList);
        classList = new ArrayList<>();
        classRecyclerAdapter = new AddClassRecyclerAdapter(classList, getApplicationContext());
        mClassList.setHasFixedSize(true);
        mClassList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mClassList.setAdapter(classRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        getClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterValue = semesterOption.getText().toString();
                if (!TextUtils.isEmpty(semesterValue)) {
                    classList.clear();
                    mFirestore.collection("Class").document(branch).collection(semesterValue).orderBy("classValue").addSnapshotListener(AdminAddClasses.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String subject_id = doc.getDocument().getId();
                                    AddClassList classValueList = doc.getDocument().toObject(AddClassList.class).withId(subject_id);
                                    classList.add(classValueList);
                                    classRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(AdminAddClasses.this, "Select Semester To Get Its Classes.", Toast.LENGTH_LONG).show();
                }
            }
        });
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mClassList.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(AdminAddClasses.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
    }
}
