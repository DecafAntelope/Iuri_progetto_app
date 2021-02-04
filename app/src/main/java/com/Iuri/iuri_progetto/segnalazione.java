package com.Iuri.iuri_progetto;

public class segnalazione {
    String motivo,uid;

    public segnalazione() {

    }

    public segnalazione(String motivo,String uid) {
        this.motivo = motivo;
        this.uid = uid;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
