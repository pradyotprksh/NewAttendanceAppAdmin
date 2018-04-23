package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SaturdaySubjectRecyclerAdapter extends RecyclerView.Adapter<SaturdaySubjectRecyclerAdapter.ViewHolder> {

    private List<MondaySubjects> subjectList;
    private Context context;
    private FirebaseFirestore mFirestore;
    private String classValue = TimetableSaturdayFragment.getClassValue();

    public SaturdaySubjectRecyclerAdapter(List<MondaySubjects> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public SaturdaySubjectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monday_subject_list, parent, false);
        return new SaturdaySubjectRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SaturdaySubjectRecyclerAdapter.ViewHolder holder, final int position) {
        final String timetableId = subjectList.get(position).timetableId;
        holder.subject.setText(subjectList.get(position).getSubjectName());
        String from = subjectList.get(position).getFrom();
        String to = subjectList.get(position).getTo();
        String timeValue = from + " : " + to;
        holder.time.setText(timeValue);
        String facultyId = subjectList.get(position).getSubjectTeacher();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(facultyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");
                        holder.takenByValue.setText(name);
                    }
                }
            }
        });
        holder.subjectCode.setText(subjectList.get(position).getSubjectCode());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeTimetableValues.class);
                intent.putExtra("dayValue", "Saturday");
                intent.putExtra("timetableId", timetableId);
                intent.putExtra("classValue", classValue);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView subject, time, takenByValue, subjectCode;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subject = mView.findViewById(R.id.subject_value);
            time = mView.findViewById(R.id.time_value);
            takenByValue = mView.findViewById(R.id.taken_by_value);
            subjectCode = mView.findViewById(R.id.subject_code);
        }
    }
}
