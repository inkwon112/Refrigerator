package com.example.in_kwonpark.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;

    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;

        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
            Toast.makeText(this,"Hello DB",Toast.LENGTH_SHORT).show(); // DB 연결 됨을 알림
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        c = db.rawQuery("SELECT * FROM ITEM", null);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
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

                String Data = result.getContents();

                String[] ItemValues = Data.split("/");

                String Name = ItemValues[0];
                String Start = ItemValues[1];
                String Finish = ItemValues[2];


                db.execSQL("insert into item values(null, '"+Name+"','"+Start+"','"+ Finish+"' );");
                while(c.moveToNext()){
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