package it.itsar.provab;

public class Cart {
    private String nome;
    private double prezzo;
    private int quantita;
    private int immagine;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cart(String key,String nome, double prezzo, int quantita, int immagine) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.immagine = immagine;
        this.id=key;
    }

    public int getImmagine() {
        return immagine;
    }

    public void setImmagine(int immagine) {
        this.immagine = immagine;
    }

    public Cart() {

    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}
