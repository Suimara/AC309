package br.com.sabedoriajedi;

import java.io.Serializable;

public class Jedi implements Serializable {
    private String Uid;
    private String nome;
    private String lado;
    private String mestre;
    private String lutou;

    public Jedi() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLado() {
        return lado;
    }

    public void setLado(String lado) {
        this.lado = lado;
    }

    public String getMestre() {
        return mestre;
    }

    public void setMestre(String mestre) {
        this.mestre = mestre;
    }

    public String getLutou() {
        return lutou;
    }

    public void setLutou(String lutou) {
        this.lutou = lutou;
    }
}
