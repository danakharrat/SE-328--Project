package com.example.dana;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<AdapterViewHolder>{

    Context context;
    private List<Model> modelList;
    Integer isDelete;

    public Adapter(Context context, List<Model> modelList, Integer isDelete) {
        this.context = context;
        this.modelList = modelList;
        this.isDelete = isDelete;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_cell,parent,false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {

        final Model model = modelList.get(position);

        holder.txtID.setText(String.valueOf(model.getID()));
        holder.txtName.setText(model.getName());
        holder.txtSname.setText(model.getSurname());
        holder.txtFname.setText(model.getFathers_name());
        holder.txtNID.setText(model.getNational_ID());
        holder.txtDOB.setText(model.getDate_of_birth());
        holder.txtGender.setText(model.getGender());

        if (isDelete == 3) {
            holder.imageButtonEdit.setVisibility(View.VISIBLE);
            holder.imageButtonDelete.setVisibility(View.VISIBLE);
        }

        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Update(model.getID(), model.getName(), model.getSurname(), model.getFathers_name(), model.getNational_ID(), model.getDate_of_birth(), model.getGender());

            }
        });

        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                alertDialog.setTitle("Message");

                alertDialog.setMessage("Do you want to delete?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = database.getReference().child("Student").child(String.valueOf(model.getID()));
                        mRef.removeValue();
                        int removeIndex = position;
                        modelList.remove(removeIndex);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    }});

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                    }});

                alertDialog.show();

            }
        });



        holder.stdLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (isDelete == 1){

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                    alertDialog.setTitle("Message");

                    alertDialog.setMessage("Do you want to update or delete student record?");

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mRef = database.getReference().child("Student").child(String.valueOf(model.getID()));
                            mRef.removeValue();
                            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                            int removeIndex = position;
                            modelList.remove(removeIndex);
                            notifyDataSetChanged();
                        }
                    });


                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            Update(model.getID(), model.getName(), model.getSurname(), model.getFathers_name(), model.getNational_ID(), model.getDate_of_birth(), model.getGender());


                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            //...

                        }
                    });


                    alertDialog.show();

                }

                return false;
            }

        });

    }

    private void Update(Integer UserID,String NameText, String SurnameText, String Fathers_nameText, String National_idText, String date_of_birthText, String GenderText) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16,16,16,16);



        final EditText Name = new EditText(context);
        Name.setHint("Name");
        Name.setText(NameText);
        layout.addView(Name);

        final EditText Surname = new EditText(context);
        Surname.setHint("Surname");
        Surname.setText(SurnameText);
        layout.addView(Surname);

        final EditText Fathers_name = new EditText(context);
        Fathers_name.setHint("Father's Name");
        Fathers_name.setText(Fathers_nameText);
        layout.addView(Fathers_name);

        final EditText National_ID = new EditText(context);
        National_ID.setHint("National ID");
        National_ID.setText(National_idText);
        layout.addView(National_ID);

        final EditText date_of_birth = new EditText(context);
        date_of_birth.setHint("Date of Birth");
        date_of_birth.setText(date_of_birthText);
        layout.addView(date_of_birth);

        final EditText Gender = new EditText(context);
        Gender.setHint("Gender");
        Gender.setText(GenderText);
        layout.addView(Gender);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Update student record")
                .setMessage("please fill all the fields")
                .setView(layout)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update(UserID,Name.getText().toString(),Surname.getText().toString(),Fathers_name.getText().toString(),National_ID.getText().toString(),date_of_birth.getText().toString(),Gender.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    private void update(Integer UserID,String Name, String Surname, String Fathers_name, String National_ID, String date_of_birth, String Gender){
        FirebaseDatabase database =  FirebaseDatabase.getInstance();

        DatabaseReference mRef =  database.getReference().child("Student").child(String.valueOf(UserID));
        mRef.child("Name").setValue(Name);
        mRef.child("Surname").setValue(Surname);
        mRef.child("Fathers_name").setValue(Fathers_name);
        mRef.child("National_ID").setValue(National_ID);
        mRef.child("date_of_birth").setValue(date_of_birth);
        mRef.child("Gender").setValue(Gender);
        Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }
}

class AdapterViewHolder extends RecyclerView.ViewHolder{

    TextView txtID,txtName,txtSname,txtFname,txtNID,txtDOB,txtGender;

    LinearLayout stdLinearLayout;

    ImageButton imageButtonEdit,imageButtonDelete;
    public AdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        txtID=itemView.findViewById(R.id.txtID);
        txtName=itemView.findViewById(R.id.txtName);
        txtSname=itemView.findViewById(R.id.txtSname);
        txtFname=itemView.findViewById(R.id.txtFname);
        txtNID=itemView.findViewById(R.id.txtNID);
        txtDOB=itemView.findViewById(R.id.txtDOB);
        txtGender=itemView.findViewById(R.id.txtGender);
        stdLinearLayout=itemView.findViewById(R.id.stdLinearLayout);

        imageButtonDelete=itemView.findViewById(R.id.ibtndelete);
        imageButtonEdit=itemView.findViewById(R.id.ibtnedit);


    }
}