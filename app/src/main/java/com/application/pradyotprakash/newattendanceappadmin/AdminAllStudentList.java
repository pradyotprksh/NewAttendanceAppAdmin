package com.application.pradyotprakash.newattendanceappadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAllStudentList extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mStudentListView;
    private List<Students> studentsList;
    private StudentRecyclerAdapter studentRecyclerAdapter;
    private FirebaseFirestore mFirestore;
    public static String branch;
    private Button sendNotification;
    private EditText finalMessage;
    private boolean isReached = false;
    private ProgressDialog progress;
    private String userId, senderName, senderImage;
    private FirebaseAuth mAuth;
    private int position;
    private FirebaseFirestore mFirestore1, mFirestore2;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_student_list);
        mToolbar = findViewById(R.id.admin_allstudentlist_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Student Lists");
        Intent intent = getIntent();
        branch = intent.getStringExtra("branch");
        mFirestore = FirebaseFirestore.getInstance();
        mStudentListView = findViewById(R.id.admin_student_lists);
        studentsList = new ArrayList<>();
        studentsList.clear();
        studentRecyclerAdapter = new StudentRecyclerAdapter(getApplicationContext(), studentsList);
        mStudentListView.setHasFixedSize(true);
        mStudentListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStudentListView.setAdapter(studentRecyclerAdapter);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mStudentListView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(AdminAllStudentList.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        finalMessage = findViewById(R.id.finalMessage);
        finalMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (finalMessage.getText().length() == 50 && !isReached) {
                    finalMessage.append("\n");
                    isReached = true;
                }
                if (finalMessage.getText().length() < 10 && isReached) isReached = false;

            }
        });
        sendNotification = findViewById(R.id.sendNotification);
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(finalMessage.getText().toString())) {
                    progress = new ProgressDialog(AdminAllStudentList.this);
                    progress.setTitle("Please Wait.");
                    progress.setMessage("Sending Message To Every Student.");
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    mFirestore2.collection("Admin").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    senderName = task.getResult().getString("name");
                                    senderImage = task.getResult().getString("image");
                                    for (position = 0; position < studentsList.size(); position++) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mm");
                                        String date = simpleDateFormat.format(new Date());
                                        Map<String, Object> notificationMessage = new HashMap<>();
                                        notificationMessage.put("message", finalMessage.getText().toString());
                                        notificationMessage.put("from", userId);
                                        notificationMessage.put("on", date);
                                        notificationMessage.put("designation", "Admin");
                                        notificationMessage.put("senderName", senderName);
                                        notificationMessage.put("senderImage", senderImage);
                                        mFirestore1.collection("Student/" + studentsList.get(position).studentId + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(AdminAllStudentList.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                                finalMessage.setText("");
                                            }
                                        });
                                    }
                                    progress.dismiss();
                                }
                            } else {
                                String retrieving_error = task.getException().getMessage();
                                Toast.makeText(AdminAllStudentList.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });
                } else {
                    LayoutInflater li = LayoutInflater.from(AdminAllStudentList.this);
                    View promptsView = li.inflate(R.layout.prompts, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdminAllStudentList.this);
                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput = promptsView
                            .findViewById(R.id.editTextDialogUserInput);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finalMessage.setText(userInput.getText());
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
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentsList.clear();
        mFirestore.collection("Student").orderBy("usn").addSnapshotListener(AdminAllStudentList.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String student_id = doc.getDocument().getId();
                        Students students = doc.getDocument().toObject(Students.class).withId(student_id);
                        studentsList.add(students);
                        studentRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchfile, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(
                android.support.v7.appcompat.R.id.search_src_text)).
                setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Students> filtermodelist = filter(studentsList, newText);
                studentRecyclerAdapter.setfilter(filtermodelist);
                return true;
            }
        });
        return true;
    }

    private List<Students> filter(List<Students> pl, String query) {
        query = query.toLowerCase();
        final List<Students> filteredModeList = new ArrayList<>();
        for (Students model : pl) {
            final String text = model.getUsn().toLowerCase();
            if (text.startsWith(query)) {
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    public static String adminBranch() {
        return branch;
    }

}
