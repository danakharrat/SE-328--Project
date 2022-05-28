package com.example.dana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Part3 extends AppCompatActivity {


    RecyclerView recyclerView;
    Adapter adapter;
    List<Model> modelList;
    Model model;
    ProgressDialog progressdialog;

    TextView textViewCity,textViewTemprature;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part3);

        textViewCity = (TextView) findViewById(R.id.txtcity3);
        textViewTemprature = (TextView) findViewById(R.id.txtTemprature3);
        imageView = (ImageView) findViewById(R.id.ivWeather3);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Part3.this);

        textViewCity.setText(preferences.getString("City", "0"));
        textViewTemprature.setText(preferences.getString("Temprature", "0"));
        Glide.with(Part3.this).load(preferences.getString("Image", "0")).into(imageView);


        progressdialog = new ProgressDialog(Part3.this);
        progressdialog.setMessage("Please Wait....");
progressdialog.show();
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        modelList=new ArrayList<>();
        adapter = new Adapter(this,modelList,3);
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