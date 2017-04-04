package com.example.in_kwonpark.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.example.in_kwonpark.qrcode.R.id.itemlist_btn;

//test
public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    private Button itemlist_btn;
    private Button shopping_btn;
    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c; // Cursor c 생성.(커서는 기본적으로 행(Row) 값을 참조 함)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        itemlist_btn = (Button) findViewById(R.id.itemlist_btn);
        shopping_btn = (Button) findViewById(R.id.shopping_btn);
        final Activity activity = this;


        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성 -> DBHelper.java에 있는 onCreate() 자동 실행
        try {
            db = helper.getWritableDatabase(); // DB를 Write&Read 전용으로 연다.
            Toast.makeText(this,"Hello DB",Toast.LENGTH_SHORT).show(); // DB 연결 됨을 알림
        } catch (SQLiteException e) { // 데이터베이스를 열 수 없는 경우
            db = helper.getReadableDatabase(); // Read전용으로 오픈
        }

        c = db.rawQuery("SELECT * FROM ITEM", null); // item 테이블의 배열을 커서 c로 넘김

        scan_btn.setOnClickListener(new View.OnClickListener() { // scan버튼 클릭 시(QR 코드)
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(1);

                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });


        itemlist_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReaderActivity.this, ItemListActivity.class);
                startActivity(intent);
            }
        });

        shopping_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://emart.ssg.com/")));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                String Data = result.getContents(); // QR코드로 부터 받은 컨텐츠를 Data 변수에 저장

                String[] ItemValues = Data.split("/"); // "/"단위로 ItemValues 배열에 순차적으로 저장

                String Name = ItemValues[0]; // 상품명을 가리키는 Name
                String Start = ItemValues[1]; // 제조일자를 가리키는 Start
                String Finish = ItemValues[2]; // 유통기한을 가리키는 Finish

                db.execSQL("insert into item values(null, '"+Name+"','"+Start+"','"+ Finish+"' );");

                while(c.moveToNext()){ // item 테이블에 있는 항목을 모두 가져와 결과 값을 뽑아내는 while 문
                    Log.d("tag", "Table Search(index : "+c.getString(0)+" name : "+c.getString(1)+" start : "+c.getString(2)+" finish : "+c.getString(3)+")");

                }
                Log.d("tag", result.getContents());
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        c.close();
    }
}