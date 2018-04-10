package com.application.pradyotprakash.newattendanceappadmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassTeacherRecyclerAdapter extends RecyclerView.Adapter<ClassTeacherRecyclerAdapter.ViewHolder> {

    private List<ClassTeacher> facultyList;
    private Context context;
    private String classId = AssignClassTeacher.getSubjectId(), classValueString = AssignClassTeacher.getClassValueString(), branchValue = AssignClassTeacher.getBranchValue(), semesterValue = AssignClassTeacher.getSemesterValue();
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2, mFirestore3, mFirestore4, mFirestore5, mFirestore6;

    public ClassTeacherRecyclerAdapter(List<ClassTeacher> facultyList, Context context) {
        this.facultyList = facultyList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ClassTeacherRecyclerAdapter.ViewHolder holder, final int position) {
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        mFirestore3 = FirebaseFirestore.getInstance();
        mFirestore4 = FirebaseFirestore.getInstance();
        mFirestore5 = FirebaseFirestore.getInstance();
        mFirestore6 = FirebaseFirestore.getInstance();
        final String facultyId = facultyList.get(position).facultyId;
        if (branchValue.equals(facultyList.get(position).getBranch())) {
            mFirestore3.collection("Faculty").document(facultyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            if (task.getResult().getString("classTeacherValue").equals("true")) {
                                holder.mView.setBackgroundColor(Color.BLUE);
                                holder.faculty_list_name.setTextColor(Color.WHITE);
                            }
                        }
                    }
                }
            });
            holder.faculty_list_name.setText(facultyList.get(position).getName());
            CircleImageView mImageView = holder.faculty_list_image;
            Glide.with(context).load(facultyList.get(position).getImage()).into(mImageView);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFirestore2.collection("Faculty").document(facultyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    if (task.getResult().getString("classTeacherValue").equals("false")) {
                                        mFirestore6.collection("Class").document(branchValue).collection(semesterValue).document(classId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        if (task.getResult().getString("classTeacher").equals("Assign Teacher")) {
                                                            HashMap<String, Object> classTeacher = new HashMap<>();
                                                            classTeacher.put("classTeacher", facultyId);
                                                            mFirestore.collection("Class").document(branchValue).collection(semesterValue).document(classId).update(classTeacher).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    HashMap<String, Object> classTeacherFaculty = new HashMap<>();
                                                                    classTeacherFaculty.put("classTeacherOf", classId);
                                                                    classTeacherFaculty.put("classTeacherValue", "true");
                                                                    mFirestore1.collection("Faculty").document(facultyId).update(classTeacherFaculty).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(context, classId + " is assigned to " + facultyList.get(position).getName(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(context, "Someone is assigned as a class teacher of " + classId, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(context, facultyList.get(position).getName() + " is already a class teacher of " + task.getResult().getString("classTeacherOf") + ". Long Press To Remove It.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
            });
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    HashMap<String, Object> removeClassTeacher = new HashMap<>();
                    removeClassTeacher.put("classTeacher", "Assign Teacher");
                    mFirestore4.collection("Class").document(branchValue).collection(semesterValue).document(classId).update(removeClassTeacher).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HashMap<String, Object> classTeacherFaculty = new HashMap<>();
                            classTeacherFaculty.put("classTeacherOf", "No Data Selected");
                            classTeacherFaculty.put("classTeacherValue", "false");
                            mFirestore5.collection("Faculty").document(facultyId).update(classTeacherFaculty).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, classId + " is removed from " + facultyList.get(position).getName(), Toast.LENGTH_SHORT).show();
                                    holder.mView.setBackgroundColor(Color.TRANSPARENT);
                                    holder.faculty_list_name.setTextColor(Color.BLACK);
                                }
                            });
                        }
                    });
                    return true;
                }
            });
        } else {
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView faculty_list_name;
        private CircleImageView faculty_list_image;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            faculty_list_name = mView.findViewById(R.id.faculty_list_name);
            faculty_list_image = mView.findViewById(R.id.faculty_list_image);
        }
    }
}
