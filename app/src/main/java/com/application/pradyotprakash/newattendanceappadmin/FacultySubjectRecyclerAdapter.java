package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FacultySubjectRecyclerAdapter extends RecyclerView.Adapter<FacultySubjectRecyclerAdapter.ViewHolder> {

    private List<FacultySubjects> subjectList;
    private Context context;

    public FacultySubjectRecyclerAdapter(List<FacultySubjects> subjectList, Context context) {
        this.subjectList = subjectList;
    }

    @Override
    public FacultySubjectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_subject_list_item, parent, false);
        return new FacultySubjectRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FacultySubjectRecyclerAdapter.ViewHolder holder, int position) {
        holder.subjectName.setText(subjectList.get(position).getSubjectName());
        holder.subjectCode.setText(subjectList.get(position).getSubjectCode());
        holder.subjectSemester.setText(subjectList.get(position).getSemester());
        holder.subjectTotalDays.setText("Total Classes Conducted: " + String.valueOf(subjectList.get(position).getTotalDays()));
        holder.subjectClass.setText("Class: " + subjectList.get(position).getClassValue());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView subjectName, subjectCode, subjectSemester, subjectTotalDays, subjectClass;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subjectName = mView.findViewById(R.id.subjectName);
            subjectCode = mView.findViewById(R.id.subjectCode);
            subjectSemester = mView.findViewById(R.id.subjectSemester);
            subjectTotalDays = mView.findViewById(R.id.subjectTotalDays);
            subjectClass = mView.findViewById(R.id.subjectClassValue);
        }
    }
}
