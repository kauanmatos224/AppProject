package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

    String TMPN = "";

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


                TmpNotification();
                /*
                notificaçao();
                transiçao();
                */



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


    // CONSTROI O CONTEÚDO DA NOTIFICAÇÃO


    //REALIZA A TRANSIÇÃO PARA A ACTIVITY ANTERIOR
    private void transiçao(){
        Intent intent = new Intent(this, ac_transitions.class);
        startActivity(intent);
    }



    //------------------NOTIFICAÇÃO COM DETECÇÃO DE TEMPO------------------------------
    private void TmpNotification(){
        Intent fullScreenIntent = new Intent(this, CallActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, TMPN)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Incoming call")
                        .setContentText("(919) 555-1234")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)

                        // Use a full-screen intent only for the highest-priority alerts where you
                        // have an associated activity that you would like to launch after the user
                        // interacts with the notification. Also, if your app targets Android 10
                        // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                        // order for the platform to invoke this notification.
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        Notification incomingCallNotification = notificationBuilder.build();

    }
    //------------------NOTIFICAÇÃO COM DETECÇÃO DE TEMPO------------------------------
}


