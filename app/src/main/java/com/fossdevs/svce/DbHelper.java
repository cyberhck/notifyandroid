package com.fossdevs.svce;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbHelper extends SQLiteOpenHelper{
    public static String DB_NAME=GlobalObject.AppName;
    public static int DB_VERSION=1;
    public static String createSql="CREATE TABLE device_id (device_id TEXT);";
    public static String noticeTable="CREATE TABLE notices(senderName VARCHAR(200),sentBy VARCHAR(20),message TEXT,sentAt VARCHAR(20),type VARCHAR(5) DEFAULT 'text',downloaded TEXT DEFAULT 'false');";
    public static String tempListSubs="CREATE TABLE listViewUser(userId INT, name VARCHAR(200), username VARCHAR(200))";
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(createSql);
        db.execSQL(noticeTable);
        db.execSQL(tempListSubs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newDb) {
        // TODO Auto-generated method stub
        if(old==1 && newDb==2){
            db.execSQL("ALTER TABLE notices ADD COLUMN downloaded TEXT DEFAULT 'false'");
            Log.d("database","upgraded!");
        }
    }

}