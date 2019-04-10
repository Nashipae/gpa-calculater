package com.gpa;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class add_subject extends AppCompatActivity {



    SQLiteDatabase db;
    DbHelper mDbHelper;
    EditText name,hours;



    Button buttonsave,buttongpa;

    Spinner grade_spinner;


    String semster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();

        name = (EditText) findViewById(R.id.name);
        hours = (EditText) findViewById(R.id.hours);

        buttonsave=findViewById(R.id.buttonsave);

        buttongpa=findViewById(R.id.buttongpa);
        grade_spinner=findViewById(R.id.grade_spinner);



        ArrayList<String> grades = new ArrayList<>();

        grades.add("A+");
        grades.add("A");
        grades.add("B+");
        grades.add("B");
        grades.add("C+");
        grades.add("C");
        grades.add("D+");
        grades.add("D");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(add_subject.this, android.R.layout.simple_spinner_dropdown_item, grades);

        grade_spinner.setAdapter(adapterSpinner);





         Bundle extras = getIntent().getExtras();
         semster = extras.getString("semster");






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
                cv.put(mDbHelper.semster, semster);
                cv.put(mDbHelper.hours, Integer.parseInt(hours.getText().toString()));
                cv.put(mDbHelper.degree,  grade);





                db.insert(mDbHelper.SUBJECT_TABLE_NAME, null, cv);


                Toast.makeText(add_subject.this, "Saved Successfully", Toast.LENGTH_SHORT).show();



                name.setText("");
                hours.setText("");



            }
        });




         buttongpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



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




                        Intent intent = new Intent(add_subject.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }
                }





                }







        });



    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }



}
