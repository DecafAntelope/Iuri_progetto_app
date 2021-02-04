package com.Iuri.iuri_progetto;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.Iuri.iuri_progetto.R;

import java.io.File;
import java.util.ArrayList;

public class ElencoProgActivityOffline extends AppCompatActivity implements AdapterProgramma.OnProgListener,PopupMenu.OnMenuItemClickListener{

    File[] files;
    File directory;
    RecyclerView rclProg;
    ArrayList<Programma> list;
    AdapterProgramma adattatore;
    ImageButton cerca;
    EditText ric;
    int posizione;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_prog);
        directory = getExternalFilesDir(null);
        files= directory.listFiles();
        rclProg = (RecyclerView) findViewById(R.id.rclProg);
        ric=(EditText)findViewById(R.id.txtCercaProg);
        rclProg.setLayoutManager( new LinearLayoutManager(this));
        list = new ArrayList<Programma>();
        /*for(int i = 0;i<files.length;i++)
        {
            Programma p = new Programma();
            Log.d("nomeP",files[i].getName().replaceAll(".txt",""));
            String[] nomi= files[i].getName().replaceAll(".txt","").split("_");
            p.nomeProgramma = nomi[0];
            Log.d("nome",p.nomeProgramma);
            //nomi[1].replaceAll(".txt","");
            Log.d("autore",nomi[1]);
            p.autore = nomi[1];
            list.add(p);
        }
        adattatore = new AdapterProgramma(ElencoProgActivityOffline.this,list,ElencoProgActivityOffline.this::onProgClick);
        rclProg.setAdapter(adattatore);*/
        ListFile();
        cerca = (ImageButton) findViewById(R.id.imgBtnCerca);
        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                if(!ric.getText().toString().equals(""))
                {
                    for(int i = 0;i<files.length;i++)
                    {
                        Programma p = new Programma();
                        String[] nomi= files[i].getName().replaceAll(".txt","").split("_");
                        p.nomeProgramma = nomi[0];
                        p.autore = nomi[1];
                        if(p.autore.contains(ric.getText().toString()) || p.nomeProgramma.contains(ric.getText().toString()))
                            list.add(p);
                    }
                    adattatore = new AdapterProgramma(ElencoProgActivityOffline.this,list,ElencoProgActivityOffline.this::onProgClick);
                    rclProg.setAdapter(adattatore);
                }
            }
        });
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //toolBarLayout.setTitle(getTitle());

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void ListFile()
    {
        files= directory.listFiles();
        for(int i = 0;i<files.length;i++)
        {
            Programma p = new Programma();
            Log.d("nomeP",files[i].getName().replaceAll(".txt",""));
            String[] nomi= files[i].getName().replaceAll(".txt","").split("_");
            p.nomeProgramma = nomi[0];
            Log.d("nome",p.nomeProgramma);
            //nomi[1].replaceAll(".txt","");
            Log.d("autore",nomi[1]);
            p.autore = nomi[1];
            list.add(p);
        }
        adattatore = new AdapterProgramma(ElencoProgActivityOffline.this,list,ElencoProgActivityOffline.this::onProgClick);
        rclProg.setAdapter(adattatore);
    }
    @Override
    public void onProgClick(int position) {

        posizione = position;
        showPopup(this.rclProg.getChildAt(position));


    }

    public void showPopup(View view)
    {
        PopupMenu popup = new PopupMenu(this,view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_programma_offline,popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Programma p = list.get(posizione);
        File destFile = new File(getExternalFilesDir(null)+File.separator+p.nomeProgramma+"_"+p.autore+".txt");
        if(item.getItemId() == R.id.optAriOff)
        {
            Intent i = new Intent(ElencoProgActivityOffline.this,ProgrammaShowActivity.class);
            i.putExtra("file", destFile.getAbsolutePath().toString());
            i.putExtra("nomeP",p.nomeProgramma.toString());
            i.putExtra("autore",p.autore.toString());
            i.putExtra("offline",true);
            startActivity(i);
        }
        else if(item.getItemId() == R.id.optElim)
        {
            destFile.delete();
            list.clear();
            ListFile();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ElencoProgActivityOffline.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}