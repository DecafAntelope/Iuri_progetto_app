package com.Iuri.iuri_progetto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    String utente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Intent i = getIntent();
        utente = i.getStringExtra("username");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HelpActivity.this,MenuActivity.class);
        i.putExtra("mail",utente);
        startActivity(i);
        finish();
    }
}