package com.example.dana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Part5 extends AppCompatActivity {


    RecyclerView recyclerView;
    Adapter2 adapter;
    List<Model> modelList;
    Model model;

    DBHelper dbHelper;
    private static final String TABLE = "student";
    SQLiteDatabase sqLiteDatabase;
    Button buttonFetch;
    FloatingActionButton btnInsert;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part5);

        progressdialog = new ProgressDialog(Part5.this);
        progressdialog.setMessage("Please Wait....");
        dbHelper = new DBHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView5);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        buttonFetch = (Button) findViewById(R.id.btnFetch);
        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog.show();
                fetchData();
            }
        });

        btnInsert = (FloatingActionButton) findViewById(R.id.btnInsertSQLite);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog.show();
                Insert(Part5.this);
            }
        });

        displayData();

    }

    private void fetchData() {
        Query query = FirebaseDatabase.getInstance().getReference("Student");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long recid = null;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                    String ID,Name,Surname,Fathers_name,National_ID,date_of_birth,Gender;

                    ID = childSnapshot.getKey();
                    Name = childSnapshot.child("Name").getValue(String.class);
                    Surname = childSnapshot.child("Surname").getValue(String.class);
                    Fathers_name = childSnapshot.child("Fathers_name").getValue(String.class);
                    National_ID = childSnapshot.child("National_ID").getValue(String.class);
                    date_of_birth = childSnapshot.child("date_of_birth").getValue(String.class);
                    Gender = childSnapshot.child("Gender").getValue(String.class);

                    sqLiteDatabase = dbHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ID", Integer.valueOf(ID));
                    contentValues.put("Name", Name);
                    contentValues.put("Surname", Surname);
                    contentValues.put("Fathers_name", Fathers_name);
                    contentValues.put("National_ID", National_ID);
                    contentValues.put("date_of_birth", date_of_birth);
                    contentValues.put("Gender", Gender);

                    recid = sqLiteDatabase.insert(TABLE, null, contentValues);

                }
                if (recid != null) {
                    Toast.makeText(Part5.this, "successfully retrieved from firebase and loaded in SQlite", Toast.LENGTH_SHORT).show();
                    displayData();
                } else {
                    progressdialog.dismiss();
                    Toast.makeText(Part5.this, "something wrong try again", Toast.LENGTH_SHORT).show();
                }

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
                        insert(UserID.getText().toString(),Name.getText().toString(),Surname.getText().toString(),Fathers_name.getText().toString(),National_ID.getText().toString(),date_of_birth.getText().toString(),Gender.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void insert(String UserID,String Name, String Surname, String Fathers_name, String National_ID, String date_of_birth, String Gender){
        FirebaseDatabase database =  FirebaseDatabase.getInstance();

        sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", Integer.valueOf(UserID));
        contentValues.put("Name", Name);
        contentValues.put("Surname", Surname);
        contentValues.put("Fathers_name", Fathers_name);
        contentValues.put("National_ID", National_ID);
        contentValues.put("date_of_birth", date_of_birth);
        contentValues.put("Gender", Gender);

        Long recid = sqLiteDatabase.insert(TABLE, null, contentValues);

        if (recid != null) {
            Toast.makeText(Part5.this, "successfully Added", Toast.LENGTH_SHORT).show();
            displayData();
        } else {
            Toast.makeText(Part5.this, "something wrong try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayData() {

        sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE, null);

        ArrayList<Model> studentModelList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                studentModelList.add(new Model(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)));


                adapter = new Adapter2(this,studentModelList,Part5.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        cursor.close();
        progressdialog.dismiss();

    }
}