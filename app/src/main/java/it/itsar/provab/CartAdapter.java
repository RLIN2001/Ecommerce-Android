package it.itsar.provab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
   private Context context;
   private String documentId;
    private ArrayList<Cart> prod;



    public CartAdapter(ArrayList<Cart> list, String documentId, Carrello carrello) {
        context=carrello;
        prod=list;
        this.documentId=documentId;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        int pos=position;

        holder.bind(prod.get(position));



        holder.add.setOnClickListener(view -> {



            holder.quantita.setText(""+(Integer.parseInt(holder.quantita.getText().toString())+1));


            db.collection("utenti").document(documentId)
                    .collection("carrello")
                    .document(prod.get(pos).getId())
                    .update("quantita",(Integer.parseInt( holder.quantita.getText().toString())))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e("messaggio","aggiunto");
                            prod.get(pos).setQuantita(Integer.parseInt(holder.quantita.getText().toString()));
                            Carrello.updateTextView(prod);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("messaggio","errore"+ e);

                        }
                    });





        });

        holder.remove.setOnClickListener(view -> {


            if(holder.quantita.getText().equals("1"))
            {


                    db.collection("utenti").document(documentId)
                            .collection("carrello")
                            .document(prod.get(pos).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    prod.remove(pos);
                                    notifyDataSetChanged();
                                    Carrello.updateTextView(prod);

                                    if(getItemCount()==0) {
                                        Carrello.emptytextView();


                                        new AlertDialog.Builder(context)
                                                .setTitle("Carrello vuoto")
                                                .setMessage("Non esistono prodotti nel carrello, clicca su torna per tornare alla home")
                                                .setPositiveButton("Torna", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent=new Intent(context,Home.class);
                                                        holder.itemView.getContext().startActivity(intent);
                                                        ((Carrello)context).finish();
                                                    }
                                                })
                                                .setIcon(R.drawable.ic_baseline_shopping_cart_24)
                                                .show();
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("errore ", "errore" + e);

                                }
                            });
                }
            else {
                holder.quantita.setText("" + (Integer.parseInt(holder.quantita.getText().toString()) - 1));

                db.collection("utenti").document(documentId)
                        .collection("carrello")
                        .document(prod.get(pos).getId())
                        .update("quantita", (Integer.parseInt(holder.quantita.getText().toString())))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.e("messaggio", "rimosso");


                                prod.get(pos).setQuantita(Integer.parseInt(holder.quantita.getText().toString()));
                                Carrello.updateTextView(prod);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("messaggio", "errore" + e);

                            }
                        });

            }
        });

    }
    @Override
    public int getItemCount() {
        return prod.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView nome,prezzo,quantita;
        private Button add, remove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.immagine);
            nome=itemView.findViewById(R.id.nomeP);
            prezzo=itemView.findViewById(R.id.prezzoP);
            quantita=itemView.findViewById(R.id.quantita);
            add=itemView.findViewById(R.id.piu);
            remove=itemView.findViewById(R.id.meno);

        }
        public void bind(Cart cart) {
            nome.setText(cart.getNome());
            prezzo.setText(""+cart.getPrezzo()+"€");
            quantita.setText(""+cart.getQuantita());
            img.setImageResource(cart.getImmagine());
        }
    }


}