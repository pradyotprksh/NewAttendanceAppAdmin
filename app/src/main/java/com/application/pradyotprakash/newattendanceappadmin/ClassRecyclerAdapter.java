package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pradyotprakash on 22/02/18.
 */

public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.ViewHolder> {

    private List<Classes> classList;
    private Context context;

    public ClassRecyclerAdapter(Context context, List<Classes> classList) {
        this.classList = classList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String adminBranch = AdminAllStudentList.adminBranch();
        if (adminBranch.equals(classList.get(position).getBranch())) {
            holder.semester.setText(classList.get(position).getSemester());
            holder.classValue.setText(classList.get(position).getClassValue());
        } else {
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView semester;
        private TextView classValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            semester = mView.findViewById(R.id.semester_value);
            classValue = mView.findViewById(R.id.class_value);
        }
    }
}
