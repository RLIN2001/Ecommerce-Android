package it.itsar.provab;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private ArrayList<Prodotto> prodottos;
    private ArrayList<String> quantita;
   private Context context;


    public CartAdapter(ArrayList<Prodotto> prodottos, ArrayList<String> quantita, Carrello carrello) {
        this.prodottos=prodottos;
        this.quantita=quantita;
        this.context=carrello;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        holder.bind(prodottos.get(position),quantita.get(position));



        ReadWriteFile readWriteFile=new ReadWriteFile();

        holder.add.setOnClickListener(view -> {
            holder.quantita.setText(""+(Integer.parseInt(holder.quantita.getText().toString())+1));

            try {
                String txt = readWriteFile.leggi("carrello",context.getFilesDir());

                txt = txt.replaceAll(prodottos.get(position).getId()+" "+quantita.get(position),prodottos.get(position).getId()+" "+(Integer.parseInt(quantita.get(position))+1));
                readWriteFile.sovrascrivi(context.getFilesDir(),txt);
                quantita.set(position,""+(Integer.parseInt(quantita.get(position))+1));


                Carrello.updateTextView(Integer.parseInt(quantita.get(position)),position);



            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        holder.remove.setOnClickListener(view -> {

            if(holder.quantita.getText().equals("1"))
            {


                try {
                    String txt = readWriteFile.leggi("carrello", context.getFilesDir());
                    txt = txt.replaceAll(prodottos.get(position).getId() + " " + quantita.get(position)+",", "");
                    readWriteFile.sovrascrivi(context.getFilesDir(), txt);


                    ((Carrello)context).recreate();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {

                holder.quantita.setText("" + (Integer.parseInt(holder.quantita.getText().toString()) - 1));

                try {
                    String txt = readWriteFile.leggi("carrello", context.getFilesDir());
                    txt = txt.replaceAll(prodottos.get(position).getId() + " " + quantita.get(position), prodottos.get(position).getId() + " " + (Integer.parseInt(quantita.get(position)) - 1));
                    readWriteFile.sovrascrivi(context.getFilesDir(), txt);
                    quantita.set(position, "" + (Integer.parseInt(quantita.get(position)) - 1));

                   Carrello.updateTextView(Integer.parseInt(quantita.get(position)),position);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return prodottos.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView nome,prezzo,quantita;
        private Button add, remove;

        Prodotto prodotto;
        int position;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.immagine);
            nome=itemView.findViewById(R.id.nomeP);
            prezzo=itemView.findViewById(R.id.prezzoP);
            quantita=itemView.findViewById(R.id.quantita);
            add=itemView.findViewById(R.id.piu);
            remove=itemView.findViewById(R.id.meno);





        }


        public void bind(Prodotto prodotto, String s) {
            nome.setText(prodotto.getNome());
            prezzo.setText(""+prodotto.getPrezzo()+"€");
            quantita.setText(s);
            img.setImageResource(prodotto.getFoto());
        }
    }


}