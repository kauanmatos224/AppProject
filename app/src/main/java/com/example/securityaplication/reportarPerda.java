package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class reportarPerda extends AppCompatActivity {

    SQLiteDatabase database;
    Cursor cursor;
    int idRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_perda);
        Intent intent = getIntent();
        idRecords = (int)intent.getSerializableExtra("IdReg");
        database = openOrCreateDatabase("database_sm", Context.MODE_PRIVATE, null);
        cursor = database.rawQuery("select * from tb_mats where (_id="+idRecords+")", null);
        if(!cursor.moveToFirst()){
            Toast.makeText(getBaseContext(), "Houve algum erro, tente novamente mais tarde", Toast.LENGTH_LONG).show();
            Intent intentAc = new Intent(reportarPerda.this, MainActivity.class);
            startActivity(intent);
            database.close();
            finish();
        }
    }
    public void CriaPdf(View view){

        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String nome_item = cursor.getString(1);
        int dirCreationState = 0;
        String descri_item = cursor.getString(4);
        String dirDocs = "/security_material/docs";
        File rootPath = new File(Environment.getExternalStorageDirectory() + dirDocs);
        //File rootPath = new File(Environment.getExternalStorageDirectory() + dirDocs+"/RelatoPerda_"+nome_item+id+".pdf");
        File absoluteFileName = new File(rootPath.getPath()+"/RelatoPerda_"+nome_item+id+".pdf");
        if(!rootPath.exists()){
            rootPath.mkdirs();
            dirCreationState = 1;
        }
        if(!rootPath.exists()){
            dirDocs = "/Documents";
            rootPath.mkdirs();
            dirCreationState = 2;
        }
        if(!rootPath.exists()){
            dirDocs="";
            rootPath.mkdirs();
            dirCreationState = 3;
        }
        if(!rootPath.exists()){
            dirCreationState = 4;
        }
        Log.i("estado de criação pasta", String.valueOf(dirCreationState));
        String pathImg = cursor.getString(2);
        Bitmap scaledBitmap, bmpImg;
        bmpImg = BitmapFactory.decodeFile(pathImg);
        String docPath = absoluteFileName.getPath();

        scaledBitmap = Bitmap.createScaledBitmap(bmpImg, 150, 200, false );
        String titulo = "RELATO DE PERDA DE MATERIAL("+nome_item+")";
        String linha = descri_item;
        String linha2="Foi perdido, se alguém o encontra-lo, entre em contato para a devolução";
        String linha3="desse material o mais rápido possível.";

        PdfDocument documentoPdf = new PdfDocument();
        PdfDocument.PageInfo detalhesDaPagina =
                new PdfDocument.PageInfo.Builder(500, 600, 1).create();

        PdfDocument.Page novaPagina;
        novaPagina = documentoPdf.startPage(detalhesDaPagina);
        Canvas canvas = novaPagina.getCanvas();

        Paint corDoTexto = new Paint();
        corDoTexto.setColor(Color.BLACK);

        canvas.drawText(titulo, 125, 15, corDoTexto);
        Paint myPaint = new Paint();
        canvas.drawBitmap(scaledBitmap, 180, 30, myPaint);
        if(!descri_item.equals("")&&!descri_item.equals(null)) {
            canvas.drawText("Esse material com as seguintes características:",10,260, corDoTexto);
            canvas.drawText(linha, 10, 275, corDoTexto);
        }

        canvas.drawText(linha2, 10, 320, corDoTexto);
        canvas.drawText(linha3, 10, 335, corDoTexto);

        documentoPdf.finishPage(novaPagina);
        File filePath = new File(docPath);
        StringBuilder sqlCommand = new StringBuilder();
        try{
            sqlCommand.append("update tb_mats" +
                    " set `status`='Perdido'" +
                    " where _id="+idRecords+";");

        }catch (Exception ex){
        }
        try{
            documentoPdf.writeTo(new FileOutputStream(filePath));
        }catch (Exception error){
        }

    ShareFile(absoluteFileName.getAbsolutePath());

    }
    private void ShareFile(String absoluteFileName){
        final Uri arquivo = Uri.fromFile(new File(absoluteFileName));
        final Intent _intent = new Intent();
        try {
            _intent.setAction(Intent.ACTION_SEND);
            _intent.putExtra(Intent.EXTRA_STREAM, arquivo);
            _intent.putExtra(Intent.EXTRA_TEXT, "Venho informar a perda de um material com o seguinte anexo: ");
            _intent.putExtra(Intent.EXTRA_TITLE, "Meu pdf");
            _intent.setType("application/pdf");

            startActivity(Intent.createChooser(_intent, "Compartilhar"));
        }catch (Exception error){
            Toast.makeText(getBaseContext(),"Ocorreu um erro ao criar o seu relatório", Toast.LENGTH_LONG).show();
        }
    }
    public void btnHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}