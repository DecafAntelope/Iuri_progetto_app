package com.Iuri.iuri_progetto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    Button crea,cerca,utente;
    FloatingActionButton help;
    String user;
    FirebaseAuth riferimentoAuth;

    DatabaseReference riferimentoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setSupportActionBar(toolbar);
        Intent i = getIntent();
        user = i.getStringExtra("mail");
        riferimentoAuth = FirebaseAuth.getInstance();
        String uid = riferimentoAuth.getCurrentUser().getUid().toString();
        //ut = riferimentoData.getInstance().getReference().child(riferimentoAuth.getCurrentUser().getUid()).;

        //user =ut.username;
        //Toast.makeText(this,user, Toast.LENGTH_SHORT).show();
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/

        crea = (Button) findViewById(R.id.btnCreaScheda);
        cerca = (Button) findViewById(R.id.btnElencProg);
        crea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controllaConnessione())
                {
                    Intent i = new Intent(MenuActivity.this,CreateActivity.class);
                    i.putExtra("username",user);
                    startActivity(i);
                    finish();
                }

            }
        });
        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controllaConnessione())
                {
                    Intent i = new Intent(MenuActivity.this,ElencoProgActivity.class);
                    i.putExtra("username",user);
                    startActivity(i);
                    finish();
                }

            }
        });
        utente = (Button) findViewById(R.id.btnModificaUtente);
        utente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controllaConnessione())
                {
                    Intent i = new Intent(MenuActivity.this,UserActivity.class);
                    i.putExtra("username",user);
                    startActivity(i);
                    finish();
                }

            }
        });
        help = (FloatingActionButton) findViewById(R.id.btnHelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this,HelpActivity.class);
                i.putExtra("username",user);
                startActivity(i);
            }
        });
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sicuro di voler uscire?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MenuActivity.this,LoginActivity.class);
                        riferimentoAuth.signOut();
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

    public boolean controllaConnessione()
    {
        ConnectivityManager cm = (ConnectivityManager) MenuActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(MenuActivity.this,"Nessuna connessione ad intenet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}