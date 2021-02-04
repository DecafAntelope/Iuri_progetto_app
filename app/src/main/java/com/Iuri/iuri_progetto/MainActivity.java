package com.Iuri.iuri_progetto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    //Variabili
    Animation alto,basso;
    ImageView imm;
    TextView txt;

    static int tempo = 5000; //5 secondi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);       //nascondere barre in alto
        setContentView(R.layout.activity_main);

        getExternalCacheDir();
        getExternalFilesDir(null);
        //Animazioni
        alto= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        basso= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //View
        imm= findViewById(R.id.logo);
        txt = findViewById(R.id.txt);

        //assegnazione animazioni alle view
        imm.setAnimation(alto);
        txt.setAnimation(basso);
        new Handler().postDelayed(new Runnable()
        {
            public void run(){
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                Pair coppia= new Pair<View,String>(imm,"logo");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions opzioni = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,coppia);
                    startActivity(i,opzioni.toBundle());
                    finish();
                }
                else
                {
                    startActivity(i);
                    finish();
                }

            }
        },tempo);
    }


}