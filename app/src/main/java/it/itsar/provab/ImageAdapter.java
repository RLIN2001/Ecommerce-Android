package it.itsar.provab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private Image[] immagini;
    private Context context;

    public ImageAdapter(Image[] immagini,Context context){
        this.immagini=immagini;
        this.context=context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(immagini[position]);

    }

    @Override
    public int getItemCount() {
        return immagini.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.immagine);
        }

        public void bind(Image image) {
            img.setImageResource(image.getId());
        }
    }


}