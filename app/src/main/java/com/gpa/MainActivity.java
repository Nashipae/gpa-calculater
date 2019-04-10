package com.gpa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper mDbHelper;
    ListView list;
    TextView Cumulative_GPA;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));


        Cumulative_GPA = findViewById(R.id.Cumulative_GPA);
        list = (ListView)findViewById(R.id.commentlist);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCreateNote = new Intent(MainActivity.this, add_semster.class);
                startActivity(openCreateNote);
            }
        });





        mDbHelper = new DbHelper(this);
        db= mDbHelper.getWritableDatabase();


        cAL_Cumulative_GPA();

        String[] from = {mDbHelper.NAME,  mDbHelper.gpa};
        final String[] column = {mDbHelper.ID, mDbHelper.NAME,mDbHelper.gpa};
        int[] to = {R.id.name, R.id.gpa_textview};

        final Cursor cursor = db.query(mDbHelper.SEMSTER_TABLE_NAME, column, null, null ,null, null, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_entry, cursor, from, to, 0);

        list.setAdapter(adapter);






        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, final long id) {


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if(delete(String.valueOf(id)))
                                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to delete this?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            }
        });

    }


    public boolean delete(String id)
    {
        return db.delete(mDbHelper.SEMSTER_TABLE_NAME, "_id" + "=" + id, null) > 0;
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.logout:
                logout();
               return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void cAL_Cumulative_GPA(){






        String qu = "SELECT  * FROM semster";


        Cursor cursor = db.rawQuery(qu,null);

        if ( cursor != null && cursor.moveToFirst()) {



            cursor.moveToFirst();
            float sum=0;
            int semster_num=0;
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {


                    sum+=   cursor.getFloat(cursor.getColumnIndex(mDbHelper.gpa));

                    semster_num++;


                }
            } finally {

            float gpa= sum/semster_num;
                Cumulative_GPA.setVisibility(View.VISIBLE);

                Cumulative_GPA.setText("Cumulative GPA: "+gpa+"");






            cursor.close();






            }

    }


    }

    private void logout() {
        SharedPrefManager.getInstance(MainActivity.this).clear();
        Intent intent = new Intent(MainActivity.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
