package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.os.Bundle;

public class Itens_ac extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens_ac);
        db = openOrCreateDatabase("dados_telefone.db",Context.MODE_PRIVATE, null);

        //Objeto usado para construir a STRING do comando SQL a ser         executado
        // Neste caso a string SQL conterá o comando de criação de
        //      tabelas
        StringBuilder sql = new StringBuilder();
//Obrigatoriamente tem que ter uma coluna _id no banco SQL Lite
        sql.append("CREATE TABLE IF NOT EXISTS contato(");
        sql.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append("nome varchar(100),");
        sql.append("telefone varchar(10) )");

        try

        {
//executa um comando SQL, neste caso a StringBuilder SQL
            db.execSQL(sql.toString());
        }
        catch(
                Exception ex)

        {
            Toast.makeText(getBaseContext(), "Error = " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    public void onClick (View v)
                    {
                        inserir();
                    }
                });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                    public void onClick (View v)
                    {
                        mostrar();
                    }
                });

    }

    private void inserir() {
        EditText txtNome, txtTelefone;
        String nome, telefone;
        txtNome = (EditText) findViewById(R.id.txtnome);
        txtTelefone = (EditText) findViewById(R.id.txttel);
        nome = txtNome.getText().toString();
        telefone = txtTelefone.getText().toString();

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO contato(nome,telefone) VALUES (");
        sql.append("'" + nome + "'");
        sql.append(",");
        sql.append("'" + telefone + "'");
        sql.append(")");
        try {
            db.execSQL(sql.toString());
            Toast.makeText(getBaseContext(), "OK - Inserido",
                    Toast.LENGTH_SHORT).show();
        } catch (SQLException ex) {
            Toast.makeText(getBaseContext(), sql.toString() + "Erro =  " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void mostrar() {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM contato");
        Cursor dados = db.rawQuery(sql.toString(), null);

        //Array com os ID dos campos do layout dados
        int[] to = {R.id.tvId, R.id.tvNome, R.id.tvTelefone};
        //Array com o nome dos campos da tabela que serão mostrados
        String[] from = {"_id", "nome", "telefone"};


        try {
            db.execSQL(sql.toString());
            Toast.makeText(getBaseContext(), "OK - Inserido",
                    Toast.LENGTH_SHORT).show();
        } catch (SQLException ex) {
            Toast.makeText(getBaseContext(), sql.toString() + "Erro =                   "
                    + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            SimpleCursorAdapter ad = new
                    SimpleCursorAdapter(getBaseContext(), R.layout.dados, dados,
                    from, to, 0);
            ListView lvDados;
            lvDados = (ListView) findViewById(R.id.lvDados);
            lvDados.setAdapter(ad);
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), sql.toString() + " Erro = "
                    + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}




