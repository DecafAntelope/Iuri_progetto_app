package com.Iuri.iuri_progetto;

public class Commento {
    String autore,testo,programma,uid;

    public Commento() {}

    public Commento(String autore, String testo, String programma, String uid) {
        this.autore = autore;
        this.testo = testo;
        this.programma = programma;
        this.uid = uid;
    }

    public Commento(String autore, String testo, String programma) {
        this.autore = autore;
        this.testo = testo;
        this.programma = programma;
    }

    public Commento(String autore, String testo) {
        this.autore = autore;
        this.testo = testo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getProgramma() {
        return programma;
    }

    public void setProgramma(String programma) {
        this.programma = programma;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
