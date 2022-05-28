package com.example.dana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Part1 extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    List<Model> modelList;
    Model model;

    FloatingActionButton floatingActionButton;
    ProgressDialog progressdialog;

    TextView textViewCity,textViewTemprature;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1);

        textViewCity = (TextView) findViewById(R.id.txtcity1);
        textViewTemprature = (TextView) findViewById(R.id.txtTemprature1);
        imageView = (ImageView) findViewById(R.id.ivWeather1);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Part1.this);

        textViewCity.setText(preferences.getString("City", "0"));
        textViewTemprature.setText(preferences.getString("Temprature", "0"));
        Glide.with(Part1.this).load(preferences.getString("Image", "0")).into(imageView);



        progressdialog = new ProgressDialog(Part1.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.btnInsert);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog.show();
                Insert(Part1.this);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        modelList=new ArrayList<>();
        adapter = new Adapter(this,modelList,1);
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



    private void Insert(Context c) {
        LinearLayout layout = new LinearLayout(c);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16,16,16,16);

        final EditText UserID = new EditText(c);
        UserID.setHint("User ID");
        layout.addView(UserID);

        final EditText Name = new EditText(c);
        Name.setHint("Name");
        layout.addView(Name);

        final EditText Surname = new EditText(c);
        Surname.setHint("Surname");
        layout.addView(Surname);

        final EditText Fathers_name = new EditText(c);
        Fathers_name.setHint("Father's Name");
        layout.addView(Fathers_name);

        final EditText National_ID = new EditText(c);
        National_ID.setHint("National ID");
        layout.addView(National_ID);

        final EditText date_of_birth = new EditText(c);
        date_of_birth.setHint("Date of Birth");
        layout.addView(date_of_birth);

        final EditText Gender = new EditText(c);
        Gender.setHint("Gender");
        layout.addView(Gender);

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Create new student record")
                .setMessage("please fill all the fields")
                .setView(layout)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        create(UserID.getText().toString(),Name.getText().toString(),Surname.getText().toString(),Fathers_name.getText().toString(),National_ID.getText().toString(),date_of_birth.getText().toString(),Gender.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    private void create(String UserID,String Name, String Surname, String Fathers_name, String National_ID, String date_of_birth, String Gender){
        FirebaseDatabase database =  FirebaseDatabase.getInstance();

        DatabaseReference mRef =  database.getReference().child("Student").child(UserID);
        mRef.child("Name").setValue(Name);
        mRef.child("Surname").setValue(Surname);
        mRef.child("Fathers_name").setValue(Fathers_name);
        mRef.child("National_ID").setValue(National_ID);
        mRef.child("date_of_birth").setValue(date_of_birth);
        mRef.child("Gender").setValue(Gender);
        Toast.makeText(Part1.this, "Successfully Created", Toast.LENGTH_SHORT).show();
    }
}