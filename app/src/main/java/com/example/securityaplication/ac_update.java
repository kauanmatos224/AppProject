package com.example.securityaplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import para acessar a permissão de upload de imagem no banco de dados.
import android.Manifest;

//imports para utilizar Intent e Widgets em código.
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//imports para o banco de dados
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//imports para tranformar bitmap em byte.
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;


public class ac_update extends AppCompatActivity {

    //variáveis e constantes para o uso de upload de imagem
    //***CONTEM UMA PERMISSÃO NO MANIFEST.XML PARA ESSE CÓDIGO FUNCIONAR (UPLOAD DE IMAGEM).

    public final int PERMISSAO_REQUEST = 1; //constante para requerir permissão do usuário para acessar galeria.
    public boolean imgSelecionada = false; //verifica se há imagem selecionada.
    public static final int CODE_PERMISSION = 12;

    //verifica as outras entradas.
    public boolean temNome = false;
    public boolean temCategoria = false;
    public boolean temDescri = false;
    public boolean notDirCreat = false;
    public boolean fimEmp = false;
    public String spnStatus="";
    public String StatusValue="";

    //byte[] byteArray; //imagem em bytes (Blob)
    public String imgPath;
    Bitmap thumbnail;

    //fim das variáveis de entrada.
    SQLiteDatabase db;
    Cursor cursor;
    public long idRegistro = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_cadastro);

        Intent intent = getIntent();
        idRegistro = (long)intent.getSerializableExtra("idRegister");

        //instancia objeto de Banco de dados para manipula-lo.
        db = openOrCreateDatabase("database_sm",Context.MODE_PRIVATE, null);
        cursor = db.rawQuery("select * from tb_mats where _id="+idRegistro+";", null);
        Spinner spinnerStatus = (Spinner)findViewById(R.id.spnStatus);
        spnStatus = spinnerStatus.getSelectedItem().toString();

        //carrega todos os dados do banco de dados e exibe na activity.
        getDataActivity();

        //pede permissão para selecionar imagem da geleria, a partir de uma tag "permission" do Manifest.xml.
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }else{

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        ,PERMISSAO_REQUEST);
            }
        }
    }

    //abre a galeria para fazer upload de imagem para o app.
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
            thumbnail = (BitmapFactory.decodeFile(picturePath));
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(thumbnail);
            imgSelecionada = true;

            TextView txtRemove = (TextView)findViewById(R.id.txtRemove);
            txtRemove.setVisibility(View.VISIBLE);

        }
    }

    //varifica se as permissões foram concedidas pelo usuário para o upload de imagem.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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

        ImageView iv = (ImageView) findViewById(R.id.imageView);

        Drawable drawable= getResources().getDrawable(R.drawable.img_add);
        iv.setImageDrawable(drawable); //seta a imagem de seleção de foto do item (imagem padrão)

        TextView txtRemove = (TextView)findViewById(R.id.txtRemove);
        imgSelecionada = false;
        txtRemove.setVisibility(View.GONE);


    }
    //carrega, ou recarrega os dados do banco para a tela.
    public void getDataActivity(){
        TextView txtNome = (TextView)findViewById(R.id.txtNome);
        TextView txtCateg = (TextView)findViewById(R.id.txtCateg);
        EditText txtDescri = (EditText)findViewById(R.id.inputDescri);
        TextView txtStatus = (TextView)findViewById(R.id.txtStatus);
        Bitmap bmpImg = (BitmapFactory.decodeFile(cursor.getString(2)));
        ImageView imgv = (ImageView)findViewById(R.id.imageView);
        imgv.setImageBitmap(bmpImg);
        txtNome.setText(cursor.getString(1));
        txtCateg.setText(cursor.getString(3));
        txtDescri.setText(cursor.getString(4));
        txtStatus.setText(cursor.getString(5));

        if(cursor.getString(5)!="Emprestado"){
            Button btnEmp = (Button)findViewById(R.id.btnFimEmp);
            btnEmp.setVisibility(View.INVISIBLE);
        }
        else{
            Button btnEmp = (Button)findViewById(R.id.btnFimEmp);
            btnEmp.setVisibility(View.INVISIBLE);
        }
        TextView txtInfoS = (TextView)findViewById(R.id.txtInfoS);
        txtInfoS.setText("mudar o status do seu material, pode desativar lembretes relacionados ao mesmo.");
        txtInfoS.setVisibility(View.INVISIBLE);
        return;
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
        inserirDados();
    }

    //monta uma consulta SQL com StringBuilder e append, inserindo os dados de entrada na consulta, e executa-a gravando na
    //tabela tb_mats da database.
    private void inserirDados(){

        StringBuilder sql = new StringBuilder();
        sql.append("update tb_mats" +
                " set img_path='"+imgPath+"'" +
                " where _id="+idRegistro+";");

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
                Toast.makeText(getBaseContext(), "Erro ao criar pasta: " + erro, Toast.LENGTH_LONG).show();
            }
            final File novaImagem = new File(rootPath, selecionada.getName());

            //Movemos o arquivo!
            try {
                moveFile(selecionada, novaImagem);
                Toast.makeText(getApplicationContext(), "Imagem movida com sucesso!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if(notDirCreat=false) {
                sql.append("\n"+"update tb_mats" +
                        " set img_path='"+novaImagem.getPath()+"'" +
                        " where _id="+idRegistro+";");
            }
            else{
                sql.append("\n"+"update tb_mats" +
                        " set img_path='"+imgPath+"'" +
                        " where _id="+idRegistro+";");
            }
        }
        else{
            sql.append("\n"+"update tb_mats" +
                    " set img_path=''" +
                    " where _id="+idRegistro+";");
        }
        if (temDescri = false) {
            sql.append("\n"+"update tb_mats" +
                    " set descri_item=''" +
                    " where _id="+idRegistro+";");
        }
        else{
            EditText edDescri = (EditText)findViewById(R.id.inputDescri);
            sql.append("\n"+"update tb_mats" +
                    " set descri_item='"+edDescri.getText().toString()+"'" +
                    " where _id="+idRegistro+";");
        }
        try {
            db.execSQL(sql.toString());
            limpaEntradas();
            Toast.makeText(getBaseContext(), "As informações foram salvas!",
                    Toast.LENGTH_SHORT).show();

        } catch (SQLException ex) {
            Toast.makeText(getBaseContext(),"Sentimos muito mesmo!!! Tente novamente mais tarde :(", Toast.LENGTH_LONG).show();
        }
    }

    private void moveFile(File sourceFile, File destFile) throws IOException {
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
        //Alertamos, caso não consiga remover
        if(!sourceFile.delete()){
            Toast.makeText(getApplicationContext(), "Não foi possível remover a imagem!", Toast.LENGTH_SHORT).show();
        }
    }


    //limpa as entradas de dados.
    public void limpaEntradas(){

        EditText inputDescri = (EditText)findViewById(R.id.inputDescri);
        inputDescri.setText("");
        ImageView iv = (ImageView)findViewById(R.id.imageView);

        Drawable drawable= getResources().getDrawable(R.drawable.img_add);
        iv.setImageDrawable(drawable); //seta a imagem de seleção de foto do item (imagem padrão)

        TextView txtRemove = (TextView)findViewById(R.id.txtRemove);
        txtRemove.setVisibility(View.GONE);

        //ao terminar cadastro do item, vai para a activity de exibição dos mesmos.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void btnHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void btnFimEmp(View view){
        //muda as cores do botão e a variável bool do salvamento da informação de empréstimo.
        Button btnEmp = (Button)findViewById(R.id.btnFimEmp);
        if(fimEmp){
            fimEmp = false;
            btnEmp.setBackgroundColor(getResources().getColor(R.color.verde));
        }
        else{
            fimEmp = true;
            btnEmp.setBackgroundColor(getResources().getColor(R.color.rosa));
        }
    }
    public void btnUpdate(View view){
        //Aqui, chama método que verifica a entrada pelo usuário
        if(!spnStatus.equals("Selecionar Status")){
            trataEntrada();
        }
        else{
            Toast.makeText(getBaseContext(), "Selecione um status", Toast.LENGTH_LONG).show();
        }

    }
    public void trataEntrada(){
        if(spnStatus != cursor.getString(5) && spnStatus.equals("Emprestado")){

        }
    }

    //método com ação onClick do botão de cadastramento.


}