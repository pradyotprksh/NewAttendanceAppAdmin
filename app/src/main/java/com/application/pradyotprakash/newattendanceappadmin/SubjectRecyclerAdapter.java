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

public class SubjectRecyclerAdapter extends RecyclerView.Adapter<SubjectRecyclerAdapter.ViewHolder> {

    private List<SubjectList> subjectList;
    private Context context;
    private FirebaseFirestore mFirestore;
    private String subjectTeacherId, facultyName;

    public SubjectRecyclerAdapter(List<SubjectList> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubjectRecyclerAdapter.ViewHolder holder, final int position) {
        final String subjectId = subjectList.get(position).subjectId;
        final String branchValue = subjectList.get(position).getBranch();
        final String semesterValue = subjectList.get(position).getSemester();
        mFirestore = FirebaseFirestore.getInstance();
        holder.subjectValue.setText(subjectList.get(position).getSubjectName());
        subjectTeacherId = subjectList.get(position).getSubjectTeacher();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectClassSubjectTeacher.class);
                intent.putExtra("branch", branchValue);
                intent.putExtra("semester", semesterValue);
                intent.putExtra("subjectId", subjectId);
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
        private TextView subjectValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subjectValue = mView.findViewById(R.id.subject_value);
        }
    }
}
