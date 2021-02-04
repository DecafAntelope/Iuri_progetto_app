package com.Iuri.iuri_progetto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin,btnRegistra,btnPassword,btnOffline;
    final int CODICE_PERMESSO_STORAGE=1;
    TextInputEditText ma,pw;
    DatabaseReference riferimentoData;
    String pwdb;
    FirebaseAuth riferimentoAuth;
    ImageView imm;
    CheckBox cboCredenziali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            //Toast.makeText(this, "Hai già concesso questo permesso", Toast.LENGTH_SHORT).show();
        }
        else
        {
            richiediPermessoStorage();
        }
        Intent i = getIntent();
        ma = (TextInputEditText) findViewById(R.id.username);
        pw = (TextInputEditText) findViewById(R.id.password);
        imm = (ImageView) findViewById(R.id.logo_login);
        cboCredenziali = (CheckBox) findViewById(R.id.cboCredenziali);
        SessionManager sessionManager = new SessionManager(LoginActivity.this);
        if(sessionManager.controlloRicordami())
        {
            HashMap<String,String> datiUtente = sessionManager.ottieniCredenzialiDaSessione();
            ma.setText(datiUtente.get(SessionManager.KEY_MAIL));
            pw.setText(datiUtente.get(SessionManager.KEY_PASSWORD));
        }
        if(i.hasExtra("username") && i.hasExtra("password"))
        {
            String user = i.getStringExtra("username");
            String pass = i.getStringExtra("password");
            Log.d("user login",user);
            Log.d("pass Login",pass);
            Log.d("user Text",ma.toString());
            Log.d("pass Text",pw.toString());
            ma.setText(user);
            pw.setText(pass);
        }

        riferimentoAuth = FirebaseAuth.getInstance();
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && controllaConnessione())
                {
                    if(!ma.getText().toString().equals("") && !pw.getText().toString().equals(""))
                    {
                        String mail = ma.getText().toString(),password = pw.getText().toString();

                        riferimentoAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    if(riferimentoAuth.getCurrentUser().isEmailVerified())
                                    {
                                        String uid = riferimentoAuth.getCurrentUser().getUid();
                                        riferimentoData= FirebaseDatabase.getInstance().getReference().child("utenti");
                                        riferimentoData.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        /*for(DataSnapshot dataSnapshot:snapshot.getChildren())
                                        {
                                            ut[0] = dataSnapshot.getValue(Utente.class);
                                            if(ut[0].mail.equals(mail))
                                            {
                                                break;
                                            }

                                        }*/
                                                Utente ut =snapshot.child(uid).getValue(Utente.class);
                                                Log.d("exists", String.valueOf(snapshot.child(uid).exists()));
                                                Log.d("user",ut.username);
                                                Pair coppia = new Pair<View,String>(imm,"logo");
                                                Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                                                i.putExtra("mail",ut.username);
                                                if(cboCredenziali.isChecked())
                                                {
                                                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                                                    sessionManager.CreaRicordamiCredenziali(mail,password);
                                                }
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                                                {
                                                    ActivityOptions opzioni = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,coppia);
                                                    startActivity(i,opzioni.toBundle());
                                                    finish();
                                                }
                                                else
                                                {
                                                    startActivity(i);
                                                    finish();
                                                }

                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this, "Devi verificare la tua e-mail per poter accedere", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    //Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    String errore = ((FirebaseAuthException)task.getException()).getErrorCode().toString();
                                    switch(errore)
                                    {
                                        case "ERROR_WEAK_PASSWORD":Toast.makeText(LoginActivity.this,"ERRORE:Password troppo debole,deve essere lunga almeno 8 caratteri",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_OPERATION_NOT_ALLOWED":Toast.makeText(LoginActivity.this, "ERRORE:Operazione non permessa", Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_INVALID_USER_TOKEN":Toast.makeText(LoginActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_USER_NOT_FOUND":Toast.makeText(LoginActivity.this,"ERRORE:utente sconosciuto",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_USER_TOKEN_EXPIRED":Toast.makeText(LoginActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_USER_DISABLED":Toast.makeText(LoginActivity.this,"ERRORE:utente disabilitato",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":Toast.makeText(LoginActivity.this,"ERRORE:Credenziali già in uso in un'altro account",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_EMAIL_ALREADY_IN_USE":Toast.makeText(LoginActivity.this,"ERRORE email già utilizzata da un altro account",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":Toast.makeText(LoginActivity.this,"ERRORE l'account esiste già con altre credenziali",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_REQUIRES_RECENT_LOGIN":Toast.makeText(LoginActivity.this,"ERRORE l'operazione necessita un login recente",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_USER_MISMATCH":Toast.makeText(LoginActivity.this,"ERRORE le credenziali non corrispondono a quelle precedenti dell'utente",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_WRONG_PASSWORD":Toast.makeText(LoginActivity.this,"ERRORE Password errata",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_INVALID_EMAIL":Toast.makeText(LoginActivity.this,"ERRORE email non valida",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_INVALID_CREDENTIAL":Toast.makeText(LoginActivity.this,"ERRORE credenziali non valide",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_CUSTOM_TOKEN_MISMATCH":Toast.makeText(LoginActivity.this,"ERRORE il token non corrisponde a quello vecchio",Toast.LENGTH_SHORT).show();break;
                                        case "ERROR_INVALID_CUSTOM_TOKEN":Toast.makeText(LoginActivity.this,"ERRRORE token invalido",Toast.LENGTH_SHORT).show();break;
                                        default :Toast.makeText(LoginActivity.this,"ERRORE",Toast.LENGTH_SHORT).show();break;
                                    }
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Compila i campi!!", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(LoginActivity.this, "Hai già concesso questo permesso", Toast.LENGTH_SHORT).show();
     
                }
                else
                {
                    richiediPermessoStorage();
                }

                /*if(!username.isEmpty() && !password.isEmpty())
                {
                    riferimento = FirebaseDatabase.getInstance().getReferenceFromUrl("https://prova-6189f-default-rtdb.firebaseio.com/utenti/"+username);
                    riferimento.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                pwdb = snapshot.child("password").getValue().toString();
                                if(pwdb.equals(password))
                                {
                                    Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                                    i.putExtra("username",username);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Password errata",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"l'utente non esiste", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }*/

            }
        });
        btnRegistra = (Button)findViewById(R.id.btnRegistrati);
        btnRegistra.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && controllaConnessione())
                {
                    //Toast.makeText(LoginActivity.this, "Hai già concesso questo permesso", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                    Pair coppia = new Pair<View,String>(imm,"logo");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                    {
                        ActivityOptions opzioni = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,coppia);
                        startActivity(i,opzioni.toBundle());
                        finish();
                    }
                    else
                    {
                        startActivity(i);
                        finish();
                    }
                }
                else
                {
                    richiediPermessoStorage();
                }
                

            }
        });
        btnPassword = (Button)findViewById(R.id.btnPasswordDimenticata);
        btnPassword.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && controllaConnessione())
                        {
                            //Toast.makeText(LoginActivity.this, "Hai già concesso questo permesso", Toast.LENGTH_SHORT).show();
                            if(!ma.getText().toString().equals(""))
                            {
                                riferimentoAuth.sendPasswordResetEmail(ma.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(LoginActivity.this, "Mail per cambiare password inviata correttamente", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            //Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            String errore = ((FirebaseAuthException)task.getException()).getErrorCode().toString();
                                            switch(errore)
                                            {
                                                case "ERROR_WEAK_PASSWORD":Toast.makeText(LoginActivity.this,"ERRORE:Password troppo debole,deve essere lunga almeno 8 caratteri",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_OPERATION_NOT_ALLOWED":Toast.makeText(LoginActivity.this, "ERRORE:Operazione non permessa", Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_INVALID_USER_TOKEN":Toast.makeText(LoginActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_USER_NOT_FOUND":Toast.makeText(LoginActivity.this,"ERRORE:utente sconosciuto",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_USER_TOKEN_EXPIRED":Toast.makeText(LoginActivity.this,"ERRORE:Le Credenziali dell'utente non sono più valide, deve rieffettuare il login",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_USER_DISABLED":Toast.makeText(LoginActivity.this,"ERRORE:utente disabilitato",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":Toast.makeText(LoginActivity.this,"ERRORE:Credenziali già in uso in un'altro account",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_EMAIL_ALREADY_IN_USE":Toast.makeText(LoginActivity.this,"ERRORE email già utilizzata da un altro account",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":Toast.makeText(LoginActivity.this,"ERRORE l'account esiste già con altre credenziali",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_REQUIRES_RECENT_LOGIN":Toast.makeText(LoginActivity.this,"ERRORE l'operazione necessita un login recente",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_USER_MISMATCH":Toast.makeText(LoginActivity.this,"ERRORE le credenziali non corrispondono a quelle precedenti dell'utente",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_WRONG_PASSWORD":Toast.makeText(LoginActivity.this,"ERRORE Password errata",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_INVALID_EMAIL":Toast.makeText(LoginActivity.this,"ERRORE email non valida",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_INVALID_CREDENTIAL":Toast.makeText(LoginActivity.this,"ERRORE credenziali non valide",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_CUSTOM_TOKEN_MISMATCH":Toast.makeText(LoginActivity.this,"ERRORE il token non corrisponde a quello vecchio",Toast.LENGTH_SHORT).show();break;
                                                case "ERROR_INVALID_CUSTOM_TOKEN":Toast.makeText(LoginActivity.this,"ERRRORE token invalido",Toast.LENGTH_SHORT).show();break;
                                                default :Toast.makeText(LoginActivity.this,"ERRORE",Toast.LENGTH_SHORT).show();break;
                                            }
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Devi compilare il campo mail per cambiare password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            richiediPermessoStorage();
                        }
                        /*Intent i=new Intent(getApplicationContext(),ElencoProgActivity.class);
                        startActivity(i);
                        finish();*/


                    }
        });
        btnOffline = (Button) findViewById(R.id.btnOffline);
        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    //Toast.makeText(LoginActivity.this, "Hai già concesso questo permesso", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,ElencoProgActivityOffline.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    richiediPermessoStorage();
                }

            }
        });
    }

    public boolean controllaConnessione()
    {
        ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(LoginActivity.this,"Nessuna connessione ad intenet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Vuoi uscire dall'applicazione?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

    private void richiediPermessoStorage() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Permesso Obbligatorio")
                    .setMessage("Questo permesso è necessario per gestire i programmi di allenamento nel tuo telefono")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODICE_PERMESSO_STORAGE);
                        }
                    })
                    .setNegativeButton("chiudi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
            .create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODICE_PERMESSO_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODICE_PERMESSO_STORAGE)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "Permesso concesso!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Permesso Negato!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}