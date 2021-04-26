package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class lembrete extends AppCompatActivity {


    // CONFIGURA UMA CONSTATNTE QUE DEFINIRÁ O VALOR DO ID DO CANAL DE NOTIFICAÇAÕ
    static final String CHANNEL_ID = "ID_CANAL";


    ///-----------------CRIAÇÃO DE OBJETOS----------------------------

    // CONFIGURA OBJETO EDITTEXT QUE RECEBE O VALOR DA DESCRIÇÃO DO LEMBRETE DIGITADO PELO USUARIO
    EditText txtNotification;

    //CRIA O OOBJETO BUTONN
    Button btnDesc;

    // CONFIGURA OBJETO EDITTEXT QUE RECEBE O VALOR DO NOME DO LEMBRETE DIGITADO PELO USUARIO
    EditText txtNlbt;


    ///------------------CRIAÇÃO DE OBJETOS-------------------------



    //-------------------CRIAÇAÕ DE VARIAVEIS-------------------------

    // VARIAVEL QUE RECEBE O VALOR DA OBJETO EDITTEXT
    String desc = "";

    String nome ="";

    //--------------------CRIAÇAÕ DE VARIAVEIS-------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete);

        //CONFIGURA O CANAL DE NOTIFICAÇÃO
        createNotificationChannel();


        //--------------REFERECIAMENTO----------------

        btnDesc = (Button) findViewById(R.id.btnDesc);

        txtNotification = (EditText) findViewById(R.id.txtNotification);

        txtNlbt = (EditText) findViewById(R.id.txtNlbt);

        //--------------REFERECIAMENTO-----------------






        //-------------------BOTÃO---------/////////////////////////////////////////---------
        btnDesc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            // Volta para a outra activity
            {

                // DESC RECEBE O VALOR DO OBJETO EDITTEXT DESCRIÇAÕ
                desc = txtNotification.getText().toString();

                //NOME RECEBE O VALOR DO OBJETO EDITTEXT NOME
                nome = txtNlbt.getText().toString();

                notificaçao();
                transiçao();




            }
        });
        //-------------------BOTÃO---------/////////////////////////////////////////---------














    }

    //-----------CRIA O CANAL DA NOTIFICAÇÃO---------------
    public void createNotificationChannel()
    {
        // CONDICIONAL QUE  CONFIGURA O NotificationChannel APENAS APRA API MAIOR QUE 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = getString(R.string.channel_name);
            //CONFIGURA O NOME CANAL QUE IRÁ SER MOSTRADO NAS CONFIGURAÇÕES DO SISTEMA
            String description = getString(R.string.channel_description);
            //CONFIGURA A DESCRIÇÃO DO CANAL QUE IRÁ SER MOSTRADA NAS CONFIGURAÇÕES DO SISTEMA
            int importance = NotificationManager.IMPORTANCE_HIGH; /*PRIORIADA DE NOTIFICAÇÃO
            DEFINIDA COMO MAXIMA --> EXIBE AS INFORMAÇÕES NA BARRA DE STATUS E EMITE UM SOM*/

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            //CONFIGURA O ID DO DO CANAL -->VALOR IMUTAVEL/CONSTANTE

            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
    //-----------CRIA O CANAL DA NOTIFICAÇÃO---------------


    // CONSTROI E EXIBE A NOTIFICAÇAÕ
    private void notificaçao(){

        //----------CRIA NOTIFICAÇAÕ PARA SER EXIBIDA NA BARRA DE STATUS------------

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //CONFIGURA O RSPECTIVO CANAL DA NOTIFICAÇAÕ COM SEU ID ESPECIFICO

                .setSmallIcon(R.drawable.icn) //ICN --> ICONE DA NOTIFICAÇÃO

                .setContentTitle(nome) //nome_da_notificação

                .setContentText(desc) //TEXTO DA NOTIFICAÇAÕ DE UMA ONICA LINHA

                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(desc)) //TEXTO_DA_NOTIFICAÇÃO MAIOR_QUE_UMA_LINHA

                .setPriority(NotificationCompat.PRIORITY_HIGH); // DEFINE A PRIORIDADE DA NOTIFICAÇÃO
        //--> PRIORIDA DEFINIDA COMO MAXIMA --> EXIBE A NOTIFICAÇAÕ NA BARRA DE STATUS E EMITE UM SOM

        //----------CRIA NOTIFICAÇAÕ PARA SER EXIBIDA NA BARRA DE STATUS-------------------------




        // ---------EXIBE A NOTIFICAÇÃO----------------------------------------------------
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        //ID DE EXIBIÇÃO DA NOTIFICA ÇÃO-->        !!!! TEM QUE DAR UM JEITO DE ATRIBUIR UM ID QUE SEJA
        //UNICO PARA A NOTIFICAÇÃO, USANDO UM ARRAY ACHO, OU DEUS SABE LÁ O QUE....
        int notificationId = 5;
        notificationManager.notify(notificationId, builder.build());
        // ---------EXIBE A NOTIFICAÇÃO----------------------------------------------------


    }


    //REALIZA A TRANSIÇÃO PARA A ACTIVITY ANTERIOR
    private void transiçao(){
        Intent intent = new Intent(this, ac_transitions.class);
        startActivity(intent);
    }

}