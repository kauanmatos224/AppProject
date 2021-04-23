package com.example.securityaplication;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;


public class ac_cadastro extends AppCompatActivity {

    //variáveis e constantes para o uso de upload de imagem
    //***CONTEM UMA PERMISSÃO NO MANIFEST.XML PARA ESSE CÓDIGO FUNCIONAR (UPLOAD DE IMAGEM).
    public static final int IMAGEM_INTERNA = 12;
    public Bitmap bitmap; //variável da imagem em bitmap.
    public Byte imgByte; //variável com a imagem convertida em Byte para armazenar.
    public final int PERMISSAO_REQUEST = 2; //constante para requerir permissão do usuário para acessar galeria.
    public boolean imgSelecionada = false; //verifica se há imagem selecionada.

    //verifica as outras entradas.
    public boolean temNome = false;
    public boolean temCategoria = false;
    public boolean temDescri = false;

    byte[] byteArray; //imagem em bytes (Blob)

    //variáveis que vão tratar a entrada.
    String txtCateg;
    String txtNome;
    String txtDescri;
    EditText inputNome;
    EditText inputDescri;
    Spinner spnCateg;
    //fim das variáveis de entrada.
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_cadastro);
        //instancia objeto de Banco de dados para manipula-lo.
        db = openOrCreateDatabase("database_sm",Context.MODE_PRIVATE, null);

        //variáveis para armazenar os dados de entrada(nome do item, imagem, descrição, categoria)
        //para cadastro.
        //abaixo não tem a variável para imagem porque ela já está acima "bitmap".
        inputNome = (EditText)findViewById(R.id.inputNome);
        inputDescri = (EditText)findViewById(R.id.inputDescri);
        spnCateg = (Spinner)findViewById(R.id.spinnerCateg);
        //acima pega os valores dos widgets, enquanto abaixo converte-os em String para armazenar no Banco.
        txtCateg = spnCateg.getSelectedItem().toString();
        txtNome = inputNome.getText().toString();
        txtDescri = inputDescri.getText().toString();
        //fim das variáveis de entrada.




        //abre o Banco de Dados;
        //db = openOrCreateDatabase("database_sm", MODE_PRIVATE, null);

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
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(thumbnail);
            imgSelecionada = true;
            bitmap = thumbnail;

            //imgByte = bitmap.

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

    public void txtRemoveClick_removeImg(View view){

        ImageView iv = (ImageView) findViewById(R.id.imageView);

        Drawable drawable= getResources().getDrawable(R.drawable.img_add);
        iv.setImageDrawable(drawable); //seta a imagem de seleção de foto do item (imagem padrão)

        TextView txtRemove = (TextView)findViewById(R.id.txtRemove);
        bitmap = null;
        imgSelecionada = false;
        txtRemove.setVisibility(View.GONE);


    }
    public void btnCadastra(View view){
        //Aqui, chama método que verifica a entrada pelo usuário
        verificarEntradas();

    }
    private void verificarEntradas(){
        //Todos os campos serão obrigatórios, exceto Descrição e imagem.
        //obrigatórios: Nome do Item e Categoria.

        if(txtNome == ""){
            temNome = false;

            Toast.makeText(getBaseContext(),"Insira o nome do material.", Toast.LENGTH_LONG).show();
            inputNome.requestFocus(); //seta focus no nome.
        }
        else if(txtNome !=""){
            temNome = true;

            if(txtCateg ==""){
                temCategoria = false;
                Toast.makeText(getBaseContext(),"Selecione uma categoria do material.", Toast.LENGTH_LONG).show();
                spnCateg.requestFocus(); //seta focus na cetogoria.
            }
            else if(txtCateg != ""){
                temCategoria = true;

                //depois de verificar todas as entradas (obs: exceto imagem que já é tratada anteriormente)
                //chama método para inserir os dados na tabela de itens do Banco de Dados "database_sm"
                if(txtDescri == ""){
                    temDescri = false;
                    comprimirImagem();

                }
                else if(txtDescri !=""){
                    temDescri = true;
                    comprimirImagem();
                }

            }
        }


    }
    public void comprimirImagem(){

       /* if(imgSelecionada=true) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
            byteArray = stream.toByteArray();
            //compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        }
        else{
            String pathImgDawable = "R.drawable.sem_foto";
            Bitmap thumbnail = (BitmapFactory.decodeFile(pathImgDawable));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byteArray = stream.toByteArray();
            //compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }*/
        inserirDados();
    }


    private void inserirDados(){

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO tb_mats(nome_item,"+/* img_item,*/ "categoria, descri_item, status, descri_emp, datahora_inicial, datahora_final) VALUES (");
        sql.append("'" + txtNome + "',");

        //se não tem imagem selecionada, manda uma imagem "sem_foto.png"
        //sql.append("'" + byteArray + "',");


        sql.append("'" + txtCateg + "',");

        if (temDescri = false) {

            sql.append("' ',");
        }
        else{
            sql.append("'" + txtDescri + "',");
        }

        sql.append("'Disponível',");
        sql.append("'null',");
        sql.append("'null',");
        sql.append("'null'");
        sql.append(")");
        try {
            db.execSQL(sql.toString());
            Toast.makeText(getBaseContext(), "Material cadastrado com sucesso!",
                    Toast.LENGTH_SHORT).show();
        } catch (SQLException ex) {
            Toast.makeText(getBaseContext(), sql.toString() + "Erro =  " + ex.getMessage()+"\n"+
                    "Sentimos muito mesmo!!! Tente novamente mais tarde :(", Toast.LENGTH_LONG).show();
        }
    }

}