package com.example.in_kwonpark.qrcode;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ItemListActivity extends AppCompatActivity {
    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c; // Cursor c 생성.(커서는 기본적으로 행(Row) 값을 참조 함)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성 -> DBHelper.java에 있는 onCreate() 자동 실행
        try {
            db = helper.getWritableDatabase(); // DB를 Write&Read 전용으로 연다.
            Toast.makeText(this,"Hello DB",Toast.LENGTH_SHORT).show(); // DB 연결 됨을 알림
        } catch (SQLiteException e) { // 데이터베이스를 열 수 없는 경우
            db = helper.getReadableDatabase(); // Read전용으로 오픈
        }

        // ITEM 테이블에서 모든 레코드를 retrieve
        c = db.rawQuery("SELECT * FROM ITEM", null);
        c.moveToFirst();

        if(c != null) {
            String[] columns = {"name", "start", "finish"};
            int[] resIds = {R.id.text01, R.id.text02, R.id.text03};
            // SimpleCursorAdapter 객체 생성
            // 리턴된 커서의 컬럼과 listitem.xml에서 준비된 리소스아이디와 연결(매칭)
            SimpleCursorAdapter adapter
                    = new SimpleCursorAdapter(getApplicationContext(), R.layout.listitem, c, columns, resIds);

            // 레이아웃에 정의된 리스트뷰에 대한 참조 객체 얻음
            ListView list = (ListView)findViewById(R.id.list01);
            list.setAdapter(adapter); // 리스트에 아답터 부착
        }
    }
}
