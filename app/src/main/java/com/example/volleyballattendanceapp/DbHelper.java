package com.example.volleyballattendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;

    //team table
    private static final String TEAM_TABLE_NAME= "TEAM_TABLE";
    public static final String T_ID= "_TID";
    public static final String TEAM_NAME_KEY= "TEAM_NAME";
    public static final String SPORT_NAME_KEY= "SPORT_NAME";

    private static final String CREATE_TEAM_TABLE =
            "CREATE TABLE " + TEAM_TABLE_NAME + "( " +
                    T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    TEAM_NAME_KEY + " TEXT NOT NULL, " +
                    SPORT_NAME_KEY + " TEXT NOT NULL, " +
                    "UNIQUE (" + TEAM_NAME_KEY + "," + SPORT_NAME_KEY + ")" +
                    ");";
    private static final String DROP_TEAM_TABLE = "DROP TABLE IF EXISTS "+TEAM_TABLE_NAME;
    private static final String SELECT_TEAM_TABLE = "SELECT * FROM "+TEAM_TABLE_NAME;

    //student table
    private static final String STUDENT_TABLE_NAME= "STUDENT_TABLE";
    public static final String S_ID= "_SID";
    public static final String STUDENT_NAME_KEY= "STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY= "ROLL";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME +
                    "( " +
                    S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    T_ID + " INTEGER NOT NULL, "+
                    STUDENT_NAME_KEY + " TEXT NOT NULL," +
                    STUDENT_ROLL_KEY + " INTEGER, " +
                    " FOREIGN KEY ( " + T_ID + ") REFERENCES " + TEAM_TABLE_NAME + "("+T_ID+")" +
                    ");";
    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS "+STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM "+STUDENT_TABLE_NAME;


    //status table
    private static final String STATUS_TABLE_NAME= "STATUS_TABLE";
    public static final String STATUS_ID= "_STATUS_ID";
    public static final String DATE_KEY= "STATUS_DATE";
    private static final String STATUS_KEY= "STATUS";

    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME + "(" + STATUS_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    S_ID + " INTEGER NOT NULL, "+
                    T_ID + " INTEGER NOT NULL, "+
                    DATE_KEY + " DATE NOT NULL," +
                    STATUS_KEY + " TEXT NOT NULL, " +
                    " UNIQUE (" + S_ID + ","+DATE_KEY+")," +
                    " FOREIGN KEY (" + S_ID +") REFERENCES " + STATUS_TABLE_NAME + "( "+S_ID+")," +
                    " FOREIGN KEY (" + T_ID +") REFERENCES " + TEAM_TABLE_NAME + "( "+T_ID+")" +
                    ");";
    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS "+STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM "+STATUS_TABLE_NAME;


    public DbHelper(@Nullable Context context) {
        super(context, "Attendance.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEAM_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(DROP_TEAM_TABLE);
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    long addTeam(String teamName,String sportName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME_KEY,teamName);
        values.put(SPORT_NAME_KEY,sportName);

        return database.insert(TEAM_TABLE_NAME,null,values);
    }

    Cursor getTeamTable(){
        SQLiteDatabase database = this.getReadableDatabase();

        return database.rawQuery(SELECT_TEAM_TABLE,null);
    }

    int deleteClass(long tid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(TEAM_TABLE_NAME,T_ID+"=?",new String[]{String.valueOf(tid)});
    }

    long updateTeam(long tid, String teamName,String sportName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME_KEY,teamName);
        values.put(SPORT_NAME_KEY,sportName);

        return database.update(TEAM_TABLE_NAME,values,T_ID+"=?",new String[]{String.valueOf(tid)});
    }

    long addStudent(long tid,int roll, String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T_ID,tid);
        values.put(STUDENT_ROLL_KEY,roll);
        values.put(STUDENT_NAME_KEY,name);

        return database.insert(STUDENT_TABLE_NAME,null,values);
    }
    Cursor getStudentTable(long tid){
        SQLiteDatabase database = this.getReadableDatabase();

        return database.query(STUDENT_TABLE_NAME,null,T_ID+"=?",new String[]{String.valueOf(tid)},null,null,STUDENT_ROLL_KEY);
    }

    int deleteStudent(long sid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(STUDENT_TABLE_NAME,S_ID+"=?",new String[]{String.valueOf(sid)});
    }
    long updateStudent(long sid, String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY,name);
        return database.update(STUDENT_TABLE_NAME,values,S_ID+"=?",new String[]{String.valueOf(sid)});
    }
    long addStatus(long sid,long tid,String date, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_ID,sid);
        values.put(T_ID,tid);
        values.put(DATE_KEY,date);
        values.put(STATUS_KEY,status);

        return database.insert(STATUS_TABLE_NAME,null,values);
    }
    int deleteStatus(StudentItem studentItem,String date){
        ContentValues values;
        SQLiteDatabase database = this.getReadableDatabase();
        values = new ContentValues();
        Log.i("1234567890","clear " +studentItem.getName());
        values.put(STATUS_KEY,studentItem.getStatus());
        String whereClause = DATE_KEY +"='"+date+"' AND "+S_ID+"="+studentItem.getSid();
        return database.delete(STATUS_TABLE_NAME,whereClause,null);

    }
    long updateStatus(long sid,String date, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY,status);
        String whereClause = DATE_KEY +"='"+date+"' AND "+S_ID+"="+sid;
        return database.update(STATUS_TABLE_NAME,values,whereClause,null);
    }
    String getStatus(long sid,String date){
        String status=null;
        SQLiteDatabase database = this.getReadableDatabase();
        String whereClause = DATE_KEY +"='"+date+"' AND "+S_ID+"="+sid;
        Cursor cursor = database.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
        if(cursor.moveToFirst())
            status = cursor.getString(cursor.getColumnIndex(STATUS_KEY));
        return status;
    }
    Cursor getDistinctMonths(long tid){
        SQLiteDatabase database = this.getReadableDatabase();

        return database.query(STATUS_TABLE_NAME,new String[]{DATE_KEY},T_ID+"="+tid,null,"substr("+DATE_KEY+",4,7)",null,null);//01.04.2020
    }

}
