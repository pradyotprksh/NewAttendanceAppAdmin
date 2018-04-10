package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TimetableSubjectRecyclerAdapter extends RecyclerView.Adapter<TimetableSubjectRecyclerAdapter.ViewHolder> {

    private List<SubjectList> subjectList;
    private Context context;
    private FirebaseFirestore mFirestore;
    private String subjectTeacherId, facultyName, classValue = SelectSubjectTimetable.getClassValue();

    public TimetableSubjectRecyclerAdapter(List<SubjectList> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimetableSubjectRecyclerAdapter.ViewHolder holder, final int position) {
        final String subjectId = subjectList.get(position).subjectId;
        final String branchValue = subjectList.get(position).getBranch();
        final String semesterValue = subjectList.get(position).getSemester();
        mFirestore = FirebaseFirestore.getInstance();
        holder.subjectValue.setText(subjectList.get(position).getSubjectName());
        subjectTeacherId = subjectList.get(position).getSubjectTeacher();
        if (subjectTeacherId.equals("Assign Subject Teacher")) {
            holder.teacherValue.setText(subjectList.get(position).getSubjectTeacher());
        } else {
            mFirestore.collection("Faculty").document(subjectTeacherId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            facultyName = task.getResult().getString("name");
                            holder.teacherValue.setText(facultyName);
                        }
                    }
                }
            });
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddTimetable.class);
                intent.putExtra("classValue", classValue);
                intent.putExtra("subjectCode", subjectId);
                intent.putExtra("subjectTeacher", subjectTeacherId);
                intent.putExtra("subjectName", subjectList.get(position).getSubjectName());
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
        private TextView teacherValue, subjectValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            teacherValue = mView.findViewById(R.id.subjectTeacher_value);
            subjectValue = mView.findViewById(R.id.subject_value);
        }
    }
}
