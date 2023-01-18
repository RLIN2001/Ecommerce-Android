package it.itsar.provab;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProdottoAdapter extends RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder>{

    private Prodotto[] prodotto;
    private Context context;
    private Prodotto prod;

    public ProdottoAdapter(Prodotto[] prodotto,Context context){
        this.prodotto=prodotto;
        this.context=context;
    }

    public void setListaFiltro(Prodotto [] listaFiltro){
        prodotto=listaFiltro;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdottoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.lista,parent,false);
        return new ProdottoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdottoViewHolder holder, int position) {
        holder.bind(prodotto[position]);


        holder.itemView.setOnClickListener(view -> {
            prod=prodotto[position];
            Intent i = new Intent(holder.itemView.getContext(), Detail.class);
            i.putExtra("prodotto",prod);
            holder.itemView.getContext().startActivity(i);

        });

    }

    @Override
    public int getItemCount() {
        return prodotto.length;
    }

    public class ProdottoViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView nome,prezzo;


        public ProdottoViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.immagine);
            nome=itemView.findViewById(R.id.nome);
            prezzo=itemView.findViewById(R.id.prezzo);
        }

        public void bind(Prodotto prodotto) {
            img.setImageResource(prodotto.getFoto());
            nome.setText(prodotto.getNome());
            prezzo.setText(""+prodotto.getPrezzo()+"€");
        }
    }


}