package com.example.dana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Part2 extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    List<Model> modelList;
    Model model;

    ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part2);

        progressdialog = new ProgressDialog(Part2.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        modelList=new ArrayList<>();
        adapter = new Adapter(this,modelList,2);
        recyclerView.setAdapter(adapter);
        GetStudent();

    }

    private void GetStudent() {
        Query query = FirebaseDatabase.getInstance().getReference("Student");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                modelList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                    String ID,Name,Surname,Fathers_name,National_ID,date_of_birth,Gender;

                    ID = childSnapshot.getKey();
                    Name = childSnapshot.child("Name").getValue(String.class);
                    Surname = childSnapshot.child("Surname").getValue(String.class);
                    Fathers_name = childSnapshot.child("Fathers_name").getValue(String.class);
                    National_ID = childSnapshot.child("National_ID").getValue(String.class);
                    date_of_birth = childSnapshot.child("date_of_birth").getValue(String.class);
                    Gender = childSnapshot.child("Gender").getValue(String.class);


                    model = new Model(Integer.valueOf(ID),Name,Surname,Fathers_name,National_ID,date_of_birth,Gender);
                    modelList.add(model);
                    adapter.notifyDataSetChanged();

                }

                progressdialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}