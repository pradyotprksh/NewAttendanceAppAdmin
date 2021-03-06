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

public class EachStudentInformation extends AppCompatActivity {

    private String student_id_value, name;
    private CircleImageView studentImage;
    private TextView studentName, studentUsn, studentBranch, studentSemester, studentClass, studentProctor, studentClassTeacher;
    private FirebaseFirestore studentInformationFirestore, mFirestore, mFirestore1, mFirestore2;
    private Uri studentImageURI = null;
    private Button sendMessage, classDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_student_information);
        student_id_value = getIntent().getStringExtra("studentId");
        studentImage = findViewById(R.id.faculty_image);
        studentName = findViewById(R.id.faculty_name);
        studentUsn = findViewById(R.id.student_usn);
        studentBranch = findViewById(R.id.faculty_branch);
        studentProctor = findViewById(R.id.faculty_proctor);
        studentClassTeacher = findViewById(R.id.student_classteacher);
        studentSemester = findViewById(R.id.student_semester);
        studentClass = findViewById(R.id.student_class);
        sendMessage = findViewById(R.id.send_message);
        studentInformationFirestore = FirebaseFirestore.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        studentInformationFirestore
                .collection("Student")
                .document(student_id_value)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        name = task.getResult().getString("name");
                        String usn = task.getResult().getString("usn");
                        String branch = task.getResult().getString("branch");
                        final String className = task.getResult().getString("className");
                        String semester = task.getResult().getString("semester");
                        String image = task.getResult().getString("image");
                        String proctor = task.getResult().getString("proctor");
                        try {
                            mFirestore
                                    .collection("Faculty")
                                    .document(proctor)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            String name = task.getResult().getString("name");
                                            studentProctor.setText("Proctor: " + name);
                                        }
                                    }
                                }
                            });
                        } catch (Exception e) {
                            studentProctor.setText("Proctor Not Assigned Yet");
                        }
                        mFirestore1
                                .collection("Class")
                                .document(branch)
                                .collection(semester)
                                .document(className)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        String classTeacher = task.getResult().getString("classTeacher");
                                        mFirestore2
                                                .collection("Faculty")
                                                .document(classTeacher)
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                String classTeacher = task.getResult().getString("name");
                                                studentClassTeacher.setText("Class Teacher: " + classTeacher);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                        studentImageURI = Uri.parse(image);
                        studentName.setText(name);
                        studentUsn.setText(usn);
                        studentBranch.setText("Branch: " + branch);
                        studentSemester.setText("Semester: " + semester);
                        studentClass.setText("Class: " + className);
                        RequestOptions placeHolderRequest = new RequestOptions();
                        placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                        try {
                            Glide.with(EachStudentInformation.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(studentImage);
                        } catch (Exception e) {
                            Toast.makeText(EachStudentInformation.this, "...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EachStudentInformation.this, "Ask The Student To Fill In The Details.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String retrieving_error = task.getException().getMessage();
                    Toast.makeText(EachStudentInformation.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToNotification();
            }
        });
        classDetails = findViewById(R.id.student_class_details);
        classDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EachStudentInformation.this, EachStudentSubjectDetails.class);
                intent.putExtra("student_id", student_id_value);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    private void sendToNotification() {
        Intent intent = new Intent(EachStudentInformation.this, AdminEachStudentNotification.class);
        intent.putExtra("student_id", student_id_value);
        intent.putExtra("name", name);
        startActivity(intent);
    }

}
