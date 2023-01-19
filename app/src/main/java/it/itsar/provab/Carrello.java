package it.itsar.provab;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Carrello extends AppCompatActivity{
    private RecyclerView recyclerView;
    CartAdapter adapter;
    static TextView totale;
    ArrayList<String> id=new ArrayList<>();
    double totCosto=0;
    private  ArrayList<Cart> list = new ArrayList<>();
    Button button;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_carrello);
        recyclerView=findViewById(R.id.recyclerview);
        totale=findViewById(R.id.totale);
        button=findViewById(R.id.checkout);



        SessionManager sessionManagement = new SessionManager(this);
        String documentId=sessionManagement.sharedPreferences.getString("documentId","");

        db.collection("utenti").document(documentId)
                .collection("carrello")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               aggiungiLista(document);
                            }




                            adapter=new CartAdapter(list,documentId,Carrello.this);
                            recyclerView.setAdapter(adapter);


                            controlItemAdapter();


                            for(int i=0;i< list.size();i++){
                                totCosto+=list.get(i).getPrezzo()*list.get(i).getQuantita();
                            }

                            totale.setText("Totale: "+totCosto+"€");

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        button.setOnClickListener(view -> {

            new AlertDialog.Builder(Carrello.this)
                    .setTitle("Conferma ordine")
                    .setMessage("Premi ok per confermare l'ordine")
                    .setPositiveButton("ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                          svuotaCarrello(documentId);

                        }
                    })
                    .setNegativeButton("annulla", null)
                    .setIcon(R.drawable.ic_baseline_shopping_cart_checkout_24)
                    .show();



        });



    }

    private void svuotaCarrello(String documentId) {
        for(int i=0;i<id.size();i++)
        {
            db.collection("utenti")
                    .document(documentId)
                    .collection("carrello")
                    .document(id.get(i))
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e("messaggio","documento rimosso");

                            successAlert();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("errore","errore "+e);

                        }
                    });


        }
    }

    private void successAlert() {
        new AlertDialog.Builder(Carrello.this)
                .setTitle("Ordine ricevuto")
                .setMessage("L'ordine è stato effettuato con successo")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Carrello.this, Home.class);
                        startActivity(intent);
                        finish();
                    }

                })
                .setIcon(R.drawable.ic_baseline_check_24)
                .show();

    }


    private void controlItemAdapter() {
        int size = adapter.getItemCount();
        if(size==0){
            new AlertDialog.Builder(Carrello.this)
                    .setTitle("Carrello vuoto")
                    .setMessage("Non esistono prodotti nel carrello, clicca su torna per tornare alla home")
                    .setPositiveButton("Torna", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(Carrello.this,Home.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setIcon(R.drawable.ic_baseline_shopping_cart_24)
                    .show();
        }
    }

    private void aggiungiLista(QueryDocumentSnapshot document) {

        String nome=(String) document.getData().get("nome");
        long quantita=(long) document.getData().get("quantita");
        double prezzo= (double)document.getData().get("prezzo");
        long immagine=(long)document.getData().get("immagine");
        String key=(String) document.getId();


        id.add(document.getId());
        list.add(new Cart(key,nome,prezzo,(int)quantita,(int)immagine));

    }








    public static void updateTextView(ArrayList<Cart> prod) {
        double totCosto=0;
        for(int i=0;i< prod.size();i++){

            totCosto+= (prod.get(i).getPrezzo()*prod.get(i).getQuantita());
        }

        totale.setText("Totale: "+totCosto+"€");


    }


    public static void emptytextView() {
        totale.setText("");

    }

}