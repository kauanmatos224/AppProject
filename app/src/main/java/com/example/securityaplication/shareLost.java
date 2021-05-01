package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class shareLost extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor cursor;
    public int id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lost);
        db = openOrCreateDatabase("database_sm", Context.MODE_PRIVATE, null);
        cursor = db.rawQuery("select * from tb_mats " +
                "where _id="+id+";", null);
    }
    public void CriaPdf(View view){

        int dirCretionState = 0;
        String dirDocs = "/security_material/docs";
        File rootPath = new File(Environment.getExternalStorageDirectory() + dirDocs);
        if(!rootPath.exists()){
            rootPath.mkdirs();
            dirCretionState = 1;
        }
        if(!rootPath.exists()){
            dirDocs = "/Documents";
            rootPath.mkdirs();
            dirCretionState = 2;
        }
        if(!rootPath.exists()){
            dirDocs="";
            rootPath.mkdirs();
            dirCretionState = 3;
        }
        if(!rootPath.exists()){
            dirCretionState = 4;
        }
        Log.i("stado de criação pasta", String.valueOf(dirCretionState));
        String pathImg = cursor.getString(2);
        Bitmap scaledBitmap, bmpImg;
        bmpImg = BitmapFactory.decodeFile(pathImg);
        String docPath = rootPath.getPath();

        scaledBitmap = Bitmap.createScaledBitmap(bmpImg, 200, 300, false );
        String titulo = "Relato de Perda de Material";
        PdfDocument documentoPdf = new PdfDocument();
        PdfDocument.PageInfo detalhesDaPagina =
                new PdfDocument.PageInfo.Builder(500, 600, 1).create();

        PdfDocument.Page novaPagina;
        novaPagina = documentoPdf.startPage(detalhesDaPagina);
        Canvas canvas = novaPagina.getCanvas();

        Paint corDoTexto = new Paint();
        corDoTexto.setColor(Color.BLACK);

        canvas.drawText(titulo, 105, 1, corDoTexto);

        Paint myPaint = new Paint();
        canvas.drawBitmap(bmpImg, 1, 2, myPaint);

        documentoPdf.finishPage(novaPagina);
        File filePath = new File(docPath);
        try{
            documentoPdf.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getBaseContext(), "PDF gerado", Toast.LENGTH_LONG).show();

        }catch (Exception error){
            Toast.makeText(getBaseContext(), "PDF não gerado: "+error, Toast.LENGTH_LONG).show();
        }


    }
}
