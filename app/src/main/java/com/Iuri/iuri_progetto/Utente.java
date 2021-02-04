package com.Iuri.iuri_progetto;

public class Utente {
    String username;//,mail;

    public Utente() {
    }
    public Utente(String user)
    {
        username=user;
    }
   /* public Utente(String u,String m)
    {
        username = u;
        mail = m;
    }*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    /*public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }*/
}
