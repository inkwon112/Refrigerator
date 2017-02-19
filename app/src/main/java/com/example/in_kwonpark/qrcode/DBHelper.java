package com.example.in_kwonpark.qrcode;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBName = "CPS.db"; // CPS라는 이름의 db 생성
    private static final int DBVer = 2; // db 버전

    public DBHelper(Context context){
        super(context, DBName, null, DBVer);
    }
    @Override
    public void onCreate(SQLiteDatabase db) { // 데이터베이스가 처음 생성 될 때 호출됨
        // item이라는 테이블이 생성되고
        // item 테이블 기본키로 _id 컬럼이 사용되며, _id 컬럼은 정수형의 숫자 값이 자동으로 증가되는 컬럼이다.
        db.execSQL("CREATE TABLE item ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name char(100), " + "start char(100), finish char(100));");

        // 데이터를 추가 할 때,
        // ContentValues 인스턴스 값이 하나도 없는 경우 행이 생성되지 않기 때문에 이런경우 null 처리해야 행이 생성되게 함
        db.execSQL("insert into item values(null, '우유', 01 , 02);");
    }
    @Override // DB를 업그레이드해야 할 때 호출 됨.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ITEM");
        onCreate(db);
    }
}
