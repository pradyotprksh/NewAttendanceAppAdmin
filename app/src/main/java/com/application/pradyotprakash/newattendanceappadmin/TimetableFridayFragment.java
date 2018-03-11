package com.application.pradyotprakash.newattendanceappadmin;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableFridayFragment extends Fragment {

    private Button from_btn, to_btn, add;
    private int hourValue;
    private int minuteValue;
    private TextView subjectName;
    private FirebaseFirestore adminAddTimetableFirestore, mFirestore;
    private String classValue;
    private RecyclerView mSubjectListView;
    private List<MondaySubjects> subjectList;
    private MondaySubjectRecyclerAdapter subjectRecyclerAdapter;

    public TimetableFridayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable_friday, container, false);
        if (getArguments() != null) {
            classValue = getArguments().getString("class");
        }
        subjectName = view.findViewById(R.id.subject_name);
        from_btn = view.findViewById(R.id.from_btn);
        from_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), timeFromPickerListener, hourValue, minuteValue, false);
                timePickerDialog.show();
            }
        });
        to_btn = view.findViewById(R.id.to_btn);
        to_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), timeToPickerListener, hourValue, minuteValue, false);
                timePickerDialog.show();
            }
        });
        adminAddTimetableFirestore = FirebaseFirestore.getInstance();
        add = view.findViewById(R.id.add_timetable);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectName.getText().toString().toUpperCase();
                String from = from_btn.getText().toString();
                String to = to_btn.getText().toString();
                if (!TextUtils.isEmpty(subject) && !from.equals("From") && !to.equals("To")) {
                    HashMap<String, String> adminMap = new HashMap<>();
                    adminMap.put("subject", subject);
                    adminMap.put("from", from);
                    adminMap.put("to", to);
                    adminMap.put("takenBy", "Not Assigned");
                    adminAddTimetableFirestore.collection("Timetable").document(classValue).collection("Friday").document().set(adminMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "All the Data Has Been Uploaded.", Toast.LENGTH_LONG).show();
                                from_btn.setText("From");
                                to_btn.setText("To");
                                subjectName.setText("");
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Fill All The Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSubjectListView = view.findViewById(R.id.monday_subject);
        subjectList = new ArrayList<>();
        subjectRecyclerAdapter = new MondaySubjectRecyclerAdapter(subjectList, getContext());
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Timetable").document(classValue).collection("Friday").orderBy("from").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        MondaySubjects subjects = doc.getDocument().toObject(MondaySubjects.class);
                        subjectList.add(subjects);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
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

}
