package com.gpa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {



    public static final String DATABASE_NAME = "gpa";


    // visitor table

    public static final String visitor_TABLE_NAME = "visitor";
    public static final String visitor_id = "_id";
    public static final String visitor_name = "name";
    public static final String visitor_phone = "phone";



    // semster table

    public static final String SEMSTER_TABLE_NAME = "semster";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String gpa = "gpa";


    // subjects table

    public static final String SUBJECT_TABLE_NAME = "subject";
    public static final String id = "_id";
    public static final String name = "name";
    public static final String degree = "degree";
    public static final String hours = "hours";
    public static final String semster = "semster_name";




    public static final int VERSION = 1;



    //users table


    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";


    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";



    // create table sql query
    private String CREATE_VISITOR_TABLE = "CREATE TABLE " + visitor_TABLE_NAME + "("
            + visitor_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + visitor_name + " TEXT,"
            + visitor_phone + " TEXT)";




    private final String CREATE_SEMSTER = "create table  " + SEMSTER_TABLE_NAME + " ( "
    + ID + " integer primary key autoincrement, "
    + NAME + " text, "
      + gpa + " FLOAT )";



    private final String CREATE_SUBJECT = "create table  " + SUBJECT_TABLE_NAME + " ( "
            + id + " integer primary key autoincrement, "
            + name + " text, "
            + semster + " text, "
            + hours + " integer, "
            + degree + " integer)";


    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {




            db.execSQL(CREATE_SEMSTER);
            db.execSQL(CREATE_SUBJECT);
            db.execSQL(CREATE_VISITOR_TABLE);




            db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void addUser(String username, String email,String password ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, username);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    public boolean Userlogin(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

}
