package com.Iuri.iuri_progetto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecPassActivity extends AppCompatActivity {


                //ACTIVITY NON UTILIZZATA
    TextInputEditText user,dom,risp;
    Button btnRecupera;
    DatabaseReference riferimento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_pass);
        user = (TextInputEditText)findViewById(R.id.txtRecUser);
        dom = (TextInputEditText)findViewById(R.id.txtRecDom);
        risp = (TextInputEditText)findViewById(R.id.txtRecRisp);
        btnRecupera = (Button)findViewById(R.id.btnRecupera);
        btnRecupera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String domanda = dom.getText().toString();
                String risposta = risp.getText().toString();
                riferimento = FirebaseDatabase.getInstance().getReferenceFromUrl("https://prova-6189f-default-rtdb.firebaseio.com/utenti/"+username);

                riferimento.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String recdom =snapshot.child("domanda").getValue().toString();
                            String recrisp =snapshot.child("risposta").getValue().toString();
                            Log.d("domanda fire",recdom);
                            Log.d("risposta fire",recrisp);
                            Log.d("domanda text",domanda);
                            Log.d("risposta text",risposta);
                            Log.d("username",username);
                            if(recdom.equals(domanda) && recrisp.equals(risposta) )
                            {
                                Intent i = new Intent(RecPassActivity.this,LoginActivity.class);
                                i.putExtra("username",username);
                                i.putExtra("password",snapshot.child("password").getValue().toString());
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(RecPassActivity.this,"domanda o risposta errate",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(RecPassActivity.this,"utente inesistente",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}
