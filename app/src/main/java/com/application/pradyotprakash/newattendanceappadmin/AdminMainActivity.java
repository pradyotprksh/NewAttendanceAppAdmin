package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminMainActivity extends AppCompatActivity {

    private Toolbar admin_main_toolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore adminMainFirestore;
    private String user_id, branch;
    private TextView adminMainName;
    private CircleImageView adminMainImage;
    private Button addFaculty, listFaculty, notificationFaculty, listStudent, notificationStudent, addClass, addSubjects, addTimetable;
    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        admin_main_toolbar = findViewById(R.id.admin_main_toolbar);
        setSupportActionBar(admin_main_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        adminMainName = findViewById(R.id.admin_main_name);
        adminMainImage = findViewById(R.id.admin_main_image);
        addFaculty = findViewById(R.id.admin_add_faculty);
        listFaculty = findViewById(R.id.admin_faculty_list);
        notificationFaculty = findViewById(R.id.admin_send_notification_faculty);
        listStudent = findViewById(R.id.admin_student_list);
        notificationStudent = findViewById(R.id.admin_send_notification_student);
        addSubjects = findViewById(R.id.admin_add_subjects);
        addClass = findViewById(R.id.admin_add_classes);
        addTimetable = findViewById(R.id.admin_add_timetable);
        addFaculty.setEnabled(false);
        addClass.setEnabled(false);
        listFaculty.setEnabled(false);
        notificationFaculty.setEnabled(false);
        listStudent.setEnabled(false);
        notificationFaculty.setEnabled(false);
        addSubjects.setEnabled(false);
        addTimetable.setEnabled(false);
        adminMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSetup();
            }
        });
        addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminMainActivity.this, "Add Faculty Details", Toast.LENGTH_SHORT).show();
            }
        });
        listFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sendToAllFaculty();
            }
        });
        notificationFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminMainActivity.this, "Send Notification To Faculty", Toast.LENGTH_SHORT).show();
            }
        });
        listStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAllStudent();
            }
        });
        notificationStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToStudentNotification();
            }
        });
        addSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSubjects();
            }
        });
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAddClass();
            }
        });
        addTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToTimetable();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        adminMainFirestore = FirebaseFirestore.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        } else {
            user_id = mAuth.getCurrentUser().getUid();
            adminMainFirestore.collection("Admin").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            sendToSetup();
                        } else {
                            String name = task.getResult().getString("name");
                            branch = task.getResult().getString("branch");
                            adminMainName.setText("Welcome, " + name);
                            String image = task.getResult().getString("image");
                            RequestOptions placeHolderRequest = new RequestOptions();
                            placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                            Glide.with(AdminMainActivity.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(adminMainImage);
                            addFaculty.setEnabled(true);
                            addClass.setEnabled(true);
                            listFaculty.setEnabled(true);
                            notificationFaculty.setEnabled(true);
                            listStudent.setEnabled(true);
                            notificationFaculty.setEnabled(true);
                            addSubjects.setEnabled(true);
                            addTimetable.setEnabled(true);
                        }
                    } else {
                        String retrieving_error = task.getException().getMessage();
                        Toast.makeText(AdminMainActivity.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_btn:
                logOut();
                return true;
            case R.id.action_setting_btn:
                sendToSetup();
                return true;
            default:
                return false;
        }
    }

    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent intent = new Intent(AdminMainActivity.this, AdminLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToSetup() {
        Intent intentSetup = new Intent(AdminMainActivity.this, AdminSetupActivity.class);
        startActivity(intentSetup);
    }

    private void sendToAllStudent() {
        Intent intent = new Intent(AdminMainActivity.this, AdminAllStudentList.class);
        intent.putExtra("branch", branch);
        startActivity(intent);
    }

    private void sendToAllFaculty() {
        Intent intent = new Intent(AdminMainActivity.this, AdminAllFacultyList.class);
        intent.putExtra("branch", branch);
        startActivity(intent);
    }

    private void sendToSubjects() {
        Intent intent = new Intent(AdminMainActivity.this, SelectSemesterClassSubjects.class);
        intent.putExtra("branch", branch);
        startActivity(intent);
    }

    private void sendToAddClass() {
        Intent intent = new Intent(AdminMainActivity.this, AdminAddClasses.class);
        intent.putExtra("branch", branch);
        startActivity(intent);
    }

    private void sendToStudentNotification() {
        Intent intent = new Intent(AdminMainActivity.this, AdminStudentNotification.class);
        startActivity(intent);
    }

    private void sendToTimetable() {
        Intent intent = new Intent(AdminMainActivity.this, SelectSemesterClassTimetable.class);
        intent.putExtra("branch", branch);
        startActivity(intent);
    }

}
