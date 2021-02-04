package com.Iuri.iuri_progetto;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    Context context;
    SharedPreferences sessioneUtente;
    SharedPreferences.Editor editor;

    public static final String KEY_MAIL = "mail";
    public static final String KEY_PASSWORD = "password";
    private static final String IS_RICORDAMI = "IsRicordami";
    public SessionManager(Context c)
    {
        context = c;
        sessioneUtente = context.getSharedPreferences("SessionericordaCredenziali",Context.MODE_PRIVATE);
        editor = sessioneUtente.edit();
    }

    public void CreaRicordamiCredenziali(String mail,String password)
    {
        editor.putBoolean(IS_RICORDAMI,true);
        editor.putString(KEY_MAIL,mail);
        editor.putString(KEY_PASSWORD,password);

        editor.commit();
    }

    public HashMap<String,String> ottieniCredenzialiDaSessione()
    {
        HashMap<String,String> dati = new HashMap<String,String>();

        dati.put(KEY_MAIL,sessioneUtente.getString(KEY_MAIL,null));
        dati.put(KEY_PASSWORD,sessioneUtente.getString(KEY_PASSWORD,null));

        return dati;
    }

    public boolean controlloRicordami()
    {
        if(sessioneUtente.getBoolean(IS_RICORDAMI,false))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
