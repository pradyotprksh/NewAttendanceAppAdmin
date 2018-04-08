package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AddClassRecyclerAdapter extends RecyclerView.Adapter<AddClassRecyclerAdapter.ViewHolder> {

    private List<AddClassList> subjectList;
    private Context context;

    public AddClassRecyclerAdapter(List<AddClassList> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String subjectId = subjectList.get(position).subjectId;
        final String classValueString = subjectList.get(position).getClassValue();
        final String branchValue = subjectList.get(position).getBranch();
        final String semesterValue = subjectList.get(position).getSemester();
        holder.classValue.setText(subjectList.get(position).getClassValue());
        holder.teacherValue.setText(subjectList.get(position).getClassTeacher());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AssignClassTeacher.class);
                intent.putExtra("subjectId", subjectId);
                intent.putExtra("classValue", classValueString);
                intent.putExtra("branchValue", branchValue);
                intent.putExtra("semesterValue", semesterValue);
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
        private TextView teacherValue, classValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            teacherValue = mView.findViewById(R.id.classTeacher_value);
            classValue = mView.findViewById(R.id.class_value);
        }
    }
}
