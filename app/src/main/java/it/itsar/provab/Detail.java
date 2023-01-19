package it.itsar.provab;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Detail extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView titolo, prezzo, descrizione, quantita,back;
    ImageView imageView, plus, minus;
    Button buttonA;
    SessionManager sessionManagement;

    String key;
    Boolean found=false;
    private int qnty = 1;
    private long quan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        getSupportActionBar().hide();

        back=findViewById(R.id.back);
        titolo = findViewById(R.id.nomeP);
        prezzo = findViewById(R.id.prezzo);
        descrizione = findViewById(R.id.descrizione);
        imageView = findViewById(R.id.immagine);
        plus = findViewById(R.id.piu);
        minus = findViewById(R.id.meno);
        quantita = findViewById(R.id.quantita);
        buttonA = findViewById(R.id.btmAggiungi);

        Prodotto prodotto = (Prodotto) getIntent().getSerializableExtra("prodotto");



        setInfoProdotto(prodotto);

        sessionManagement = new SessionManager(this);

        plus.setOnClickListener(view -> {
            qnty++;
            quantita.setText("" + qnty);
        });


        back.setOnClickListener(view -> {
            finish();
        });

        minus.setOnClickListener(view -> {
            if (qnty > 1) {
                qnty--;
                quantita.setText("" + qnty);
            } else
                Toast.makeText(this, "Quantità non può essere inferiore a 1 per poterlo inserire al carrello", Toast.LENGTH_SHORT).show();
        });

        buttonA.setOnClickListener(view -> {
            if(checkLogin()==false){
               alertNotLogin();

            }
            else {

                sessionManagement = new SessionManager(this);
                String documentId=sessionManagement.sharedPreferences.getString("documentId","");



                db.collection("utenti").document(documentId)
                        .collection("carrello")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getData().get("nome").equals(prodotto.getNome())){
                                               trovaProdotto(document);
                                                break;

                                            }
                                    }

                                    if(found==true){
                                      aggiornaCarrello(documentId);
                                    }
                                    else{
                                        aggiungiProdotto(prodotto,documentId);

                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });

            }




        });
    }







    private void aggiungiProdotto(Prodotto prodotto, String documentId) {

        Map<String, Object> dati = new HashMap<>();
        dati.put("nome",prodotto.getNome());
        dati.put("prezzo",prodotto.getPrezzo());
        dati.put("quantita",qnty);
        dati.put("immagine",prodotto.getFoto());


        db.collection("utenti").document(documentId)
                .collection("carrello")
                .add(dati)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }






    private void aggiornaCarrello(String documentId) {

        db.collection("utenti").document(documentId)
                .collection("carrello")
                .document(key)
                .update("quantita",((int)quan+qnty))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("messaggio","aggiornato");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("messaggio","errore"+ e);

                    }
                });
    }





    private void trovaProdotto(QueryDocumentSnapshot document) {
        found=true;
        quan=(long) document.getData().get("quantita");
        key=(String) document.getId();
    }






    private void alertNotLogin() {
        new AlertDialog.Builder(this)
                .setTitle("Attenzione")
                .setMessage("Non sei loggato, premi ok per passare al login")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent=new Intent(Detail.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }






    private void setInfoProdotto(Prodotto prodotto) {
        titolo.setText(prodotto.getNome());
        prezzo.setText("" + prodotto.getPrezzo() + "€");
        descrizione.setText("Descrizione:\n" + prodotto.getDescrizione());
        imageView.setImageResource(prodotto.getFoto());


    }


    private boolean checkLogin() {

        boolean stato;

        SessionManager sessionManagement = new SessionManager(this);
        int userID = sessionManagement.getSession();

        if(userID == -1)
            stato=false;
        else stato=true;

        return stato;

    }
}