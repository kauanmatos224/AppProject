package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

//imports para o banco de dados
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
//fim dos imports;


public class splash extends AppCompatActivity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                db = openOrCreateDatabase("database_sm",Context.MODE_PRIVATE, null);

                //Objeto usado para construir a STRING do comando SQL a ser executado
                // Neste caso a string SQL conterá o comando de criação de tabelas
                StringBuilder sql = new StringBuilder();
                //cria as colunnas / campos na tabela dos itens(tb_mats)
                //Obrigatoriamente tem que ter uma coluna _id no banco SQL Lite
                sql.append("CREATE TABLE IF NOT EXISTS tb_mats(");
                sql.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
                sql.append("nome_item varchar(100),");
                sql.append("img_path String,");
                sql.append("categoria varchar(100),");
                sql.append("descri_item varchar(100),");
                sql.append("status varchar(20),");
                sql.append("descri_emp varchar(100));");
             
                try

                {
                //executa um comando SQL, neste caso a StringBuilder SQL
                    db.execSQL(sql.toString());
                    //se não houver erro na criação, a instrução sql 'sql' foi executada com sucesso
                    //abaixo faz a verificação para saber se há registros na tabela
                    //senão houver, então direciona para a tela de cadastros.
                    //a variável sqlVerTabela está armaenada em strings.xml do diretório "values"
                    //onde contém a Query para verificar se há registros
                    @SuppressLint("Recycle") Cursor cur = db.rawQuery(getString(R.string.sqlVerTabela), null);
                    //possível alteração:  Cursor cur = db.rawQuery(getString(R.string.sqlVerTabela), null);

                    if (cur != null) {
                        cur.moveToFirst();
                        if (cur.getInt (0) == 0) {
                            //nesse caso verifica-se que não há registros no banco.
                            //Toast.makeText(getBaseContext(),"Não há nenhum material ainda cadastrado", Toast.LENGTH_LONG).show();

                            try{
                                db.close();

                               chamaTelaCadastro();

                            }catch(Exception erroClosingDB){
                              //  Toast.makeText(getBaseContext(),"Houve algum erro, tente novamente mais tarde :(" +
                                //        "\n"+"erro: "+erroClosingDB, Toast.LENGTH_LONG).show();
                            }

                        }
                        else {
                            //nesse caso há registros no banco, então porque não direcionar
                            //para a tela dos itens? :)
                            db.close();
                            //Toast.makeText(getBaseContext(),"Que tal ver seus itens cadastrados?!", Toast.LENGTH_LONG).show();
                            mostrarMainActivity();
                        }
                    }

                }
                catch(Exception exErro) {
                    //em caso de erro na criação da tabela.
                    //Toast.makeText(getBaseContext(), "Error = " + exErro.getMessage(),
                      //      Toast.LENGTH_LONG).show();
                }

            }
        }, 5000);
    }

    private void mostrarMainActivity() {
        Intent intent = new Intent(
                this,MainActivity.class
        );
        startActivity(intent);
        finish();
    }

    private void chamaTelaCadastro(){
        Intent intent = new Intent(
                this,ac_cadastro.class
        );
        startActivity(intent);
        finish();
    }
}