package com.example.securityaplication;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.LocalDateTime.of;

public class Lembrete2 extends AppCompatActivity {




    EditText descEmp;
    EditText data;
    EditText hora;
    Button btnLemb;

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




    @RequiresApi(api = Build.VERSION_CODES.O) //  <-- INICIALIZA O OBJETO LOCALDATATIME
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete2);

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

        //System.out.println(Shoraa);

        //-------------------------CALCULO DA DIFERENÇA DAS DATAS----------------------------------------
        LocalDateTime startDate = LocalDateTime.of(Sano, Smes, Sdia, Shoraa, Smin, Sseg, 0);
        LocalDateTime endDate = LocalDateTime.of(ano, mes, dia, horaa, min, seg, 0);

        Duration milis = Duration.between(startDate, endDate);
        long s = (milis.toMillis());
        //-------------------------CALCULO DA DIFERENÇA DAS DATAS----------------------------------------
        /* A VARIAVEL LONG S DEMONSTRADA ACIMA TEM O VALOR EM MILESSEGUNONOS DO TEMPO
        PASSADO ENTRE A DATA ATUAL E A DATA TRANMITIDA ATRAVÉS DE UMA VARIAVEL, A QUAL
         O USUARIO PODERIA DIGITAR NA TELA.
         */

        //System.out.println(s);
    }
}