package com.example.securityaplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.LocalDateTime.of;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Lembrete extends AppCompatActivity {

    EditText descEmp;
    Button btnLemb;

    public static long difMilis;

///------CAPTURA INFORMAÇÕES SORE A DATA DE EMISSAÃO DO LEMBRETE--------
    EditText txtAno;
    EditText txtMes;
    EditText txtDia;
    EditText txtNome;
    EditText txtHora;
    EditText txtMin;
    Cursor cursor;
    String dateSys;
///------CAPTURA INFORMAÇÕES SORE A DATA DE EMISSAÃO DO LEMBRETE--------

    //VARIVEIS DO TEMPO FINAL
    int ano=10;
    int mes=10;
    int dia=10;
    int seg=10;
    int horaa=10;
    int min=10;

    //VARIAVEIS DO TEMPO ATUAL DO SISTEMA
    int Sano;
    int Smes;
    int Sdia;
    int Sseg;
    int Shoraa;
    int Smin;
    int idRecords;

    // TESXTO DESCRIÇÃO DO EMPRESTIMO
    public static String DescEmp;


    // VARIAVEIS TESTE
    String DATA = "";
    String HORA = "";

    boolean AlterState = false;
    SQLiteDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.O) //  <-- INICIALIZA O OBJETO LOCALDATATIME
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete2);
        Intent intent = getIntent();
        idRecords = (int)intent.getSerializableExtra("IdR");
        db = openOrCreateDatabase("database_sm", Context.MODE_PRIVATE, null);
        cursor = db.rawQuery("select * from tb_mats where _id="+idRecords+";", null);

        //-------------REFERENCIAMENTO---------------------------------------------
        btnLemb = findViewById(R.id.btnLemb);
        txtNome = findViewById(R.id.editNome);
        txtAno = findViewById(R.id.txtAno);
        txtMes = findViewById(R.id.txtMEs);
        txtMin = findViewById(R.id.TxtMin);
        txtHora = findViewById(R.id.txtHora);
        txtDia = findViewById(R.id.txtDia);

        //-------------REFERENCIAMENTO---------------------------------------------

        btnLemb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateSys = getDate();
                //--CAPTURA AS INFORMAÇÕEOS SOBRE A DATA E HORA FINAL, E CONVERTE EM INT---------
                String anoC = txtAno.getText().toString();
                ano = Integer.parseInt(anoC.format(anoC));

                String mesC = txtMes.getText().toString();
                mes = Integer.parseInt(mesC.format(mesC));


                 String diaC = txtDia.getText().toString();
                dia = Integer.parseInt(diaC.format(diaC));


                String horaC = txtHora.getText().toString();
                horaa = Integer.parseInt(horaC.format(horaC));


                String minC = txtMin.getText().toString();
                min = Integer.parseInt(minC.format(minC));

                System.out.println(ano+" "+mes+" "+dia+" "+horaa+" "+min);
                //--CAPTURA AS INFORMAÇÕEOS SOBRE A DATA E HORA FINAL, E CONVERTE EM INT---------
                if(cursor.moveToFirst()){
                    AlterState = true;
                    alterState();
                }
                else{
                    Intent intentAc = new Intent(Lembrete.this, MainActivity.class);
                    startActivity(intentAc);
                    finish();
                    db.close();
                }

                CalcData();
                maca();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O) //  <-- INICIALIZA O OBJETO LOCALDATATIME
    private void CalcData(){

        //RECUPERA A DATA DO SISTEMA E QUEBRA EM PEDAÇOS ATRINUINDO CADA PARTE A UMA VARIAVEL INT
        Date dateAno = new Date();
        SimpleDateFormat formatar = new SimpleDateFormat("y");
        String p1 = formatar.format(dateAno);
        Sano = Integer.parseInt(p1.format(p1));


        //RECUPERA A DATA DO SISTEMA E QUEBRA EM PEDAÇOS ATRINUINDO CADA PARTE A UMA VARIAVEL INT
        Date datemes = new Date();
        SimpleDateFormat formatar1 = new SimpleDateFormat("M");
        String p2 = formatar1.format(dateAno);
        Smes = Integer.parseInt(p2.format(p2));


        //RECUPERA A DATA DO SISTEMA E QUEBRA EM PEDAÇOS ATRINUINDO CADA PARTE A UMA VARIAVEL INT
        Date dateHora = new Date();
        SimpleDateFormat formatar2 = new SimpleDateFormat("H");
        String p4 = formatar2.format(dateAno);
        Shoraa = Integer.parseInt(p4.format(p4));

        //RECUPERA A DATA DO SISTEMA E QUEBRA EM PEDAÇOS ATRINUINDO CADA PARTE A UMA VARIAVEL INT
        Date dateDia = new Date();
        SimpleDateFormat formatar3 = new SimpleDateFormat("d");
        String p5 = formatar3.format(dateAno);
        Sdia = Integer.parseInt(p5.format(p5));

        //RECUPERA A DATA DO SISTEMA E QUEBRA EM PEDAÇOS ATRINUINDO CADA PARTE A UMA VARIAVEL INT
        Date dateMin = new Date();
        SimpleDateFormat formatar4 = new SimpleDateFormat("m");
        String p6 = formatar4.format(dateAno);
        Smin = Integer.parseInt(p6.format(p6));

        //RECUPERA A DATA DO SISTEMA E QUEBRA EM PEDAÇOS ATRINUINDO CADA PARTE A UMA VARIAVEL INT
        Date dateSeg = new Date();
        SimpleDateFormat formatar5 = new SimpleDateFormat("s");
        String p7 = formatar5.format(dateAno);
        Sseg = Integer.parseInt(p7.format(p7));

        System.out.println(Sano+" "+Smes+" "+Sdia+" "+Shoraa+" "+Smin+" "+Sseg);

        //-------------------------CALCULO DA DIFERENÇA DAS DATAS----------------------------------------
        LocalDateTime startDate = LocalDateTime.of(Sano, Smes, Sdia, Shoraa, Smin, Sseg, 0);
        LocalDateTime endDate = LocalDateTime.of(ano, mes, dia, horaa, min, Sseg, 0);

        Duration milis = Duration.between(startDate, endDate);
        difMilis = (milis.toMillis());
        //-------------------------CALCULO DA DIFERENÇA DAS DATAS----------------------------------------
        /* A VARIAVEL LONG S DEMONSTRADA ACIMA TEM O VALOR EM MILESSEGUNONOS DO TEMPO
        PASSADO ENTRE A DATA ATUAL E A DATA TRANMITIDA ATRAVÉS DE UMA VARIAVEL, A QUAL
         O USUARIO PODERIA DIGITAR NA TELA.
         */

        System.out.println(difMilis);
    }
    private void maca(){
        Intent intent = new Intent(this, MainActivityAbacate.class);
        startActivity(intent);
    }
    private void alterState(){
        try {
            StringBuilder sqlCommand = new StringBuilder();
            sqlCommand.append("update tb_mats" +
                    " set `status`='Emprestado'" +
                    " where _id=" + idRecords);
            StringBuilder sqlCommand1 = new StringBuilder();
            sqlCommand1.append("update tb_mats"+
                    " set descri_emp='Item emprestado para "+txtNome.getText().toString()+"\n" +
                    " em " + dateSys+ " para ser recuperado no dia "+txtDia.getText().toString()+"/"+txtMes.getText().toString()+"/"+txtAno.getText().toString()+" às "+txtHora.getText().toString()+":"+txtMin.getText().toString()+".';"+
                    " where _id="+idRecords+";");
            db.execSQL(sqlCommand.toString());
            db.execSQL(sqlCommand1.toString());
        }catch (Exception ex){
            Intent intentAc = new Intent(Lembrete.this, MainActivity.class);
            Toast.makeText(getBaseContext(), "Ocorreu um erro, tente novamente mais tarde", Toast.LENGTH_LONG).show();
            startActivity(intentAc);
            finish();
            db.close();
        }
        return;
    }
    public void btnHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

}