package com.application.pradyotprakash.newattendanceappadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminStudentNotification extends AppCompatActivity {

    private EditText message;
    private Button send;
    private FirebaseAuth mAuth;
    private String user_id, branch;
    private FirebaseFirestore mFirestore, adminMainFirestore;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_notification);
        Toolbar mToolbar = findViewById(R.id.adminStudentNotifyToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Your Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mProgressbar = findViewById(R.id.sendingNotifBar);
        mProgressbar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        adminMainFirestore = FirebaseFirestore.getInstance();
        adminMainFirestore.collection("Admin").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        branch = task.getResult().getString("branch");
                    }
                }
            }
        });
        message = findViewById(R.id.studentMessage);
        send = findViewById(R.id.sendToStudent);
        mFirestore = FirebaseFirestore.getInstance();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = message.getText().toString();
                if (!TextUtils.isEmpty(value)) {
                    mProgressbar.setVisibility(View.VISIBLE);
                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message", value);
                    notificationMessage.put("sender", user_id);
                    mFirestore.collection("Notification").document("Admin").collection(branch).add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AdminStudentNotification.this, "Notification Sent.", Toast.LENGTH_SHORT).show();
                            message.setText("");
                            mProgressbar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminStudentNotification.this, "Unable To Send Notification.", Toast.LENGTH_SHORT).show();
                            mProgressbar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(AdminStudentNotification.this, "Write A Message.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
