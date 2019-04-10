package com.gpa;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_semster extends AppCompatActivity {



    SQLiteDatabase db;
    DbHelper mDbHelper;
    EditText name;



    Button buttonsave;

;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semster);
        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();
//
        name = (EditText) findViewById(R.id.name);


        buttonsave=findViewById(R.id.buttonsave);






        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                // insert into database
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.NAME, name.getText().toString());





                db.insert(mDbHelper.SEMSTER_TABLE_NAME, null, cv);

                Intent intent = new Intent(add_semster.this, add_subject.class);
                intent.putExtra("semster", name.getText().toString());
                startActivity(intent);






            }
        });




    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }



}
