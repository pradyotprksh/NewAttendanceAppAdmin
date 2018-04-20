package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class EachFacultyDetails extends AppCompatActivity {

    private String facultyId;
    private TextView facultyName, facultyIdValue, facultyBranch, facultyClassTeacher;
    private CircleImageView facultyImage;
    private FirebaseFirestore mFirestore;
    private Uri studentImageURI = null;
    private Button facultySubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_faculty_details);
        facultyId = getIntent().getStringExtra("facultyId");
        facultyName = findViewById(R.id.faculty_name);
        facultyIdValue = findViewById(R.id.faculty_id);
        facultyBranch = findViewById(R.id.faculty_branch);
        facultyImage = findViewById(R.id.faculty_image);
        facultyClassTeacher = findViewById(R.id.faculty_class_teacher);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(facultyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        facultyName.setText(task.getResult().getString("name"));
                        facultyIdValue.setText(task.getResult().getString("id"));
                        facultyBranch.setText(task.getResult().getString("branch"));
                        String image = task.getResult().getString("image");
                        studentImageURI = Uri.parse(image);
                        RequestOptions placeHolderRequest = new RequestOptions();
                        placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                        try {
                            Glide.with(EachFacultyDetails.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(facultyImage);
                        } catch (Exception e) {
                            Toast.makeText(EachFacultyDetails.this, "...", Toast.LENGTH_SHORT).show();
                        }
                        String isAClassTeacher = task.getResult().getString("classTeacherValue");
                        if (isAClassTeacher.equals("true")) {
                            facultyClassTeacher.setText("Class Teacher of : " + task.getResult().getString("classTeacherOf"));
                        } else {
                            facultyClassTeacher.setText("Not A Class Teacher");
                        }
                    }
                }
            }
        });
        facultySubjects = findViewById(R.id.faculty_subjects);
        facultySubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EachFacultyDetails.this, EachFacultySubjectLists.class);
                intent.putExtra("facultyId", facultyId);
                startActivity(intent);
            }
        });
    }
}
