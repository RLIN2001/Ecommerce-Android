package it.itsar.provab;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Carrello extends AppCompatActivity{
    private RecyclerView recyclerView;
    static ArrayList<Prodotto> prC=new ArrayList<>();
    static ArrayList<String> quan=new ArrayList<>();
    CartAdapter adapter;
    static TextView totale;
    double totCosto=0;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_carrello);
        recyclerView=findViewById(R.id.recyclerview);
        totale=findViewById(R.id.totale);
        button=findViewById(R.id.checkout);

        prendiDati();

        button.setOnClickListener(view -> {

            new AlertDialog.Builder(Carrello.this)
                    .setTitle("Conferma ordine")
                    .setMessage("Premi ok per confermare l'ordine")
                    .setPositiveButton("ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {



                            new AlertDialog.Builder(Carrello.this)
                                    .setTitle("Ordine ricevuto")
                                    .setMessage("L'ordine è stato effettuato con successo")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            File f= new File(getFilesDir(),"carrello");

                                            if (f.delete()) {
                                                Intent intent = new Intent(Carrello.this, Home.class);
                                                startActivity(intent);
                                                finish();

                                            }

                                        }
                                    })
                                    .setIcon(R.drawable.ic_baseline_check_24)
                                    .show();



                        }
                    })
                    .setNegativeButton("annulla", null)
                    .setIcon(R.drawable.ic_baseline_shopping_cart_checkout_24)
                    .show();



        });











    }

    private void prendiDati() {
        ReadWriteFile readWriteFile=new ReadWriteFile();
        try {
            String txt = readWriteFile.leggi("carrello",getFilesDir());


            System.out.println(txt);


            String []split=txt.split(",");

            String s="";

            for (String string : split)
                s+=string+" ";


            ArrayList<String> id=new ArrayList<>();
            ArrayList<String>quantity=new ArrayList<>();


            String []split2= s.split(" ");
            for(int i=0;i<split2.length;i++)
            {
                if(i%2==0) {
                    id.add(split2[i]);
                }
                else
                    quantity.add(split2[i]);
            }

            txt = readWriteFile.leggi("prodotti",getFilesDir());

            String []splitt=txt.split("[/\n]");

            ArrayList<String> idP=getDati("id",splitt);
            ArrayList<String> nomeP=getDati("nome", splitt);
            ArrayList<String> descrizioneP=getDati("descrizione", splitt);
            ArrayList<String> categoriaP=getDati("categoria", splitt);
            ArrayList<String> fotoP=getDati("foto", splitt);
            ArrayList<String> prezzoP=getDati("prezzo", splitt);

            Prodotto []prodotti=new Prodotto[idP.size()];

            for(int i=0;i<idP.size();i++)
                prodotti[i]=new Prodotto(idP.get(i),nomeP.get(i),descrizioneP.get(i),categoriaP.get(i),Integer.parseInt(fotoP.get(i)),Double.parseDouble(prezzoP.get(i)));


            int i=0;
            int j=0;
            while(j<id.size()){
                if(prodotti[i].getId().equals(id.get(j))){
                    prC.add(prodotti[i]);
                    quan.add(quantity.get(j));
                    j++;
                    i=0;
                }
                else{
                    i++;
                }
            }


            adapter=new CartAdapter(prC,quan,this);
            recyclerView.setAdapter(adapter);

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


            for(int x=0;x< prC.size();x++){
                totCosto+=prC.get(x).getPrezzo()*Double.parseDouble(quan.get(x));
            }

            totale.setText("Totale: "+totCosto+"€");



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private ArrayList<String> getDati(String info, String[] split) {

        ArrayList<String> dati=new ArrayList<>();

        for(int i=0;i<split.length;i++)
        {
            if(split[i].contains(info))
            {
                dati.add(split[i].substring(split[i].indexOf(":") +1));

            }

        }
        return dati;
    }



    public static void updateTextView(int quantita, int position) {
       quan.set(position, ""+quantita);




       double totCosto=0;
        for(int x=0;x< prC.size();x++){
            totCosto+=prC.get(x).getPrezzo()*Integer.parseInt(quan.get(x));
        }


        totale.setText("Totale: "+totCosto+"€");

    }

    public void onDestroy() {
        super.onDestroy();
        prC.clear();
        quan.clear();
    }
}