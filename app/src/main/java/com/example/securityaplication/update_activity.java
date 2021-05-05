package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class update_activity extends AppCompatActivity {

    public final int PERMISSAO_REQUEST = 1; //constante para requerir permissão do usuário para acessar galeria.
    public boolean imgSelecionada = false; //verifica se há imagem selecionada.
    public static final int CODE_PERMISSION = 12;
    public boolean notDirCreat = false;
    public boolean clicked = false;

    TextView tNome, txtView;
    EditText edDescri, edEmp;
    Spinner sStatus;
    String empDescri;

    //byte[] byteArray; //imagem em bytes (Blob)
    public String imgPath;
    int idRecords;
    SQLiteDatabase sqldb;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_activity);
        Spinner spinner = (Spinner)findViewById(R.id.spnStatus);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spinner.getSelectedItemPosition()==2){
                    edEmp.setEnabled(true);
                    edEmp.setText(empDescri);
                }else {
                    edEmp.setEnabled(false);
                    edEmp.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        Intent intent = getIntent();
        idRecords = (int)intent.getSerializableExtra("idRegister");
        sqldb = openOrCreateDatabase("database_sm", Context.MODE_PRIVATE, null);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }else{

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        ,PERMISSAO_REQUEST);
            }
        }

        getData(idRecords);

    }
    private void getData(int id){

        cursor = sqldb.rawQuery("SELECT * FROM tb_mats " +
                "WHERE _id="+id+"", null);
        if(cursor.moveToFirst()){
            tNome = (TextView)findViewById(R.id.txtNome);
            sStatus = (Spinner)findViewById(R.id.spnStatus);
            TextView tCateg = (TextView)findViewById(R.id.txtCateg);
            edDescri = (EditText)findViewById(R.id.inputDescri);
            edEmp = (EditText)findViewById(R.id.descriEmp);
            txtView = (TextView)findViewById(R.id.textView9);
            edEmp = (EditText)findViewById(R.id.descriEmp);
            edEmp.setText(cursor.getString(6));
            empDescri = cursor.getString(6);
            Bitmap bmpImg;
            bmpImg = BitmapFactory.decodeFile(cursor.getString(2));
            ImageView img = (ImageView)findViewById(R.id.imageView);
            if(cursor.getString(2).equals("")|| cursor.getString(2).equals(null)){
                @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = this.getResources().getDrawable(R.drawable.sem_foto);
                bmpImg = ((BitmapDrawable)drawable).getBitmap();
                img.setImageBitmap(bmpImg);
            }else {
                img.setImageBitmap(bmpImg);
            }
            tNome.setText(cursor.getString(1));
            sStatus.setSelection(0);

            switch (cursor.getString(5)) {
                case "Disponível":
                    sStatus.setSelection(0);
                    edEmp.setEnabled(false);
                    edEmp.setText("");
                    break;
                case "Perdido":
                    sStatus.setSelection(1);
                    edEmp.setEnabled(false);
                    edEmp.setText("");
                    break;
                case "Emprestado":
                    sStatus.setSelection(2);
                    edEmp.setEnabled(true);
                    edEmp.setText(empDescri);
                    break;
            }
            edDescri.setText(cursor.getString(4));
            tCateg.setText(cursor.getString(3));
        }
        else{

        }

    }
    public void imgClick(View view){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    //trata a imagem selecionada e converte em bitmap (valor exibível em widget e utilizavel).
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            imgPath = picturePath; //caminho da imagem.

            //abaixo, decodifica a imagem para mostrar o ImageView.
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(thumbnail);
            imgSelecionada = true;

            TextView txtRemove = (TextView)findViewById(R.id.txtRemove);
            txtRemove.setVisibility(View.VISIBLE);
            if(thumbnail==null){
                Toast.makeText(getBaseContext(), "Imagem não suportada!", Toast.LENGTH_LONG).show();
                RemoveImg();
            }

        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSAO_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // A permissão foi concedida. Pode continuar
            }
            else {
                Toast.makeText(getBaseContext(), "A permissão de seleção de imagem não foi concedida."+"\n"+
                        "Se quiser selecionar uma imagem, tente novamente e conceda a permissão :)", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }

    //método OnClick no TextView vermelho de remoção da imagem.
    public void txtRemoveClick_removeImg(View view){
        RemoveImg();
    }
    private void RemoveImg(){
        ImageView iv = (ImageView) findViewById(R.id.imageView);

        Drawable drawable= getResources().getDrawable(R.drawable.sem_foto);
        iv.setImageDrawable(drawable); //seta a imagem de seleção de foto do item (imagem padrão)

        TextView txtRemove = (TextView)findViewById(R.id.txtRemove);
        imgSelecionada = false;
        txtRemove.setVisibility(View.GONE);
    }

    public void btnAtualiza(View view) {
        checkPermission();
    }

    private void checkPermission() {
        // Verifica necessidade de verificacao de permissao
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
        atualizarDados();
    }

    private void atualizarDados(){
        StringBuilder sql1 = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        StringBuilder sql3 = new StringBuilder();
        StringBuilder sql4 = new StringBuilder();
        StringBuilder sql5 = new StringBuilder();

        if(imgSelecionada) {
            File selecionada = new File(imgPath);
            File rootPath = new File(android.os.Environment.getExternalStorageDirectory() + "/security_material/imagens/");

            try {
                if (!rootPath.exists()) {
                    rootPath.mkdirs();
                }
                if (!rootPath.exists()) {
                    rootPath = new File(imgPath);
                    notDirCreat = true;
                }

            } catch (Exception erro) {
            }
            final File novaImagem = new File(rootPath, selecionada.getName());
            //Movemos o arquivo!
            if(!notDirCreat) {
                try {
                    moveFile(selecionada, novaImagem);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (notDirCreat = false) {
                    sql1.append(" update tb_mats" +
                            " set img_path='"+novaImagem.getPath()+"'" +
                            " where _id="+idRecords+";");
                } else {
                    sql1.append(" update tb_mats" +
                            " set img_path='"+imgPath+"'" +
                            " where _id="+idRecords+";");
                }
            }else{
                sql1.append(" update tb_mats" +
                        " set img_path='"+imgPath+"'" +
                        " where _id="+idRecords+";");
            }

        }else{
            sql1.append(" update tb_mats" +
                    " set img_path='"+cursor.getString(2)+"'" +
                    " where _id="+idRecords+";");
        }

        if(sStatus.getSelectedItemPosition()==2) {
            sql2.append("update tb_mats" +
                    " set `status`='Emprestado'" +
                    " where _id=" + idRecords + ";");
        }else if(sStatus.getSelectedItemPosition()==0){
            sql2.append(" update tb_mats" +
                    " set `status`='Disponível'" +
                    " where _id=" + idRecords + ";");
        }
        else if(sStatus.getSelectedItemPosition()==1){
            sql2.append(" update tb_mats" +
                    " set `status`='Perdido'" +
                    " where _id=" + idRecords + ";");
        }

        sql3.append(" update tb_mats" +
                " set descri_item='"+edDescri.getText().toString()+"'" +
                " where _id="+idRecords+";");
        sql3.append(" update tb_mats" +
                " set descri_emp='"+edEmp.getText().toString()+"'" +
                " where _id="+idRecords+";");
        try{
            sqldb.execSQL(sql1.toString());
            sqldb.execSQL(sql2.toString());
            sqldb.execSQL(sql3.toString());
            sqldb.execSQL(sql4.toString());
            sqldb.execSQL(sql5.toString());
            Toast.makeText(getBaseContext(),"As informações do item, foram alteradas",Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(getBaseContext(),"Houve algum erro na alteração, tente novamente mais tarde",Toast.LENGTH_LONG).show();
        }
            Intent intent = new Intent(this, MainActivity.class);
            sqldb.close();
            startActivity(intent);
            finish();
    }
    private void moveFile(File sourceFile, File destFile) throws IOException {

        if(sourceFile != destFile) {
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destFile);  // Transferindo bytes de entrada para saída
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            imgPath = destFile.getPath();
        }
        else {
            imgPath = destFile.getPath();
        }
    }
    public void btnHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}