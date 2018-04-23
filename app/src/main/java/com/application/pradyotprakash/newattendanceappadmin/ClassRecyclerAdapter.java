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

public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.ViewHolder> {

    private List<ClassList> subjectList;
    private Context context;
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2;
    private String classTeacherId, facultyName;

    public ClassRecyclerAdapter(List<ClassList> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public ClassRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_item, parent, false);
        return new ClassRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ClassRecyclerAdapter.ViewHolder holder, final int position) {
        final String classValueString = subjectList.get(position).getClassValue();
        final String branchValue = subjectList.get(position).getBranch();
        final String semesterValue = subjectList.get(position).getSemester();
        mFirestore = FirebaseFirestore.getInstance();
        holder.classValue.setText(subjectList.get(position).getClassValue());
        classTeacherId = subjectList.get(position).getClassTeacher();
        if (classTeacherId.equals("Class Teacher Not Assigned")) {
            holder.teacherValue.setText(subjectList.get(position).getClassTeacher());
        } else {
            mFirestore.collection("Faculty").document(classTeacherId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
        final String subjectId = SelectClassSubjectTeacher.getSubjectId();
        final String branchValueString = SelectClassSubjectTeacher.getBranchValue();
        final String semesterValueString = SelectClassSubjectTeacher.getSemesterValue();
        final String subjectName = SelectClassSubjectTeacher.getSubjectName();
        final String classId = subjectList.get(position).subjectId;
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mFirestore1.collection("Subject").document(branchValueString).collection(semesterValueString).document(subjectId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    final String facultyId = task.getResult().getString(classId);
                                    try {
                                        mFirestore2.collection("Faculty").document(facultyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        String facultyName = task.getResult().getString("name");
                                                        Intent intent = new Intent(context, EachSubjectDetails.class);
                                                        intent.putExtra("classId", classId);
                                                        intent.putExtra("facultyName", facultyName);
                                                        intent.putExtra("subjectId", subjectId);
                                                        intent.putExtra("subjectName", subjectName);
                                                        intent.putExtra("branch", branchValue);
                                                        intent.putExtra("semester", semesterValue);
                                                        intent.putExtra("facultyId", facultyId);
                                                        context.startActivity(intent);
                                                    }
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        Intent intent = new Intent(context, AssignSubjectTeacher.class);
                                        intent.putExtra("subjectId", subjectId);
                                        intent.putExtra("branch", branchValueString);
                                        intent.putExtra("semester", semesterValueString);
                                        intent.putExtra("subjectName", subjectName);
                                        intent.putExtra("classId", classId);
                                        context.startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Intent intent = new Intent(context, AssignSubjectTeacher.class);
                    intent.putExtra("subjectId", subjectId);
                    intent.putExtra("branch", branchValueString);
                    intent.putExtra("semester", semesterValueString);
                    intent.putExtra("subjectName", subjectName);
                    intent.putExtra("classId", classId);
                    context.startActivity(intent);
                }
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
            teacherValue = mView.findViewById(R.id.subjectTeacher_value);
            classValue = mView.findViewById(R.id.class_value);
        }
    }
}
