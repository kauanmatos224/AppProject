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


public class ac_cadastro extends AppCompatActivity {

    //variáveis e constantes para o uso de upload de imagem
    //***CONTEM UMA PERMISSÃO NO MANIFEST.XML PARA ESSE CÓDIGO FUNCIONAR (UPLOAD DE IMAGEM).

    public final int PERMISSAO_REQUEST = 1; //constante para requerir permissão do usuário para acessar galeria.
    public boolean imgSelecionada = false; //verifica se há imagem selecionada.
    public static final int CODE_PERMISSION = 12;

    //verifica as outras entradas.
    public boolean temNome = false;
    public boolean temCategoria = false;
    public boolean temDescri = false;

    //byte[] byteArray; //imagem em bytes (Blob)
    public String imgPath;
    Bitmap thumbnail;

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
    //método com ação onClick do botão de cadastramento.
    public void btnCadastra(View view){
        //Aqui, chama método que verifica a entrada pelo usuário
        verificarEntradas();

    }
    private void verificarEntradas(){
        //Todos os campos serão obrigatórios, exceto Descrição e imagem.
        //obrigatórios: Nome do Item e Categoria.

        //abaixo não tem a variável para imagem porque ela já está acima "bitmap".
        inputNome = (EditText)findViewById(R.id.inputNome);
        inputDescri = (EditText)findViewById(R.id.inputDescri);
        spnCateg = (Spinner)findViewById(R.id.spinnerCateg);
        //acima pega os valores dos widgets, enquanto abaixo converte-os em String para armazenar no Banco.
        txtCateg = spnCateg.getSelectedItem().toString();
        txtNome = inputNome.getText().toString();
        txtDescri = inputDescri.getText().toString();
        //fim das variáveis de entrada.

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

                if(spnCateg.getSelectedItemPosition() == 0) {
                    Toast.makeText(getBaseContext(),"Selecione uma categoria do material.", Toast.LENGTH_LONG).show();
                    spnCateg.requestFocus(); //seta focus na cetogoria.
                }
                else {
                    if (txtDescri == "") {
                        temDescri = false;
                        checkPermission();

                    } else if (txtDescri != "") {
                        temDescri = true;
                        checkPermission();
                    }
                }


            }
        }


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

        final File selecionada = new File(imgPath);
        File rootPath  = new File(android.os.Environment.getExternalStorageDirectory()+"/security_material/imagens/");

        try {
            if (!rootPath.exists()) {
                rootPath.mkdirs();
            }
            if(!rootPath.exists()){
                rootPath = new File(imgPath);
            }

        }catch (Exception erro){
            Toast.makeText(getBaseContext(),"Erro ao criar pasta: "+erro, Toast.LENGTH_LONG).show();
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



        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO tb_mats(nome_item, img_path, categoria, descri_item, status, descri_emp) VALUES (");
        sql.append("'" + txtNome + "',");
        sql.append("'").append(novaImagem.getPath()).append("',");
        sql.append("'" + txtCateg + "',");

        if (temDescri = false) {
            sql.append("' ',");
        }
        else{
            sql.append("'" + txtDescri + "',");
        }

        sql.append("'Disponível',");
        sql.append("''");
        sql.append(")");
        try {
            db.execSQL(sql.toString());
            limpaEntradas();
            Toast.makeText(getBaseContext(), "Material cadastrado com sucesso!",
                    Toast.LENGTH_SHORT).show();

        } catch (SQLException ex) {
            Toast.makeText(getBaseContext(), sql.toString() + "Erro =  " + ex.getMessage()+"\n"+
                    "Sentimos muito mesmo!!! Tente novamente mais tarde :(", Toast.LENGTH_LONG).show();
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
        spnCateg.setSelection(0);
        inputDescri.setText("");
        inputNome.setText("");
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


}