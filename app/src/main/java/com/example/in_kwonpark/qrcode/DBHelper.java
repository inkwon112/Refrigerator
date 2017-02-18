package com.example.in_kwonpark.qrcode;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBName = "CPS.db";
    private static final int DBVer = 2;

    public DBHelper(Context context){
        super(context, DBName, null, DBVer);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE item ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name char(100), " + "start char(100), finish char(100));");

       db.execSQL("insert into item values(null, '우유', 01 , 02);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ITEM");
        onCreate(db);
    }
}
