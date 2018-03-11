package com.application.pradyotprakash.newattendanceappadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private Button addClass;
    String branch;
    private FirebaseFirestore adminAddClassFirestore;
    private static final String[] semester = new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6"};

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
                String semester = semesterOption.getText().toString();
                String classValues = classValue.getText().toString().toUpperCase();
                if (!TextUtils.isEmpty(semester) && !TextUtils.isEmpty(classValues)) {
                    HashMap<String, String> adminMap = new HashMap<>();
                    adminMap.put("classValue", classValues);
                    adminAddClassFirestore.collection("Class").document(branch).collection(semester).document().set(adminMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }
}
