package com.gpa;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class edit_subject extends AppCompatActivity {



    SQLiteDatabase db;
    DbHelper mDbHelper;
    EditText name,hours;



    Button buttonsave;

    Spinner grade_spinner;


    String semster;

    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();

        name = (EditText) findViewById(R.id.name);
        hours = (EditText) findViewById(R.id.hours);

        buttonsave=findViewById(R.id.buttonsave);

        grade_spinner=findViewById(R.id.grade_spinner);






        Bundle extras = getIntent().getExtras();
         subject = extras.getString("name");
         semster = extras.getString("semster_name");



        Cursor cursor = db.rawQuery("SELECT * FROM  subject   where name='"+subject+"'" , null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                this.setTitle(cursor.getString(cursor.getColumnIndex(mDbHelper.name)));
//                name_textview.setText(cursor.getString(cursor.getColumnIndex(dbHelper.NAME)));
//
                name.setText(cursor.getString(cursor.getColumnIndex(mDbHelper.name)));
                hours.setText(cursor.getString(cursor.getColumnIndex(mDbHelper.hours)));
//
//                phone_edittext.setText(cursor.getString(cursor.getColumnIndex(dbHelper.PHONE_NUMBER)));
//
//                phone=cursor.getString(cursor.getColumnIndex(dbHelper.PHONE_NUMBER));
//


            }
            cursor.close();

        }





        ArrayList<String> grades = new ArrayList<>();

        grades.add("A+");
        grades.add("A");
        grades.add("B+");
        grades.add("B");
        grades.add("C+");
        grades.add("C");
        grades.add("D+");
        grades.add("D");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(edit_subject.this, android.R.layout.simple_spinner_dropdown_item, grades);

        grade_spinner.setAdapter(adapterSpinner);











        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {









                int grade = 0;
                switch (grade_spinner.getSelectedItem().toString()) {
                    case "A+":
                        grade = 100;
                        break;
                    case "A":
                        grade = 95;
                        break;
                    case "B+":
                        grade = 90;
                        break;
                    case "B":
                        grade = 85;
                        break;
                    case "C+":
                        grade = 80;
                        break;
                    case "C":
                        grade = 75;
                        break;
                    case "D+":
                        grade = 70;
                        break;
                    case "D":
                        grade = 65;
                        break;
                    case "F":
                        grade = 60;
                        break;
                }






                // insert into database
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.name, name.getText().toString());

                cv.put(mDbHelper.hours, Integer.parseInt(hours.getText().toString()));
                cv.put(mDbHelper.degree,  grade);





                db.update(mDbHelper.SUBJECT_TABLE_NAME, cv, "name='"+subject+"'", null);




                updategpa();

                Intent intent = new Intent(edit_subject.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);



                //db.insert(mDbHelper.SUBJECT_TABLE_NAME, null, cv);


                Toast.makeText(edit_subject.this, "Saved Successfully", Toast.LENGTH_SHORT).show();



                name.setText("");
                hours.setText("");



            }
        });

















    }


    public void updategpa() {






        Cursor cursor = db.rawQuery("SELECT * FROM  subject   where semster_name='"+semster+"'" , null);


        if ( cursor != null && cursor.moveToFirst()) {



            cursor.moveToFirst();
            float sum=0;
            int hours=0;
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {


                    sum+=   cursor.getInt(cursor.getColumnIndex(mDbHelper.hours))*cursor.getInt(cursor.getColumnIndex(mDbHelper.degree));

                    hours+=   cursor.getInt(cursor.getColumnIndex(mDbHelper.hours));



                }
            } finally {




                float gpa =sum/hours/20;


                ContentValues cv = new ContentValues();



                cv.put(DbHelper.gpa,  gpa);




                db.update(mDbHelper.SEMSTER_TABLE_NAME, cv, "name='"+semster+"'", null);





                cursor.close();






            }
        }





    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }



}
