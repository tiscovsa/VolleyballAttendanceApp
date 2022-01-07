package com.example.volleyballattendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    //team table
    private static final String TEAM_TABLE_NAME= "TEAM_TABLE";
    public static final String T_ID= "_TID";
    public static final String TEAM_NAME_KEY= "TEAM_NAME";
    public static final String SPORT_NAME_KEY= "SPORT_NAME";

    private static final String CREATE_TEAM_TABLE =
            "CREATE TABLE " + TEAM_TABLE_NAME + "( " +
                    T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    TEAM_NAME_KEY + " TEXT NOT NULL, " +
                    SPORT_NAME_KEY + " TEXT NOT NULL, " +
                    "UNIQUE (" + TEAM_NAME_KEY + "," + SPORT_NAME_KEY + ")" +
                    ");";
    private static final String DROP_TEAM_TABLE = "DROP TABLE IF EXISTS "+TEAM_TABLE_NAME;
    private static final String SELECT_TEAM_TABLE = "SELECT * FROM "+TEAM_TABLE_NAME;

    //student table
    private static final String STUDENT_TABLE_NAME= "STUDENT_TABLE";
    private static final String S_ID= "_SID";
    private static final String STUDENT_NAME_KEY= "STUDENT NAME";
    private static final String STUDENT_ROLL_KEY= "ROLL";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME + "(" + S_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    T_ID + " INTEGER NOT NULL, "+
                    STUDENT_NAME_KEY + " ,TEXT NOT NULL," +
                    STUDENT_ROLL_KEY + " INTEGER, " +
                    " FOREIGN KEY ( " + T_ID + ") REFERENCES " + TEAM_TABLE_NAME + "("+T_ID+")" +
                    ");";
    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS "+STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM "+STUDENT_TABLE_NAME;


    //status table
    private static final String STATUS_TABLE_NAME= "STATUS_TABLE";
    private static final String STATUS_ID= "_STATUS_ID";
    private static final String DATE_KEY= "STATUS_DATE";
    private static final String STATUS_KEY= "STATUS";

    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME + "(" + STATUS_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    S_ID + " INTEGER NOT NULL, "+
                    DATE_KEY + " ,DATE NOT NULL," +
                    STATUS_KEY + "TEXT NOT NULL, " +
                    " UNIQUE (" + S_ID + ","+DATE_KEY+")," +
                    " FOREIGN KEY (" + S_ID +") REFERENCES " + STATUS_TABLE_NAME + "( "+S_ID+")" +
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
}