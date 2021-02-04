package com.Iuri.iuri_progetto;

public class Programma {
    String nomeProgramma;
    String autore;
    String uid;

    public Programma() {
    }

    public Programma(String nomeProgramma, String autore) {
        this.nomeProgramma = nomeProgramma;
        this.autore = autore;
    }

    public Programma(String nomeProgramma, String autore, String uid) {
        this.nomeProgramma = nomeProgramma;
        this.autore = autore;
        this.uid = uid;
    }

    public String getNomeProgramma() {
        return nomeProgramma;
    }

    public void setNomeProgramma(String nomeProgramma) {
        this.nomeProgramma = nomeProgramma;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
