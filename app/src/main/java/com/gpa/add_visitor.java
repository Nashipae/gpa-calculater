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

public class add_visitor extends AppCompatActivity {



    SQLiteDatabase db;
    DbHelper mDbHelper;
    EditText name,phone;



    Button buttonsave;

    Spinner grade_spinner;


    String semster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);
        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);

        buttonsave=findViewById(R.id.buttonsave);











        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {













                // insert into database
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.visitor_name, name.getText().toString());
                cv.put(mDbHelper.visitor_phone,  phone.getText().toString());






                db.insert(mDbHelper.visitor_TABLE_NAME, null, cv);


                Toast.makeText(add_visitor.this, "Saved Successfully", Toast.LENGTH_SHORT).show();







            }
        });







    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }



}
