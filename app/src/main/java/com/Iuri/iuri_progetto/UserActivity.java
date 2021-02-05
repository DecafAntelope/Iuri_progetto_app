package com.Iuri.iuri_progetto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserActivity extends AppCompatActivity {

    FirebaseAuth riferimentoAuth;
    DatabaseReference riferimentoData;
    Button mail,pass,back,elim,dim;
    TextInputEditText txtmail,txtuser,pas1,pas2;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        user = getIntent().getStringExtra("username");
        riferimentoAuth = FirebaseAuth.getInstance();
        riferimentoData = FirebaseDatabase.getInstance().getReference().child("utenti");
        mail = (Button) findViewById(R.id.btnNuovaMail);
        pass = (Button) findViewById(R.id.btnNuovaPassword);
        back = (Button) findViewById(R.id.btnHome);
        elim = (Button) findViewById(R.id.btnEliminaUser);
        dim = (Button) findViewById(R.id.btnDimentica);
        SessionManager contr = new SessionManager(UserActivity.this);
        if(!contr.ottieniCredenzialiDaSessione().get(SessionManager.KEY_MAIL).equals(riferimentoAuth.getCurrentUser().getEmail()))
        {
            dim.setClickable(false);
            dim.setVisibility(View.INVISIBLE);
        }
        txtmail = (TextInputEditText) findViewById(R.id.NuovaMail);
        pas1 = (TextInputEditText) findViewById(R.id.VecchiaPassword);
        pas2 = (TextInputEditText) findViewById(R.id.NuovaPassword);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controllaConnessione())
                {
                    if(!txtmail.getText().toString().equals(""))
                    {
                        riferimentoAuth.getCurrentUser().verifyBeforeUpdateEmail(txtmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                /*riferimentoAuth.getCurrentUser().updateEmail(txtmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(UserActivity.this, "E-mail aggiornata con successo", Toast.LENGTH_SHORT).show();
                                            Log.d("verificato",String.valueOf(riferimentoAuth.getCurrentUser().isEmailVerified()));
                                        }
                                        else
                                        {
                                            Toast.makeText(UserActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });*/
                                    Toast.makeText(UserActivity.this, "task succesfull", Toast.LENGTH_SHORT).show();
                                    Log.d("verificato",String.valueOf(riferimentoAuth.getCurrentUser().isEmailVerified()));
                                }
                                else
                                {
                                    //Toast.makeText(UserActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    String errore = ((FirebaseAuthException)task.getException()).getErrorCode().toString();
                                    switch(errore)
                                    {
                                        case "ERROR_WEAK_PASSWORD":Toast.makeText(UserActivity.this,"ERRORE:Password troppo debole,deve essere lunga almeno 8 caratteri",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_OPERATION_NOT_ALLOWED":Toast.makeText(UserActivity.this, "ERRORE:Operazione non permessa", Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_INVALID_USER_TOKEN":Toast.makeText(UserActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_USER_NOT_FOUND":Toast.makeText(UserActivity.this,"ERRORE:utente sconosciuto",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_USER_TOKEN_EXPIRED":Toast.makeText(UserActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_USER_DISABLED":Toast.makeText(UserActivity.this,"ERRORE:utente disabilitato",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":Toast.makeText(UserActivity.this,"ERRORE:Credenziali già in uso in un'altro account",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_EMAIL_ALREADY_IN_USE":Toast.makeText(UserActivity.this,"ERRORE email già utilizzata da un altro account",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":Toast.makeText(UserActivity.this,"ERRORE l'account esiste già con altre credenziali",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_REQUIRES_RECENT_LOGIN":Toast.makeText(UserActivity.this,"ERRORE l'operazione necessita un login recente",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_USER_MISMATCH":Toast.makeText(UserActivity.this,"ERRORE le credenziali non corrispondono a quelle precedenti dell'utente",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_WRONG_PASSWORD":Toast.makeText(UserActivity.this,"ERRORE Password errata",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_INVALID_EMAIL":Toast.makeText(UserActivity.this,"ERRORE email non valida",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_INVALID_CREDENTIAL":Toast.makeText(UserActivity.this,"ERRORE credenziali non valide",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_CUSTOM_TOKEN_MISMATCH":Toast.makeText(UserActivity.this,"ERRORE il token non corrisponde a quello vecchio",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_INVALID_CUSTOM_TOKEN":Toast.makeText(UserActivity.this,"ERRRORE token invalido",Toast.LENGTH_SHORT).show();break;
                                        default :Toast.makeText(UserActivity.this,"ERRORE",Toast.LENGTH_SHORT).show();break;
                                    }
                                }

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(UserActivity.this, "Compila tutti i campi!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controllaConnessione())
                {
                    if(!pas1.getText().toString().equals("") && !pas2.getText().toString().equals(""))
                    {
                        if(!pas1.getText().toString().equals(pas2.getText().toString()))
                        {
                            FirebaseUser user = riferimentoAuth.getCurrentUser();
                            AuthCredential credenziali = EmailAuthProvider.getCredential(user.getEmail(),pas1.getText().toString());
                            user.reauthenticate(credenziali).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        riferimentoAuth.getCurrentUser().updatePassword(pas2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(UserActivity.this, "Password aggiornata con successo", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else
                                    {
                                        String errore = ((FirebaseAuthException)task.getException()).getErrorCode().toString();
                                        switch(errore)
                                        {
                                            case "ERROR_WEAK_PASSWORD":Toast.makeText(UserActivity.this,"ERRORE:Password troppo debole,deve essere lunga almeno 8 caratteri",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_OPERATION_NOT_ALLOWED":Toast.makeText(UserActivity.this, "ERRORE:Operazione non permessa", Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_INVALID_USER_TOKEN":Toast.makeText(UserActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_USER_NOT_FOUND":Toast.makeText(UserActivity.this,"ERRORE:utente sconosciuto",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_USER_TOKEN_EXPIRED":Toast.makeText(UserActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_USER_DISABLED":Toast.makeText(UserActivity.this,"ERRORE:utente disabilitato",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_CREDENTIAL_ALREADY_IN_USE":Toast.makeText(UserActivity.this,"ERRORE:Credenziali già in uso in un'altro account",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_EMAIL_ALREADY_IN_USE":Toast.makeText(UserActivity.this,"ERRORE email già utilizzata da un altro account",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":Toast.makeText(UserActivity.this,"ERRORE l'account esiste già con altre credenziali",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_REQUIRES_RECENT_LOGIN":Toast.makeText(UserActivity.this,"ERRORE l'operazione necessita un login recente",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_USER_MISMATCH":Toast.makeText(UserActivity.this,"ERRORE le credenziali non corrispondono a quelle precedenti dell'utente",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_WRONG_PASSWORD":Toast.makeText(UserActivity.this,"ERRORE Password errata",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_INVALID_EMAIL":Toast.makeText(UserActivity.this,"ERRORE email non valida",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_INVALID_CREDENTIAL":Toast.makeText(UserActivity.this,"ERRORE credenziali non valide",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_CUSTOM_TOKEN_MISMATCH":Toast.makeText(UserActivity.this,"ERRORE il token non corrisponde a quello vecchio",Toast.LENGTH_SHORT).show();break;
                                            case "ERROR_INVALID_CUSTOM_TOKEN":Toast.makeText(UserActivity.this,"ERRRORE token invalido",Toast.LENGTH_SHORT).show();break;
                                            default :Toast.makeText(UserActivity.this,"ERRORE",Toast.LENGTH_SHORT).show();break;
                                        }
                                    }
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(UserActivity.this, "Le due password non possono essere uguali!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(UserActivity.this, "Compila tutti i campi!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserActivity.this);
                dialog.setTitle("Ssei sicuro?")
                        .setMessage("Cancellando l'account questo verrà eliminato completamente e non potrai più accedere all'app con esso")
                        .setPositiveButton("elimina", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String mail = riferimentoAuth.getCurrentUser().getEmail();
                                riferimentoAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            SessionManager sessionManager = new SessionManager(UserActivity.this);
                                            HashMap<String,String> dati = new HashMap<String,String>();
                                            dati = sessionManager.ottieniCredenzialiDaSessione();
                                            Log.d("mail",dati.get(SessionManager.KEY_MAIL));
                                            if(mail.equals(dati.get(SessionManager.KEY_MAIL)))
                                            {
                                                sessionManager.CreaRicordamiCredenziali("","");
                                            }
                                            Toast.makeText(UserActivity.this,"utente eliminato",Toast.LENGTH_SHORT);
                                            Intent i = new Intent(UserActivity.this,LoginActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(UserActivity.this, "ERRORE:non è satto possibile eliminare l'account", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton("esci", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();

            }
        });
        dim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(UserActivity.this);
                sessionManager.CreaRicordamiCredenziali("","");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UserActivity.this,MenuActivity.class);
        i.putExtra("mail",user);
        startActivity(i);
        finish();
    }

    public boolean controllaConnessione()
    {
        ConnectivityManager cm = (ConnectivityManager) UserActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(UserActivity.this,"Nessuna connessione ad intenet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}