package com.example.dana;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.List;

public class Adapter2 extends RecyclerView.Adapter<Adapter2ViewHolder>{

    Context context;
    private List<Model> modelList;
    Part5 part5;
    DBHelper dbHelper;
    private static final String TABLE = "student";
    SQLiteDatabase sqLiteDatabase;

    public Adapter2(Context context, List<Model> modelList, Part5 part5) {
        this.context = context;
        this.modelList = modelList;
        this.part5 = part5;

        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public Adapter2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_cell,parent,false);
        return new Adapter2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter2ViewHolder holder, int position) {
        final Model model = modelList.get(position);

        holder.txtID.setText(String.valueOf(model.getID()));
        holder.txtName.setText(model.getName());
        holder.txtSname.setText(model.getSurname());
        holder.txtFname.setText(model.getFathers_name());
        holder.txtNID.setText(model.getNational_ID());
        holder.txtDOB.setText(model.getDate_of_birth());
        holder.txtGender.setText(model.getGender());



     holder.stdLinearLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(context, model.getName()+" "+model.getSurname(), Toast.LENGTH_SHORT).show();
        }
    });

        holder.stdLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                alertDialog.setTitle("Message");

                alertDialog.setMessage("Do you want to update or delete?");

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        db.delete(TABLE, "ID=?", new String[]{String.valueOf(model.getID())});
                        db.close();
                        int removeIndex = position;
                        modelList.remove(removeIndex);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        showUpdateDialog(model.getID(), model.getName(), model.getSurname(), model.getFathers_name(), model.getNational_ID(), model.getDate_of_birth(), model.getGender());


                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        //...

                    }
                });


                alertDialog.show();



            return false;
        }

    });
}

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    private void showUpdateDialog(Integer UserID,String taskNameText, String SurnameText, String Fathers_nameText, String National_idText, String date_of_birthText, String GenderText) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText taskName = new EditText(context);
        taskName.setHint("Name");
        taskName.setText(taskNameText);
        layout.addView(taskName);

        final EditText taskSurname = new EditText(context);
        taskSurname.setHint("Surname");
        taskSurname.setText(SurnameText);
        layout.addView(taskSurname);

        final EditText taskFathers_name = new EditText(context);
        taskFathers_name.setHint("Father's Name");
        taskFathers_name.setText(Fathers_nameText);
        layout.addView(taskFathers_name);

        final EditText taskNational_ID = new EditText(context);
        taskNational_ID.setHint("National ID");
        taskNational_ID.setText(National_idText);
        layout.addView(taskNational_ID);

        final EditText taskdate_of_birth = new EditText(context);
        taskdate_of_birth.setHint("Date of Birth");
        taskdate_of_birth.setText(date_of_birthText);
        layout.addView(taskdate_of_birth);

        final EditText taskGender = new EditText(context);
        taskGender.setHint("Gender");
        taskGender.setText(GenderText);
        layout.addView(taskGender);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Update Record")
                .setMessage("Please enter the information to update student record")
                .setView(layout)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update(UserID,taskName.getText().toString(),taskSurname.getText().toString(),taskFathers_name.getText().toString(),taskNational_ID.getText().toString(),taskdate_of_birth.getText().toString(),taskGender.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void update(Integer UserID,String taskNameText, String SurnameText, String Fathers_nameText, String National_idText, String date_of_birthText, String GenderText){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        ContentValues contentValues = new ContentValues();

        contentValues.put("Name", taskNameText);
        contentValues.put("Surname", SurnameText);
        contentValues.put("Fathers_name", Fathers_nameText);
        contentValues.put("National_ID", National_idText);
        contentValues.put("date_of_birth", date_of_birthText);
        contentValues.put("Gender", GenderText);

        db.update(TABLE, contentValues, "ID=?", new String[]{String.valueOf(UserID)});
        db.close();

        part5.displayData();
        notifyDataSetChanged();
        Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();

    }

}

class Adapter2ViewHolder extends RecyclerView.ViewHolder{

    TextView txtID,txtName,txtSname,txtFname,txtNID,txtDOB,txtGender;

    LinearLayout stdLinearLayout;

    ImageButton imageButtonEdit,imageButtonDelete;
    public Adapter2ViewHolder(@NonNull View itemView) {
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