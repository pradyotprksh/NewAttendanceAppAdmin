package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
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

public class TimetableRecyclerAdapter extends RecyclerView.Adapter<TimetableRecyclerAdapter.ViewHolder> {

    private List<SubjectsTimetable> subjectList;
    private Context context;
    private String subjectCode = AddTimetable.getSubjectCode(), facultyId;
    private FirebaseFirestore mFirestore;

    public TimetableRecyclerAdapter(List<SubjectsTimetable> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public TimetableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monday_subject_list, parent, false);
        return new TimetableRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimetableRecyclerAdapter.ViewHolder holder, int position) {
        mFirestore = FirebaseFirestore.getInstance();
        if (subjectCode.equals(subjectList.get(position).getSubjectCode())) {
            holder.subject.setText(subjectList.get(position).getSubjectName());
            String from = subjectList.get(position).getFrom();
            String to = subjectList.get(position).getTo();
            String timeValue = from + " : " + to;
            holder.time.setText(timeValue);
            facultyId = subjectList.get(position).getSubjectTeacher();
            mFirestore.collection("Faculty").document(facultyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            holder.takenByValue.setText(task.getResult().getString("name"));
                        }
                    }
                }
            });
            holder.subjectCode.setText(subjectList.get(position).getSubjectCode());
        } else {
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
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
