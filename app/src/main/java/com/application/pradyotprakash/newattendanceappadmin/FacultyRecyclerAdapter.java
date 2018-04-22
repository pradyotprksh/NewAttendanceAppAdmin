package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyRecyclerAdapter extends RecyclerView.Adapter<FacultyRecyclerAdapter.ViewHolder> {

    private List<Faculties> studentsList;
    private Context context;

    public FacultyRecyclerAdapter(Context context, List<Faculties> studentsList) {
        this.studentsList = studentsList;
        this.context = context;
    }

    @Override
    public FacultyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item, parent, false);
        return new FacultyRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FacultyRecyclerAdapter.ViewHolder holder, int position) {
        String adminBranch = AdminAllFacultyList.adminBranch();
        if (adminBranch.equals(studentsList.get(position).getBranch())) {
            holder.mUsn.setText(studentsList.get(position).getName());
            CircleImageView mImageView = holder.mImage;
            Glide.with(context).load(studentsList.get(position).getImage()).into(mImageView);
            final String faculty_id = studentsList.get(position).facultyId;
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EachFacultyDetails.class);
                    intent.putExtra("facultyId", faculty_id);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public void setfilter(List<Faculties> listitem) {
        studentsList = new ArrayList<>();
        studentsList.addAll(listitem);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView mImage;
        private TextView mUsn;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = mView.findViewById(R.id.student_list_image);
            mUsn = mView.findViewById(R.id.student_list_usn);
        }
    }

}
