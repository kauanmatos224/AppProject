package com.example.securityaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ac_transitions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_transitions);
    }

    public void btnCad(View view){
        Intent intent = new Intent(this, ac_cadastro.class);
        startActivity(intent);
    }


    public void btnMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}