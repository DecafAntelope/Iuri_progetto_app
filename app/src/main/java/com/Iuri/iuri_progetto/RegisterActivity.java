package com.Iuri.iuri_progetto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText user,pass,mail;
    Button registrati;
    DatabaseReference riferimentoData;
    FirebaseAuth riferimentoAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        user = (TextInputEditText) findViewById(R.id.txtUserReg);
        pass = (TextInputEditText) findViewById(R.id.txtPassReg);
        mail = (TextInputEditText) findViewById(R.id.txtMailReg);
        //risp = (TextInputEditText) findViewById(R.id.txtRispReg);
        registrati = (Button) findViewById(R.id.btnReg);
        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String password = pass.getText().toString();
                String email = mail.getText().toString();
                //String risposta = risp.getText().toString();
                riferimentoData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://prova-6189f-default-rtdb.firebaseio.com/utenti");
                riferimentoAuth = FirebaseAuth.getInstance();
                if(controllaConnessione())
                {
                    if(!username.isEmpty() && !username.isEmpty() && !email.isEmpty())
                    {
                        riferimentoAuth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            if(task.isSuccessful())
                                            {
                                                String uid = riferimentoAuth.getCurrentUser().getUid().toString();
                                                riferimentoAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Utente ut = new Utente(username);//Utente(username,email);
                                                            riferimentoData.child(uid).setValue(ut);
                                                            Toast.makeText(RegisterActivity.this,"è stata inviata una mail per la conferma all'indirizzo specificato",Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(RegisterActivity.this, "ERRORE:non è stato possibile inviare la mail di verifica!!", Toast.LENGTH_SHORT).show();
                                                        }

                                                        /*riferimentoData.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                riferimentoData.child(riferimentoAuth.getCurrentUser().getUid()).setValue(ut);
                                                                Log.d("tutto","bene");

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });*/

                                                    }
                                                });


                                            }

                                        }
                                        else {
                                            //Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            String errore = ((FirebaseAuthException)task.getException()).getErrorCode().toString();
                                            switch(errore)
                                            {
                                                case "ERROR_WEAK_PASSWORD":Toast.makeText(RegisterActivity.this,"ERRORE:Password troppo debole,deve essere lunga almeno 8 caratteri",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_OPERATION_NOT_ALLOWED":Toast.makeText(RegisterActivity.this, "ERRORE:Operazione non permessa", Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_INVALID_USER_TOKEN":Toast.makeText(RegisterActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_USER_NOT_FOUND":Toast.makeText(RegisterActivity.this,"ERRORE:utente sconosciuto",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_USER_TOKEN_EXPIRED":Toast.makeText(RegisterActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_USER_DISABLED":Toast.makeText(RegisterActivity.this,"ERRORE:utente disabilitato",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":Toast.makeText(RegisterActivity.this,"ERRORE:Credenziali già in uso in un'altro account",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_EMAIL_ALREADY_IN_USE":Toast.makeText(RegisterActivity.this,"ERRORE email già utilizzata da un altro account",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":Toast.makeText(RegisterActivity.this,"ERRORE l'account esiste già con altre credenziali",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_REQUIRES_RECENT_LOGIN":Toast.makeText(RegisterActivity.this,"ERRORE l'operazione necessita un login recente",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_USER_MISMATCH":Toast.makeText(RegisterActivity.this,"ERRORE le credenziali non corrispondono a quelle precedenti dell'utente",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_WRONG_PASSWORD":Toast.makeText(RegisterActivity.this,"ERRORE Password errata",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_INVALID_EMAIL":Toast.makeText(RegisterActivity.this,"ERRORE email non valida",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_INVALID_CREDENTIAL":Toast.makeText(RegisterActivity.this,"ERRORE credenziali non valide",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_CUSTOM_TOKEN_MISMATCH":Toast.makeText(RegisterActivity.this,"ERRORE il token non corrisponde a quello vecchio",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_INVALID_CUSTOM_TOKEN":Toast.makeText(RegisterActivity.this,"ERRRORE token invalido",Toast.LENGTH_SHORT).show();break;
                                                default :Toast.makeText(RegisterActivity.this,"ERRORE",Toast.LENGTH_SHORT).show();break;
                                            }
                                        }
                                    }
                                });
                /*riferimento = FirebaseDatabase.getInstance().getReferenceFromUrl("https://prova-6189f-default-rtdb.firebaseio.com/utenti");
                    Utente ut = //new Utente(username,password,domanda,risposta);
                    riferimento.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.child(username).exists())
                            {
                                riferimento.child(username).setValue(ut);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Esiste già un utente con quel nome",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/

                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"Compila tutti i campi!",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }

    public boolean controllaConnessione()
    {
        ConnectivityManager cm = (ConnectivityManager) RegisterActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(RegisterActivity.this,"Nessuna connessione ad intenet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}