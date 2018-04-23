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
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubjectTeacherRecyclerAdapter extends RecyclerView.Adapter<SubjectTeacherRecyclerAdapter.ViewHolder> {

    private List<ClassTeacher> facultyList;
    private Context context;
    private String subjectId = AssignSubjectTeacher.getSubjectId(), branchValue = AssignSubjectTeacher.getBranchValue(), semesterValue = AssignSubjectTeacher.getSemesterValue(), subjectName = AssignSubjectTeacher.getSubjectName(), classId = AssignSubjectTeacher.getClassId();
    private FirebaseFirestore mFirestore, mFirestore1, mfirestore2, mFirestore3;

    public SubjectTeacherRecyclerAdapter(List<ClassTeacher> facultyList, Context context) {
        this.facultyList = facultyList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubjectTeacherRecyclerAdapter.ViewHolder holder, final int position) {
        final String facultyId = facultyList.get(position).facultyId;
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mfirestore2 = FirebaseFirestore.getInstance();
        mFirestore3 = FirebaseFirestore.getInstance();
        if (branchValue.equals(facultyList.get(position).getBranch())) {
            mfirestore2.collection("Subject").document(branchValue).collection(semesterValue).document(subjectId).collection(classId).document(facultyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            try {
                                if (task.getResult().getString("subjectTeacher").equals(facultyId)) {
                                    holder.mView.setBackgroundColor(Color.BLUE);
                                    holder.faculty_list_name.setTextColor(Color.WHITE);
                                }
                            } catch (Exception e) {

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
                    mfirestore2.collection("Subject").document(branchValue).collection(semesterValue).document(subjectId).collection(classId).document(facultyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    try {
                                        if (task.getResult().getString("subjectTeacher").equals(facultyId)) {
                                            Intent intent = new Intent(context, EachSubjectDetails.class);
                                            intent.putExtra("classId", classId);
                                            intent.putExtra("facultyName", facultyList.get(position).getName());
                                            intent.putExtra("subjectId", subjectId);
                                            intent.putExtra("subjectName", subjectName);
                                            intent.putExtra("branch", branchValue);
                                            intent.putExtra("semester", semesterValue);
                                            intent.putExtra("facultyId", facultyId);
                                            context.startActivity(intent);
                                            Toast.makeText(context, subjectId + " is assigned to same faculty.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent intent = new Intent(context, EachSubjectDetails.class);
                                            intent.putExtra("classId", classId);
                                            intent.putExtra("facultyName", facultyList.get(position).getName());
                                            intent.putExtra("subjectId", subjectId);
                                            intent.putExtra("subjectName", subjectName);
                                            intent.putExtra("branch", branchValue);
                                            intent.putExtra("semester", semesterValue);
                                            intent.putExtra("facultyId", facultyId);
                                            Toast.makeText(context, subjectId + " is assigned to some other faculty.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        HashMap<String, Object> addToSubject = new HashMap<>();
                                        addToSubject.put(classId, facultyId);
                                        mFirestore.collection("Subject").document(branchValue).collection(semesterValue).document(subjectId).update(addToSubject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                HashMap<String, Object> assignSubjectTeacher = new HashMap<>();
                                                assignSubjectTeacher.put("subjectTeacher", facultyId);
                                                assignSubjectTeacher.put("classValue", classId);
                                                mFirestore1.collection("Subject").document(branchValue).collection(semesterValue).document(subjectId).collection(classId).document(facultyId).set(assignSubjectTeacher).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        HashMap<String, Object> assignSubjectToFaculty = new HashMap<>();
                                                        assignSubjectToFaculty.put("classValue", classId);
                                                        assignSubjectToFaculty.put("subjectName", subjectName);
                                                        assignSubjectToFaculty.put("branch", branchValue);
                                                        assignSubjectToFaculty.put("semester", semesterValue);
                                                        assignSubjectToFaculty.put("subjectCode", subjectId);
                                                        mFirestore3.collection("Faculty").document(facultyId).collection("Subjects").document(subjectId).set(assignSubjectToFaculty).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(context, subjectId + " is assigned.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                } else {
                                    HashMap<String, Object> addToSubject = new HashMap<>();
                                    addToSubject.put(classId, facultyId);
                                    mFirestore.collection("Subject").document(branchValue).collection(semesterValue).document(subjectId).update(addToSubject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            HashMap<String, Object> assignSubjectTeacher = new HashMap<>();
                                            assignSubjectTeacher.put("subjectTeacher", facultyId);
                                            assignSubjectTeacher.put("classValue", classId);
                                            mFirestore1.collection("Subject").document(branchValue).collection(semesterValue).document(subjectId).collection(classId).document(facultyId).set(assignSubjectTeacher).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    HashMap<String, Object> assignSubjectToFaculty = new HashMap<>();
                                                    assignSubjectToFaculty.put("classValue", classId);
                                                    assignSubjectToFaculty.put("subjectName", subjectName);
                                                    assignSubjectToFaculty.put("branch", branchValue);
                                                    assignSubjectToFaculty.put("semester", semesterValue);
                                                    assignSubjectToFaculty.put("subjectCode", subjectId);
                                                    mFirestore3.collection("Faculty").document(facultyId).collection("Subjects").document(subjectId).set(assignSubjectToFaculty).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(context, subjectId + " is assigned.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });
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
