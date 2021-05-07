package com.example.safemonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import para acessar a permissão de upload de imagem no banco de dados.
import android.Manifest;

//imports para utilizar Intent e Widgets em código.
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
import android.database.sqlite.SQLiteDatabase;

//imports para tranformar bitmap em byte.
import com.example.safemonkey.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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
    public boolean notDirCreat = false;

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
            if(thumbnail==null){
                Toast.makeText(getBaseContext(), "Imagem não suportada!", Toast.LENGTH_LONG).show();
                RemoveImg();
            }

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
        RemoveImg();
    }
    private void RemoveImg(){
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
                Toast.makeText(this,"É necessário a sua permissão!", Toast.LENGTH_SHORT).show();
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
        sql.append("INSERT INTO tb_mats(nome_item, img_path, categoria, descri_item, status, descri_emp) VALUES (");
        sql.append("'" + txtNome + "',");

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
                //Toast.makeText(getBaseContext(), "Erro ao criar pasta: " + erro, Toast.LENGTH_LONG).show();
            }
            final File novaImagem = new File(rootPath, selecionada.getName());

            //Movemos o arquivo!
            if(!notDirCreat) {
                try {
                    moveFile(selecionada, novaImagem);
                    Toast.makeText(getApplicationContext(), "Imagem movida com sucesso!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (notDirCreat = false) {
                    sql.append("'").append(novaImagem.getPath()).append("',");
                } else {
                    sql.append("'" + imgPath + "',");
                }
            }else{
                if(!novaImagem.exists()) {
                    if(!novaImagem.exists()){
                        File newImg = new File(getFilesDir()+"/security_material/img/"+selecionada.getName());
                        try {
                            moveFile(selecionada, newImg);
                        }catch (Exception ex){}

                        if(!newImg.exists()){
                            newImg = new File(getFilesDir()+"/"+selecionada.getName());
                            try {
                                moveFile(selecionada, newImg);
                            }catch (Exception ex){}
                            if(!newImg.exists()){
                                sql.append("'" + imgPath + "',");
                            }else{
                                sql.append("'").append(newImg.getPath()).append("',");
                            }
                        }else{
                            sql.append("'").append(newImg.getPath()).append("',");
                        }
                    }

                }
            }
        }
        else{
            sql.append("'',");
        }

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
            //Toast.makeText(getBaseContext(), "Material cadastrado com sucesso!",
                   // Toast.LENGTH_SHORT).show();

        } catch (SQLException ex) {
            //Toast.makeText(getBaseContext(),"Sentimos muito mesmo!!! Tente novamente mais tarde :(", Toast.LENGTH_LONG).show();
        }
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