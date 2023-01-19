package it.itsar.provab;

public class Utente {
    int id;
    String username,password;
    String documentId;

    public Utente(int id, String username, String password, String documentId) {
        this.id = id;
        this.username = username;
        this.password=password;
        this.documentId=documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
