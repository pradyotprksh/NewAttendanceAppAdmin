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
        holder.subject.setText(subjectList.get(position).getSubject());
        holder.from.setText(subjectList.get(position).getFrom());
        holder.to.setText(subjectList.get(position).getTo());
        holder.takenByValue.setText(subjectList.get(position).getTakenBy());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView subject, from, to, takenByValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subject = mView.findViewById(R.id.subject_value);
            from = mView.findViewById(R.id.from_value);
            to = mView.findViewById(R.id.to_value);
            takenByValue = mView.findViewById(R.id.taken_by_value);
        }
    }
}
