package com.Iuri.iuri_progetto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class ProgrammaShowActivity extends AppCompatActivity implements DialogReport.DialogReportListener {

    String filePath,user;
    ArrayList<Esercizio> arrayEsercizi;
    RecyclerView esercizi;
    TextView nomeProg,nomeAut;
    AdapterEsercizio adattatore;
    FloatingActionButton btnAgg,btnComm,btnSeg;
    Animation apri,chiudi,alza,abbassa;
    FirebaseAuth riferimentoAuth;
    DatabaseReference riferimentoData;
    boolean cliccato = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programma_show);
        riferimentoAuth = FirebaseAuth.getInstance();
        riferimentoData = FirebaseDatabase.getInstance().getReference().child("segnalazioni");
        Intent i = getIntent();
        filePath=i.getStringExtra("file");
        nomeProg=(TextView)findViewById(R.id.tNomeScheda);
        nomeAut=(TextView)findViewById(R.id.tAutore);
        nomeProg.setText(i.getStringExtra("nomeP"));
        nomeAut.setText(i.getStringExtra("autore"));
        esercizi = (RecyclerView) findViewById(R.id.rclEserciziShow);
        esercizi.setLayoutManager(new LinearLayoutManager(this));
        arrayEsercizi = new ArrayList<Esercizio>();
        btnAgg = (FloatingActionButton) findViewById(R.id.fbtnAdd);
        btnComm = (FloatingActionButton) findViewById(R.id.fbtnComm);
        btnSeg = (FloatingActionButton) findViewById(R.id.fbtnReport);
        if(i.hasExtra("offline"))
        {
            btnAgg.setClickable(false);
            btnComm.setClickable(false);
            btnSeg.setClickable(false);
            btnAgg.setVisibility(View.INVISIBLE);
            btnAgg.setVisibility(View.INVISIBLE);
            btnAgg.setVisibility(View.INVISIBLE);
        }
        else
        {
            user = i.getStringExtra("username");
            apri = AnimationUtils.loadAnimation(this,R.anim.apri_rotazione_anim);
            chiudi = AnimationUtils.loadAnimation(this,R.anim.chiudi_rotazione_anim);
            alza = AnimationUtils.loadAnimation(this,R.anim.da_basso_anim);
            abbassa = AnimationUtils.loadAnimation(this,R.anim.da_alto_anim);


            btnAgg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!cliccato)
                    {
                        btnComm.setVisibility(View.VISIBLE);
                        btnSeg.setVisibility(View.VISIBLE);
                        btnComm.startAnimation(alza);
                        btnSeg.startAnimation(alza);
                        btnAgg.startAnimation(apri);
                        btnComm.setClickable(true);
                        btnSeg.setClickable(true);
                    }
                    else
                    {
                        btnComm.setVisibility(View.INVISIBLE);
                        btnSeg.setVisibility(View.INVISIBLE);
                        btnComm.startAnimation(abbassa);
                        btnSeg.startAnimation(abbassa);
                        btnAgg.startAnimation(chiudi);
                        btnComm.setClickable(false);
                        btnSeg.setClickable(false);
                    }
                    cliccato =!cliccato;
                }
            });

            btnComm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(controllaConnessione()){
                        Intent i = new Intent(ProgrammaShowActivity.this,CommentActivity.class);
                        i.putExtra("nomeFile",nomeProg.getText()+"_"+nomeAut.getText());
                        i.putExtra("username",user);
                        startActivity(i);
                    }

                }
            });
            btnSeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(controllaConnessione())
                    {
                        DialogReport dialog = new DialogReport(riferimentoAuth.getCurrentUser().getUid());
                        //dialog.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show(getSupportFragmentManager(),"alert dialog");
                    }

                }
            });
        }
        leggiDaFile();

        Log.d("intent file",filePath);




    }

    public boolean controllaConnessione()
    {
        ConnectivityManager cm = (ConnectivityManager) ProgrammaShowActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(ProgrammaShowActivity.this,"Nessuna connessione ad intenet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void leggiDaFile()
    {
        File file = new File(getExternalFilesDir(null).toString()+File.separator+nomeProg.getText()+"_"+nomeAut.getText()+".txt");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));

            Log.d("avviso","Fin qua ci sono");
            while(true)
            {
                Log.d("while","eeeeee");
                try {
                    Esercizio es = new Esercizio();
                    es.nome = in.readLine();
                    Log.d("provalet",es.nome);
                    es.minuti=Integer.parseInt(in.readLine());
                    Log.d("serie",String.valueOf(es.minuti));
                    es.secondi=Integer.parseInt(in.readLine());
                    Log.d("ripetizioni",String.valueOf(es.secondi));
                    es.ripetizioni=Integer.parseInt(in.readLine());
                    Log.d("minuti",String.valueOf(es.ripetizioni));
                    es.serie=Integer.parseInt(in.readLine());
                    Log.d("secondi",String.valueOf(es.serie));
                    arrayEsercizi.add(es);
                } catch (IOException e) {
                    break;
                }
                catch(Exception e)
                {
                    Log.d("Exception e","eccezione a cazzo");
                    e.printStackTrace();
                    break;
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("porvone",arrayEsercizi.get(0).nome);
        adattatore = new AdapterEsercizio(ProgrammaShowActivity.this,arrayEsercizi);
        esercizi.setAdapter(adattatore);
        adattatore.notifyDataSetChanged();
        /*try {
            FileInputStream fin = new FileInputStream(file);//openFileInput(nomeProg.getText()+"_"+nomeAut.getText()+".txt");
            InputStreamReader in = new InputStreamReader(fin);
            BufferedReader bin = new BufferedReader(in);
            while(true)
            {
                Log.d("while","eeeeee");
                try {
                    Esercizio es= new Esercizio();
                    es.nome = bin.readLine();
                    Log.d("provalet",es.nome);
                    es.serie=Integer.parseInt(bin.readLine());
                    Log.d("serie",String.valueOf(es.serie));
                    es.ripetizioni=Integer.parseInt(bin.readLine());
                    Log.d("ripetizioni",String.valueOf(es.ripetizioni));
                    es.minuti=Integer.parseInt(bin.readLine());
                    Log.d("minuti",String.valueOf(es.minuti));
                    es.secondi=Integer.parseInt(bin.readLine());
                    Log.d("secondi",String.valueOf(es.secondi));
                    arrayEsercizi.add(es);
                } catch (IOException e) {
                    break;
                }
                catch(Exception e)
                {
                    Log.d("Exception e","eccezione a cazzo");
                    e.printStackTrace();
                    break;
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        /*File file = new File(getExternalFilesDir(null).toString()+File.separator+"ciao.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter out = new OutputStreamWriter(fout);
        try {
            out.write("ciao");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream fin = null;//openFileInput(nomeProg.getText()+"_"+nomeAut.getText()+".txt");
        try {
            fin = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader in = new InputStreamReader(fin);
        BufferedReader bin = new BufferedReader(in);
        try {
            Log.d("test",bin.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(ProgrammaShowActivity.this,ElencoProgActivity.class);
        startActivity(i);
        finish();*/
        super.onBackPressed();
        finish();
    }

    @Override
    public void Report(segnalazione s) {
        riferimentoData.child(nomeProg.getText().toString()+"_"+nomeAut.getText().toString()).push().setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ProgrammaShowActivity.this, "Segnaazione Inviata!! Grazie del tuo aiuto!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}