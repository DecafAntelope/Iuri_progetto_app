package com.Iuri.iuri_progetto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    RecyclerView comm;
    EditText txtComm;
    Button btnInvia;
    DatabaseReference riferimentoInvia,riferimentoComm;
    String nomeProg;
    ArrayList<Commento> list;
    AdapterCommento adattatore;
    String user;
    ArrayList<String> chiavi;
    FirebaseAuth riferimentoAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        list = new ArrayList<Commento>();
        chiavi = new ArrayList<String>();
        comm = (RecyclerView) findViewById(R.id.rclCommenti);
        comm.setLayoutManager(new LinearLayoutManager(this));
        nomeProg=getIntent().getStringExtra("nomeFile");
        user = getIntent().getStringExtra("username");
        if(controllaConnessione())
        {
            btnInvia = (Button) findViewById(R.id.btnInviaCommento);
            txtComm = (EditText) findViewById(R.id.editTextTextPersonName);
            riferimentoAuth = FirebaseAuth.getInstance();
            if(getIntent().hasExtra("programmashow"))
            {
                btnInvia.setVisibility(View.INVISIBLE);
                txtComm.setVisibility(View.INVISIBLE);
                btnInvia.setClickable(false);
                txtComm.setClickable(false);
            }
            else
            {
                btnInvia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(controllaConnessione())
                        {
                            riferimentoComm = FirebaseDatabase.getInstance().getReference().child("commenti").child(nomeProg);
                            Commento c= new Commento(user,txtComm.getText().toString(),nomeProg,riferimentoAuth.getCurrentUser().getUid().toString());
                            riferimentoComm.push().setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CommentActivity.this, "Commento aggiunto!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            txtComm.setText("");
                        }

                    }
                });
            }

            riferimentoComm = FirebaseDatabase.getInstance().getReference().child("commenti").child(nomeProg);
            riferimentoComm.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        if(!chiavi.contains(dataSnapshot.getKey()))
                        {
                            Commento c = dataSnapshot.getValue(Commento.class);
                            list.add(c);
                            chiavi.add(dataSnapshot.getKey());
                        }
                    }
                    for(int i=0;i<list.size();i++)
                    {
                        Log.d("list",list.get(i).testo);
                    }
                    adattatore = new AdapterCommento(list,CommentActivity.this);
                    comm.setAdapter(adattatore);
                    adattatore.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public boolean controllaConnessione()
    {
        ConnectivityManager cm = (ConnectivityManager) CommentActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(CommentActivity.this,"Nessuna connessione ad intenet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}