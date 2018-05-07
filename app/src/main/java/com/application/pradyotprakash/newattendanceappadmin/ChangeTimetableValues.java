package com.application.pradyotprakash.newattendanceappadmin;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ChangeTimetableValues extends AppCompatActivity {

    private String dayValue, timetableId, classValue, subjectTeacher, subjectCode, from, to, subjectName;
    private TextView subjectValue, subjectTeacherValue, subjectIdValue, removeAssignment, weekDay, changeAssignment;
    private Button fromValue, toValue;
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2, mFirestore3, mFirestore4;
    private int hourValue;
    private int minuteValue;
    private ProgressDialog progress, progress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_timetable_values);
        Toolbar mToolbar = findViewById(R.id.adminEachClassTeacherToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Timetable Details");
        dayValue = getIntent().getStringExtra("dayValue");
        timetableId = getIntent().getStringExtra("timetableId");
        classValue = getIntent().getStringExtra("classValue");
        subjectValue = findViewById(R.id.subjectValue);
        subjectTeacherValue = findViewById(R.id.subjectTeacherValue);
        subjectIdValue = findViewById(R.id.subjectIdValue);
        removeAssignment = findViewById(R.id.removeAssignment);
        weekDay = findViewById(R.id.weekDay);
        changeAssignment = findViewById(R.id.editAssignment);
        fromValue = findViewById(R.id.fromValue);
        toValue = findViewById(R.id.toValue);
        fromValue.setEnabled(false);
        toValue.setEnabled(false);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        mFirestore3 = FirebaseFirestore.getInstance();
        mFirestore4 = FirebaseFirestore.getInstance();
        mFirestore.collection("Timetable").document(classValue).collection(dayValue).document(timetableId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        subjectValue.setText(task.getResult().getString("subjectName"));
                        subjectName = task.getResult().getString("subjectName");
                        subjectIdValue.setText(task.getResult().getString("subjectCode"));
                        subjectCode = task.getResult().getString("subjectCode");
                        weekDay.setText("On: " + task.getResult().getString("weekDay"));
                        fromValue.setText(task.getResult().getString("from"));
                        toValue.setText(task.getResult().getString("to"));
                        subjectTeacher = task.getResult().getString("subjectTeacher");
                        String subjectTeacherId = task.getResult().getString("subjectTeacher");
                        mFirestore1.collection("Faculty").document(subjectTeacherId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        subjectTeacherValue.setText("Taken By: " + task.getResult().getString("name"));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        progress = new ProgressDialog(ChangeTimetableValues.this);
        progress.setTitle("Please Wait.");
        progress.setMessage("Changing Timings. It will take some time.");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        changeAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeAssignment.getText().toString().equals("To Edit This Assignment Click Here")) {
                    from = fromValue.getText().toString();
                    to = toValue.getText().toString();
                    fromValue.setEnabled(true);
                    toValue.setEnabled(true);
                    changeAssignment.setText("Save Changes?");
                } else {
                    progress.show();
                    HashMap<String, Object> removeTimetable = new HashMap<>();
                    removeTimetable.put("subjectName", FieldValue.delete());
                    removeTimetable.put("subjectCode", FieldValue.delete());
                    removeTimetable.put("subjectTeacher", FieldValue.delete());
                    removeTimetable.put("from", FieldValue.delete());
                    removeTimetable.put("to", FieldValue.delete());
                    removeTimetable.put("weekDay", FieldValue.delete());
                    mFirestore4.collection("Timetable").document(classValue).collection(dayValue).document(timetableId).update(removeTimetable).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HashMap<String, Object> newTime = new HashMap<>();
                            newTime.put("subjectName", subjectName);
                            newTime.put("subjectCode", subjectCode);
                            newTime.put("subjectTeacher", subjectTeacher);
                            newTime.put("from", fromValue.getText().toString());
                            newTime.put("to", toValue.getText().toString());
                            newTime.put("weekDay", dayValue);
                            mFirestore2.collection("Timetable").document(classValue).collection(dayValue).document(subjectCode + fromValue.getText().toString() + dayValue).set(newTime).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    HashMap<String, Object> removeTimetable = new HashMap<>();
                                    removeTimetable.put("subjectName", FieldValue.delete());
                                    removeTimetable.put("subjectCode", FieldValue.delete());
                                    removeTimetable.put("from", FieldValue.delete());
                                    removeTimetable.put("to", FieldValue.delete());
                                    removeTimetable.put("weekDay", FieldValue.delete());
                                    removeTimetable.put("classValue", FieldValue.delete());
                                    mFirestore1.collection("Faculty").document(subjectTeacher).collection("Timetable").document(subjectCode + from + dayValue).update(removeTimetable).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            HashMap<String, Object> addTimetable = new HashMap<>();
                                            addTimetable.put("subjectName", subjectName);
                                            addTimetable.put("subjectCode", subjectCode);
                                            addTimetable.put("from", fromValue.getText().toString());
                                            addTimetable.put("to", toValue.getText().toString());
                                            addTimetable.put("weekDay", dayValue);
                                            addTimetable.put("classValue", classValue);
                                            mFirestore3.collection("Faculty").document(subjectTeacher).collection("Timetable").document(subjectCode + fromValue.getText().toString() + dayValue).set(addTimetable).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ChangeTimetableValues.this,"Timing changed", Toast.LENGTH_SHORT).show();
                                                    fromValue.setEnabled(false);
                                                    toValue.setEnabled(false);
                                                    changeAssignment.setText("To Edit This Assignment Click Here");
                                                    progress.dismiss();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
        progress.dismiss();
        fromValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ChangeTimetableValues.this, timeFromPickerListener, hourValue, minuteValue, false);
                timePickerDialog.show();
            }
        });
        toValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ChangeTimetableValues.this, timeToPickerListener, hourValue, minuteValue, false);
                timePickerDialog.show();
            }
        });
        progress1 = new ProgressDialog(ChangeTimetableValues.this);
        progress1.setTitle("Please Wait.");
        progress1.setMessage("Removing timetable. It will take some time.");
        progress1.setCancelable(false);
        progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        removeAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress1.show();
                HashMap<String, Object> removeTimetable = new HashMap<>();
                removeTimetable.put("subjectName", FieldValue.delete());
                removeTimetable.put("subjectCode", FieldValue.delete());
                removeTimetable.put("subjectTeacher", FieldValue.delete());
                removeTimetable.put("from", FieldValue.delete());
                removeTimetable.put("to", FieldValue.delete());
                removeTimetable.put("weekDay", FieldValue.delete());
                mFirestore4.collection("Timetable").document(classValue).collection(dayValue).document(timetableId).update(removeTimetable).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String, Object> removeTimetable = new HashMap<>();
                        removeTimetable.put("subjectName", FieldValue.delete());
                        removeTimetable.put("subjectCode", FieldValue.delete());
                        removeTimetable.put("from", FieldValue.delete());
                        removeTimetable.put("to", FieldValue.delete());
                        removeTimetable.put("weekDay", FieldValue.delete());
                        removeTimetable.put("classValue", FieldValue.delete());
                        mFirestore1.collection("Faculty").document(subjectTeacher).collection("Timetable").document(subjectCode + fromValue.getText().toString() + dayValue).update(removeTimetable).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ChangeTimetableValues.this,"Timetable Removed.", Toast.LENGTH_SHORT).show();
                                progress1.dismiss();
                            }
                        });
                    }
                });
            }
        });
        progress1.dismiss();
    }

    private TimePickerDialog.OnTimeSetListener timeFromPickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (hourOfDay == 0) {
                hourOfDay = 12;
            }
            String hourString = String.format("%02d", hourOfDay);
            String minuteString = String.valueOf(minute);
            String time = hourString + ":" + minuteString;
            fromValue.setText(time);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeToPickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (hourOfDay == 0) {
                hourOfDay = 12;
            }
            String hourString = String.format("%02d", hourOfDay);
            String minuteString = String.valueOf(minute);
            String time = hourString + ":" + minuteString;
            toValue.setText(time);
        }
    };
}
