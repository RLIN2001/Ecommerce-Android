package it.itsar.provab;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Detail extends AppCompatActivity {

    TextView titolo, prezzo, descrizione, quantita,back;
    ImageView imageView, plus, minus;
    Button buttonA;

    private int qnty = 1;

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

        titolo.setText(prodotto.getNome());
        prezzo.setText("" + prodotto.getPrezzo() + "€");
        descrizione.setText("Descrizione:\n" + prodotto.getDescrizione());
        imageView.setImageResource(prodotto.getFoto());


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

            SessionManager sessionManagement = new SessionManager(this);

            if(checkLogin()==false){
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
            else {


                ReadWriteFile readWriteFile = new ReadWriteFile();

                File file = new File(getFilesDir(), "carrello");
                if (!file.exists()) {
                    try {
                        readWriteFile.scrivi("carrello", prodotto.getId()+" "+qnty+",", getFilesDir());
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                else
                {
                    try {
                        String txt = readWriteFile.leggi("carrello",getFilesDir());

                        String []split=txt.split(",");

                        String s="";

                        for (String string : split)
                            s+=string+" ";


                        ArrayList<String>id=new ArrayList<>();
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







                        if(id.contains(prodotto.getId())) {

                            int index=id.indexOf(prodotto.getId());
                            txt = txt.replaceAll(id.get(index)+" "+quantity.get(index), id.get(index)+" "+(Integer.parseInt(quantity.get(index))+qnty));
                            readWriteFile.sovrascrivi(getFilesDir(),txt);
                            finish();
                        }
                        else
                        {
                            readWriteFile.scrivi("carrello", prodotto.getId()+" "+qnty+",", getFilesDir());
                            finish();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }




        });
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