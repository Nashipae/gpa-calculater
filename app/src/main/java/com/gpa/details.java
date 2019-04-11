package com.gpa;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class details extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper dbHelper;
    EditText name_edittext, phone_edittext;

    TextView name_textview,showupdate;

    Button update,call;

    LinearLayout updatelayout;

    ListView list;


    String phone="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();


        list = (ListView)findViewById(R.id.subject_list);

        name_edittext = findViewById(R.id.name);


        update = findViewById(R.id.update);


        showupdate = findViewById(R.id.show_update);
        name_textview = findViewById(R.id.name_textview);


        updatelayout = findViewById(R.id.updatelayout);



        Bundle extras = getIntent().getExtras();
        final String name = extras.getString("name");
        final String semster = extras.getString("semster_name");





        String selection ="semster_name = ?" ;

        // selection arguments
        String[] selectionArgs = {name};


        String[] from = {DbHelper.NAME,  DbHelper.degree,DbHelper.hours};
        final String[] column = {DbHelper.ID, DbHelper.NAME,DbHelper.degree,DbHelper.hours};
        int[] to = {R.id.name, R.id.grade_textview,R.id.hours_textview};

        final Cursor cursor = db.query(DbHelper.SUBJECT_TABLE_NAME, column, selection, selectionArgs ,null, null, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_subject, cursor, from, to, 0);


        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.grade_textview) {


                    int getIndex = cursor.getColumnIndex("degree");
                    int grade = cursor.getInt(getIndex);
                    String degree_in_lette="";

                    switch (grade) {
                        case 100:
                            degree_in_lette = "A+";
                            break;
                        case 95:
                            degree_in_lette ="A" ;
                            break;
                        case 90 :
                        degree_in_lette = "B+";
                            break;
                        case 85:
                            degree_in_lette = "B";
                            break;
                        case 80:
                            degree_in_lette = "C+";
                            break;
                        case 75:
                            degree_in_lette = "C";
                            break;
                        case 70:
                            degree_in_lette= "D+";
                            break;
                        case 65:
                            degree_in_lette = "D";
                            break;
                        case 60:
                            degree_in_lette = "F";
                            break;
                    }




                    TextView GRADETextView = (TextView) view;

                    GRADETextView.setText(degree_in_lette);
                    return true;
                }
                return false;
            }
        });
        list.setAdapter(adapter);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View view, int position,
                                    long id){
                Cursor cursor = ((SimpleCursorAdapter)list.getAdapter()).getCursor();
                cursor.moveToPosition(position);
                String subject_name = cursor.getString(cursor.getColumnIndex("name"));

                Intent startDayActivity = new Intent(details.this, edit_subject.class);
                startDayActivity.putExtra("name", subject_name);
               startDayActivity.putExtra("semster_name", name);
                startActivity(startDayActivity);
            }

        });
















        this.setTitle(name);
                name_textview.setText(name);

                name_edittext.setText(name);








          update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //UPDATE name in semster table
                ContentValues cv = new ContentValues();



                cv.put(DbHelper.NAME, name_edittext.getText().toString());



                db.update(dbHelper.SEMSTER_TABLE_NAME, cv, "name='"+name+"'", null);



                //UPDATE name in subject table
                ContentValues cv2 = new ContentValues();



                cv2.put(DbHelper.semster, name_edittext.getText().toString());





                db.update(dbHelper.SUBJECT_TABLE_NAME, cv2, "semster_name='"+name+"'", null);

                Toast.makeText(details.this, "updated ", Toast.LENGTH_SHORT).show();



            }
        });




        showupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                updatelayout.setVisibility(View.VISIBLE);




            }
        });
    }



    private  void callphone(String number){

        if (ContextCompat.checkSelfPermission(details.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(details.this, new String[]{Manifest.permission.CALL_PHONE},99);



        }
        else
        {



            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +number));// Initiates the Intent
            startActivity(intent);
        }


    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }



}
