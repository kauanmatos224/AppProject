package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ac_transitions extends AppCompatActivity {

    Button btnLbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_transitions);

        // CRIA BOTÃO PARA FAZER A TRANSIÇÃO.
        btnLbt = (Button) findViewById(R.id.btnLbt);

        btnLbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Lembrete();
            }
        });



    }

    //MÉTODOS QUE SÃO EXECUTADOS AO CLICAR NOS SEGUINTES BOTÕES,
    //CUJOS COM OS MESMOS NOMES DOS MÉTODOS.
    public void btnCad(View view){
        Intent intent = new Intent(this, ac_cadastro.class);
        startActivity(intent);
    }


    public void btnMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnItens(View view){

        Intent intent = new Intent(this, Itens_ac.class);
        startActivity(intent);

    }
    public void btnMats(View view){
        Intent intent = new Intent(this, materiais_ac.class);
        startActivity(intent);

    }//FIM DOS MÉTODOS ONCLICK DOS BOTÕES COM ATRIBUTO ONCLICK NO DESIGN.

    // REALIZA TRANSIÇAÕ PARA A ACTIVITY LEMBRETE
    private void Lembrete(){
        Intent intent = new Intent(this, MainActivityAbacate.class);
        startActivity(intent);
    }


}