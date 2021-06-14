package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    PdfDocument documentoPdf;
    int CODE_PERMISSION = 8;
    boolean dirCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_perda);
        Intent intent = getIntent();
        idRecords = (int)intent.getSerializableExtra("IdReg");
        database = openOrCreateDatabase("database_sm", Context.MODE_PRIVATE, null);
        cursor = database.rawQuery("select * from tb_mats where (_id="+idRecords+")", null);
        if(!cursor.moveToFirst()){
            //Toast.makeText(getBaseContext(), "Houve algum erro, tente novamente mais tarde", Toast.LENGTH_LONG).show();
            Intent intentAc = new Intent(reportarPerda.this, MainActivity.class);
            startActivity(intent);
            database.close();
            finish();
        }
    }
    public void CriaPdf(View view){
        checarPermissao();
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String nome_item = cursor.getString(1);
        int dirCreationState = 0;
        String descri_item = cursor.getString(4);
        String dirDocs = "/security_material/docs";
        File rootPath = new File(Environment.getExternalStorageDirectory() + dirDocs);
        //File rootPath = new File(Environment.getExternalStorageDirectory() + dirDocs+"/RelatoPerda_"+nome_item+id+".pdf");
        File absoluteFileName = new File(rootPath.getPath()+"/RelatoPerda_"+nome_item+id+".pdf");
        /*if(!rootPath.exists()){
            rootPath.mkdir();
            dirCreationState = 1;
        }
        if(!rootPath.exists()){
            dirDocs = "/Documents";
            rootPath.mkdir();
            dirCreationState = 2;
        }
        if(!rootPath.exists()){
           // dirDocs="";
            rootPath = new File(Environment.getStorageDirectory()+dirDocs);
            rootPath.mkdir();
            dirCreationState = 3;
        }*/
        if(!rootPath.exists()){
            rootPath = getFilesDir();
            rootPath.mkdir();
            dirCreationState = 4;
        }
        /*if(!rootPath.exists()){
            dirCreationState = 5;
            absoluteFileName = new File(rootPath.getPath()+"/RelatoPerda_"+nome_item+id+".pdf");
            rootPath.mkdirs();
        }*/
        absoluteFileName = new File(rootPath.getPath()+"/RelatoPerda_"+nome_item+id+".pdf");
        if(rootPath.exists()){
            dirCreated = true;
            Log.i("Status do Diretório:",String.valueOf(dirCreated));
            Log.i("PATH ARQUIVO: ", absoluteFileName.getPath());
        }

        Log.i("Estado de criação Pasta", String.valueOf(dirCreationState));
        String pathImg = cursor.getString(2);
        Bitmap scaledBitmap, bmpImg;
        bmpImg = BitmapFactory.decodeFile(pathImg);
        String docPath = absoluteFileName.getPath();

        try{
        scaledBitmap = Bitmap.createScaledBitmap(bmpImg, 150, 200, false );
        }catch (Exception ex){scaledBitmap=null;}

        String titulo = "RELATO DE PERDA DE MATERIAL("+nome_item+")";
        String linha = descri_item;
        String linha2="Foi perdido, se alguém o encontra-lo, entre em contato para a devolução";
        String linha3="desse material o mais rápido possível.";

        documentoPdf = new PdfDocument();
        PdfDocument.PageInfo detalhesDaPagina =
                new PdfDocument.PageInfo.Builder(500, 600, 1).create();

        PdfDocument.Page novaPagina;
        novaPagina = documentoPdf.startPage(detalhesDaPagina);
        Canvas canvas = novaPagina.getCanvas();

        Paint corDoTexto = new Paint();
        corDoTexto.setColor(Color.BLACK);

        canvas.drawText(titulo, 125, 15, corDoTexto);
        Paint myPaint = new Paint();
        if(scaledBitmap!=null) {
            canvas.drawBitmap(scaledBitmap, 180, 30, myPaint);
        }
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

            database.execSQL(sqlCommand.toString());
        }catch (Exception ex){
        }
        try{
            documentoPdf.writeTo(new FileOutputStream(filePath));
        }catch (Exception error){
        }

    ShareFile(absoluteFileName.getAbsolutePath());

    }
    private void checarPermissao(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Verifica necessidade de explicar necessidade da permissao
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this,"É necessário a  de leitura e escrita!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE },
                        CODE_PERMISSION);
            } else {
                // Solicita permissao
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,  android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_PERMISSION);
            }
        }
        return;
    }
    private void ShareFile(String absoluteFileName){
        Log.i("  INSHARE: ", absoluteFileName );
        Uri arquivo = Uri.fromFile(new File(absoluteFileName));
        final Intent _intent = new Intent();
        try {
            _intent.setAction(Intent.ACTION_SEND);
            _intent.setType("application/pdf");
            _intent.putExtra(Intent.EXTRA_STREAM, arquivo);
            _intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //startActivity(Intent.createChooser(_intent, "Compartilhar"));
            //_intent.setPackage("com.whatsapp");
            startActivity(_intent);
        }catch (Exception error){


            try {
                final Intent iintent = new Intent();
                Uri Uarquivo = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", new File(absoluteFileName));
                iintent.setAction(Intent.ACTION_SEND);
                iintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                iintent.putExtra(Intent.EXTRA_STREAM, Uarquivo);
                iintent.setType("application/pdf");
                //_intent.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(iintent, "Compartilhar"));
            }catch (Exception eror){

            }
        }
    }
    public void btnHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}