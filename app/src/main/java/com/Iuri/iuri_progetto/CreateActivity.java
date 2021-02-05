package com.Iuri.iuri_progetto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity implements DialogProgramma.dialogProgrammaListener,DialogModifica.dialogModificaListener, PopupMenu.OnMenuItemClickListener {
    private static final int PICK_FILE = 1;
    ListView lstEsercizi;
    String utente;
    TextInputEditText esercizio,ripetizioni,serie,minuti,secondi;
    ArrayList<Esercizio> programma;
    Button btnSalvaEs,btnSalvaFile;
    //ArrayAdapter adattatore;
    CustomAdapter custAdatt;
    String nomeProgramma="";
    DatabaseReference riferimento;
    FirebaseAuth riferimentoAuth;
    int posizioneModifica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utente = getIntent().getStringExtra("username");
        //Toast.makeText(getApplicationContext(),utente,Toast.LENGTH_LONG).show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create);
        riferimentoAuth = FirebaseAuth.getInstance();
        lstEsercizi=(ListView) findViewById(R.id.lstEsercizi);
        esercizio=(TextInputEditText) findViewById(R.id.txtEsercizio);
        ripetizioni=(TextInputEditText)findViewById(R.id.txtRipetizioni);
        serie=(TextInputEditText)findViewById(R.id.txtSerie);
        minuti=(TextInputEditText)findViewById(R.id.txtMinuti);
        secondi=(TextInputEditText)findViewById(R.id.txtSecondi);
        riferimento = FirebaseDatabase.getInstance().getReference().child("programmi");
        programma=new ArrayList<Esercizio>();
        btnSalvaEs=(Button)findViewById(R.id.btnSalva);
        //adattatore = new ArrayAdapter(this,android.R.layout.simple_list_item_1,programma);
        custAdatt = new CustomAdapter(CreateActivity.this,programma);
        lstEsercizi.setAdapter(custAdatt);
        btnSalvaEs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!esercizio.getText().toString().equals("") && !ripetizioni.getText().toString().equals("") && !serie.getText().toString().equals("") && !minuti.getText().toString().equals("") && !secondi.getText().toString().equals(""))
                {
                    int rip=Integer.parseInt(ripetizioni.getText().toString()),min=Integer.parseInt(minuti.getText().toString()),sec=Integer.parseInt(secondi.getText().toString()),ser=Integer.parseInt(serie.getText().toString());
                    if(rip>0 && ser>0 && (sec>-1 && sec<60) && (min>-1 && min<60)){
                        Esercizio e = new Esercizio(esercizio.getText().toString(),min,sec,rip,ser);
                        programma.add(e);
                        //adattatore.notifyDataSetChanged();
                        custAdatt = new CustomAdapter(CreateActivity.this,programma);
                        lstEsercizi.setAdapter(custAdatt);
                        custAdatt.notifyDataSetChanged();
                        esercizio.setText("");
                        ripetizioni.setText("");
                        serie.setText("");
                        minuti.setText("");
                        secondi.setText("");
                    }


                }
            }
        });
        btnSalvaFile = (Button) findViewById(R.id.btnSalvaFile);
        btnSalvaFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(controllaConnessione())
                {
                    if(!programma.isEmpty()){
                        DialogProgramma dialog = new DialogProgramma();
                        dialog.show(getSupportFragmentManager(),"dialog programma");
                    }
                    else
                    {
                        Toast.makeText(CreateActivity.this, "Devi inserire almeno un esercizio!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });
        lstEsercizi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //AlertDialog dialog = new AlertDialog();
                posizioneModifica = position;
                Log.d("ciao",String.valueOf(lstEsercizi.getChildCount()));
                showPopup(view);
            }
        });
    }

    public boolean controllaConnessione()
    {
        ConnectivityManager cm = (ConnectivityManager) CreateActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(CreateActivity.this,"Nessuna connessione ad intenet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    @Override
    public void applyTexts(String txtprogramma){
        nomeProgramma = (txtprogramma+"_"+utente);
        riferimento.child(nomeProgramma).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.getResult().exists())
                {
                    //nomeProgramma.concat(".txt");
                    Log.d("file",nomeProgramma);
                    nomeProgramma = nomeProgramma+".txt";
                    //Toast.makeText(getApplicationContext(),nomeProgramma, Toast.LENGTH_LONG).show();
                    FileOutputStream fout = null;
                    File file = new File(getExternalFilesDir(null).toString()+File.separator+nomeProgramma);
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        fout = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    OutputStreamWriter writer = new OutputStreamWriter(fout);
                    for(int i=0;i<programma.size();i++)
                    {
                        try {
                            writer.write(programma.get(i).nome+"\n");
                            writer.write(String.valueOf(programma.get(i).minuti)+"\n");
                            writer.write(String.valueOf(programma.get(i).secondi)+"\n");
                            writer.write(String.valueOf(programma.get(i).ripetizioni)+"\n");
                            writer.write(String.valueOf(programma.get(i).serie)+"\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        writer.close();
                        fout.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Programma p = new Programma(txtprogramma,utente);
                    StorageReference Folder = FirebaseStorage.getInstance().getReference().child("Programmi");

                    StorageReference nomeFile = Folder.child(p.nomeProgramma+"_"+p.autore);
                    Uri FileUrl = Uri.fromFile(file);
                    nomeFile.putFile(FileUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                /*nomeFile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                    @Override
                    public void onSuccess(Uri uri) {
                        //p.setUrl(String.valueOf(uri));
                        riferimento.child(p.nomeProgramma+"_"+p.autore).setValue(p);
                    }
                });*/
                            p.setUid(riferimentoAuth.getCurrentUser().getUid());
                            riferimento.child(p.nomeProgramma+"_"+p.autore).setValue(p);
                        }
                    });
                    Toast.makeText(CreateActivity.this, "Programma Creato Correttamente", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CreateActivity.this,MenuActivity.class);
                    i.putExtra("mail",utente);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(CreateActivity.this,"Nome non utilizzabile",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void applicaTestoModifica(Esercizio e) {
        programma.set(posizioneModifica,e);
        custAdatt = new CustomAdapter(CreateActivity.this,programma);
        lstEsercizi.setAdapter(custAdatt);
        custAdatt.notifyDataSetChanged();
    }

    public void showPopup(View view)
    {
        PopupMenu popup = new PopupMenu(this,view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_esercizio,popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.optModifica)
        {
            DialogModifica dialog = new DialogModifica(programma.get(posizioneModifica));
            dialog.show(getSupportFragmentManager(),"dialog Modifica");
        }
        else if(item.getItemId() == R.id.optElimina)
        {
            programma.remove(posizioneModifica);
        }
        custAdatt = new CustomAdapter(CreateActivity.this,programma);
        lstEsercizi.setAdapter(custAdatt);
        custAdatt.notifyDataSetChanged();
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateActivity.this,MenuActivity.class);
        i.putExtra("mail",utente);
        startActivity(i);
        finish();
    }
}