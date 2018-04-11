package com.application.pradyotprakash.newattendanceappadmin;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddTimetable extends AppCompatActivity {

    private static String classValue, subjectName, subjectCode, subjectTeacher;
    private android.support.v7.widget.Toolbar mToolbar;
    private AutoCompleteTextView weekOption;
    private ImageView daySpinner;
    private TextView subjectValue, subjectIdValue;
    private Button from_btn, to_btn, addTimetable;
    private int hourValue;
    private int minuteValue;
    private FirebaseFirestore mFirestore, mFirestore1;
    private static final String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private RecyclerView mSubjectListView;
    private List<SubjectsTimetable> subjectList;
    private TimetableRecyclerAdapter subjectRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);
        classValue = getIntent().getStringExtra("classValue");
        subjectName = getIntent().getStringExtra("subjectName");
        subjectCode = getIntent().getStringExtra("subjectCode");
        subjectTeacher = getIntent().getStringExtra("subjectTeacher");
        mToolbar = findViewById(R.id.adminSetupToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Timetable For " + classValue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        subjectValue = findViewById(R.id.subjectValue);
        subjectIdValue = findViewById(R.id.subjectIdValue);
        weekOption = findViewById(R.id.select_weekday);
        daySpinner = findViewById(R.id.day_spinner);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(AddTimetable.this, android.R.layout.simple_dropdown_item_1line, days);
        weekOption.setAdapter(adapterSemester);
        daySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekOption.showDropDown();
            }
        });
        subjectValue.setText(subjectName);
        subjectIdValue.setText(subjectCode);
        from_btn = findViewById(R.id.from_btn);
        from_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTimetable.this, timeFromPickerListener, hourValue, minuteValue, false);
                timePickerDialog.show();
            }
        });
        to_btn = findViewById(R.id.to_btn);
        to_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTimetable.this, timeToPickerListener, hourValue, minuteValue, false);
                timePickerDialog.show();
            }
        });
        addTimetable = findViewById(R.id.add_timetable);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        addTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(weekOption.getText().toString()) && !from_btn.getText().toString().equals("From") && !to_btn.getText().toString().equals("To")) {
                    if (!subjectTeacher.equals("Assign Subject Teacher")) {
                        HashMap<String, Object> addTimetable = new HashMap<>();
                        addTimetable.put("subjectName", subjectName);
                        addTimetable.put("subjectCode", subjectCode);
                        addTimetable.put("subjectTeacher", subjectTeacher);
                        addTimetable.put("from", from_btn.getText().toString());
                        addTimetable.put("to", to_btn.getText().toString());
                        addTimetable.put("weekDay", weekOption.getText().toString());
                        mFirestore.collection("Timetable").document(classValue).collection(weekOption.getText().toString()).document(subjectCode + from_btn.getText().toString() + weekOption.getText().toString()).set(addTimetable).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                HashMap<String, Object> addTimetableFaculty = new HashMap<>();
                                addTimetableFaculty.put("subjectName", subjectName);
                                addTimetableFaculty.put("subjectCode", subjectCode);
                                addTimetableFaculty.put("from", from_btn.getText().toString());
                                addTimetableFaculty.put("to", to_btn.getText().toString());
                                addTimetableFaculty.put("weekDay", weekOption.getText().toString());
                                addTimetableFaculty.put("classValue", classValue);
                                mFirestore1.collection("Faculty").document(subjectTeacher).collection("Timetable").document(subjectCode + from_btn.getText().toString() + weekOption.getText().toString()).set(addTimetableFaculty).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AddTimetable.this, "Timetable Added.", Toast.LENGTH_SHORT).show();
                                        from_btn.setText(to_btn.getText().toString());
                                        to_btn.setText("To");
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(AddTimetable.this, "First Assign The Subject Teacher.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddTimetable.this, "Fill All The Details For Timetable.", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(weekOption.getText().toString())) {
                    mSubjectListView = findViewById(R.id.timetableList);
                    subjectList = new ArrayList<>();
                    subjectRecyclerAdapter = new TimetableRecyclerAdapter(subjectList, AddTimetable.this);
                    mSubjectListView.setHasFixedSize(true);
                    mSubjectListView.setLayoutManager(new LinearLayoutManager(AddTimetable.this));
                    mSubjectListView.setAdapter(subjectRecyclerAdapter);
                    mFirestore = FirebaseFirestore.getInstance();
                    mFirestore.collection("Timetable").document(classValue).collection(weekOption.getText().toString()).orderBy("from").addSnapshotListener(AddTimetable.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    SubjectsTimetable subjects = doc.getDocument().toObject(SubjectsTimetable.class);
                                    subjectList.add(subjects);
                                    subjectRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            }
        });
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
            from_btn.setText(time);
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
            to_btn.setText(time);
        }
    };

    public static String getSubjectCode() {
        return subjectCode;
    }
}
