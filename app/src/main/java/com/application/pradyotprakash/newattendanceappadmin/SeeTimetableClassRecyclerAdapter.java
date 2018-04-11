package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SeeTimetableClassRecyclerAdapter extends RecyclerView.Adapter<SeeTimetableClassRecyclerAdapter.ViewHolder>{

    private List<TimetableClasses> classList;
    private Context context;

    public SeeTimetableClassRecyclerAdapter(List<TimetableClasses> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @Override
    public SeeTimetableClassRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_class_list_item, parent, false);
        return new SeeTimetableClassRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeeTimetableClassRecyclerAdapter.ViewHolder holder, final int position) {
        holder.classValue.setText(classList.get(position).getClassValue());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminTimetable.class);
                intent.putExtra("classValue", classList.get(position).getClassValue());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView classValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            classValue = mView.findViewById(R.id.class_value);
        }
    }
}
