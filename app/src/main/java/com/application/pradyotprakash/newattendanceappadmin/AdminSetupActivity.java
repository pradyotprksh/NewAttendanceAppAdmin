package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminSetupActivity extends AppCompatActivity {

    private CircleImageView adminSetupImage;
    private Uri adminMainImageURI = null;
    private EditText adminName, adminId;
    private AutoCompleteTextView adminBranch;
    private Button adminSetupBtn;
    private StorageReference madminStorageReference;
    private FirebaseAuth mAuth;
    private ProgressBar adminSetupProgress;
    private FirebaseFirestore adminSetupFirestore;
    private String user_id;
    private boolean isChanged = false;
    private ImageView branchSpinner;
    private static final String[] branch = new String[]{"Bio Technology Engineering", "Civil Engineering", "Computer Science Engineering", "Electrical & Electronics Engineering", "Electronics & Comm. Engineering", "Information Science & Engineering", "Mechanical Engineering"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setup);
        Toolbar mToolbar = findViewById(R.id.adminSetupToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Your Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adminSetupProgress = findViewById(R.id.admin_setup_progress);
        adminSetupImage = findViewById(R.id.admin_setup_image);
        adminId = findViewById(R.id.admin_setup_id);
        adminSetupBtn = findViewById(R.id.admin_setup_btn);
        adminName = findViewById(R.id.admin_setup_name);
        adminBranch = findViewById(R.id.select_weekday);
        adminBranch.setThreshold(1);
        branchSpinner = findViewById(R.id.branch_spinner);
        ArrayAdapter<String> adapterBranch = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, branch);
        adminBranch.setAdapter(adapterBranch);
        branchSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminBranch.showDropDown();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        madminStorageReference = FirebaseStorage.getInstance().getReference();
        adminSetupFirestore = FirebaseFirestore.getInstance();
        adminSetupProgress.setVisibility(View.VISIBLE);
        adminName.setEnabled(false);
        adminId.setEnabled(false);
        adminBranch.setEnabled(false);
        branchSpinner.setEnabled(false);
        adminSetupFirestore.collection("Admin").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");
                        String branch = task.getResult().getString("branch");
                        String id = task.getResult().getString("id");
                        String image = task.getResult().getString("image");
                        adminMainImageURI = Uri.parse(image);
                        adminName.setText(name);
                        adminBranch.setText(branch);
                        adminId.setText(id);
                        RequestOptions placeHolderRequest = new RequestOptions();
                        placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                        Glide.with(AdminSetupActivity.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(adminSetupImage);
                        adminName.setEnabled(false);
                        adminId.setEnabled(false);
                        adminBranch.setEnabled(false);
                        branchSpinner.setEnabled(false);
                    } else {
                        Toast.makeText(AdminSetupActivity.this, "Fill All The Data and Upload a Profile Image.", Toast.LENGTH_SHORT).show();
                        adminName.setEnabled(true);
                        adminId.setEnabled(true);
                        adminBranch.setEnabled(true);
                        branchSpinner.setEnabled(true);
                    }
                } else {
                    String retrieving_error = task.getException().getMessage();
                    Toast.makeText(AdminSetupActivity.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                }
                adminSetupProgress.setVisibility(View.INVISIBLE);
                adminSetupBtn.setEnabled(true);
            }
        });
        adminSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String admin_name = adminName.getText().toString();
                final String admin_branch = adminBranch.getText().toString();
                final String admin_id = adminId.getText().toString();
                if (!TextUtils.isEmpty(admin_name) && !TextUtils.isEmpty(admin_branch) && !TextUtils.isEmpty(admin_id) && adminMainImageURI != null) {
                    adminSetupProgress.setVisibility(View.VISIBLE);
                    if (isChanged) {
                        StorageReference image_path = madminStorageReference.child("admin_profile_images").child(user_id + ".jpg");
                        image_path.putFile(adminMainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeFireStore(task, admin_name, admin_branch, admin_id);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(AdminSetupActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                    adminSetupProgress.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    } else {
                        storeFireStore(null, admin_name, admin_branch, admin_id);
                    }

                } else {
                    Toast.makeText(AdminSetupActivity.this, "Fill all the details and add a profile image also.", Toast.LENGTH_LONG).show();
                }
            }
        });
        adminSetupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ContextCompat.
                            checkSelfPermission(AdminSetupActivity.this,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                            (ContextCompat.
                                    checkSelfPermission(AdminSetupActivity.this,
                                            android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(AdminSetupActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        ActivityCompat.requestPermissions(AdminSetupActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        cropImage();
                    }
                } else {
                    cropImage();
                }
            }
        });
    }

    private void storeFireStore(Task<UploadTask.TaskSnapshot> task, String admin_name, String admin_branch, String admin_id) {
        Uri download_uri;
        if (task != null) {
            download_uri = task.getResult().getDownloadUrl();
        } else {
            download_uri = adminMainImageURI;
        }
        HashMap<String, String> adminMap = new HashMap<>();
        adminMap.put("name", admin_name);
        adminMap.put("branch", admin_branch);
        adminMap.put("id", admin_id);
        adminMap.put("image", download_uri.toString());
        adminSetupFirestore.collection("Admin").document(user_id).set(adminMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminSetupActivity.this, "All the Data Has Been Uploaded.", Toast.LENGTH_LONG).show();
                } else {
                    String image_error = task.getException().getMessage();
                    Toast.makeText(AdminSetupActivity.this, "Error: " + image_error, Toast.LENGTH_SHORT).show();
                }
                adminSetupProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                adminMainImageURI = result.getUri();
                adminSetupImage.setImageURI(adminMainImageURI);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AdminSetupActivity.this);
    }
}
