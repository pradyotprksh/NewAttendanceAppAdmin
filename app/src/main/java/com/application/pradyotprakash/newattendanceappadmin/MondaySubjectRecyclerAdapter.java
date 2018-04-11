package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pradyotprakash on 25/02/18.
 */

public class MondaySubjectRecyclerAdapter extends RecyclerView.Adapter<MondaySubjectRecyclerAdapter.ViewHolder> {

    private List<MondaySubjects> subjectList;
    private Context context;

    public MondaySubjectRecyclerAdapter(List<MondaySubjects> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monday_subject_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.subject.setText(subjectList.get(position).getSubjectName());
        String from = subjectList.get(position).getFrom();
        String to = subjectList.get(position).getTo();
        String timeValue = from + " : " + to;
        holder.time.setText(timeValue);
        holder.takenByValue.setText(subjectList.get(position).getSubjectTeacher());
        holder.subjectCode.setText(subjectList.get(position).getSubjectCode());
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
