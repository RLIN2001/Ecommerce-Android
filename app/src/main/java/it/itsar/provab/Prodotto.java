package it.itsar.provab;


import java.io.Serializable;

public class Prodotto implements Serializable {
    private String id,nome,descrizione,categoria;
    private int foto;
    private double prezzo;

    public Prodotto(String id, String nome, String descrizione, String categoria, int foto, double prezzo) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.foto = foto;
        this.prezzo = prezzo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }
}
