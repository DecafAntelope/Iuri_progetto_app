package com.Iuri.iuri_progetto;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ElencoProgActivity extends AppCompatActivity  implements AdapterProgramma.OnProgListener,PopupMenu.OnMenuItemClickListener{

    DatabaseReference riferimento;
    RecyclerView recycler;
    ArrayList<Programma> list;
    AdapterProgramma adattatore;
    ImageButton cerca;
    EditText ric;
    String user;
    ArrayList<String> listaPres;
    ValueEventListener inizio,ricerca;
    int posizione;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_prog);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        listaPres = new ArrayList<String>();
        user = getIntent().getStringExtra("username");
        cerca = (ImageButton) findViewById(R.id.imgBtnCerca);
        ric=(EditText)findViewById(R.id.txtCercaProg);
        recycler = (RecyclerView) findViewById(R.id.rclProg);
        recycler.setLayoutManager( new LinearLayoutManager(this));
        list = new ArrayList<Programma>();
        if(controllaConnessione())
        {
            cerca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ric.getText().toString().equals(""))
                    {

                        riferimento.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear();
                                for(DataSnapshot dataSnapshot1: snapshot.getChildren())
                                {
                                    Programma p=dataSnapshot1.getValue(Programma.class);
                                    if(p.nomeProgramma.contains(ric.getText().toString()) || p.autore.contains(ric.getText().toString()))
                                    {
                                        list.add(p);
                                        Log.d("conta2",p.nomeProgramma);
                                    }

                                }
                                adattatore = new AdapterProgramma(ElencoProgActivity.this,list,ElencoProgActivity.this::onProgClick);
                                recycler.setAdapter(adattatore);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(ElencoProgActivity.this, "Devi scrivere qualcosa da cercare", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            riferimento = FirebaseDatabase.getInstance().getReferenceFromUrl("https://prova-6189f-default-rtdb.firebaseio.com").child("programmi");
            riferimento.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for(DataSnapshot dataSnapshot1: snapshot.getChildren())
                    {
                        Programma p=dataSnapshot1.getValue(Programma.class);
                        Log.d("conta",p.nomeProgramma);
                        list.add(p);
                    }
                    for(int i = 0;i<list.size();i++)
                    {
                        Log.d("for"+String.valueOf(i),list.get(i).nomeProgramma);
                    }
                    Log.d("for","fuori dal for");
                    adattatore = new AdapterProgramma(ElencoProgActivity.this,list,ElencoProgActivity.this::onProgClick);
                    recycler.setAdapter(adattatore);
                    adattatore.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        /*setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onProgClick(int position) {
        posizione = position;
        showPopup(this.recycler.getChildAt(position));
    }

    public void showPopup(View view)
    {
        PopupMenu popup = new PopupMenu(this,view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_programma,popup.getMenu());
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Programma p = list.get(posizione);
        File destFile = new File(getExternalFilesDir(null)+File.separator+p.nomeProgramma+"_"+p.autore+".txt");
        if(!destFile.exists())
        {
            Log.d("controllo if","sei dentro l'if ok");
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("Programmi");
            StorageReference file =ref.child(p.nomeProgramma+"_"+p.autore);
            file.getFile(destFile);
        }
        Log.d("file",destFile.getName());
        while(!destFile.exists())
        {
            destFile = new File(getExternalFilesDir(null)+File.separator+p.nomeProgramma+"_"+p.autore+".txt");
        }
        if(item.getItemId() == R.id.optComm)
        {
            Intent i = new Intent(ElencoProgActivity.this,CommentActivity.class);
            i.putExtra("nomeFile",p.nomeProgramma+"_"+p.autore);
            i.putExtra("username",user);
            i.putExtra("programmashow",true);
            startActivity(i);
        }
        else
        {

            Intent i = new Intent(ElencoProgActivity.this,ProgrammaShowActivity.class);
            i.putExtra("file", destFile.getAbsolutePath().toString());
            i.putExtra("nomeP",p.nomeProgramma.toString());
            i.putExtra("autore",p.autore.toString());
            i.putExtra("username",user);
            startActivity(i);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(ElencoProgActivity.this,MenuActivity.class);
        i.putExtra("mail",user);
        startActivity(i);
        finish();
    }
    public boolean controllaConnessione()
    {
        ConnectivityManager cm = (ConnectivityManager) ElencoProgActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(ElencoProgActivity.this,"Nessuna connessione ad intenet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

