package it.itsar.provab;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Registration extends AppCompatActivity {
    EditText username,passwd,passwdC;
    Button registrazione;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        username=findViewById(R.id.username);
        passwd=findViewById(R.id.password);
        passwdC=findViewById(R.id.passwordC);
        registrazione=findViewById(R.id.registrati);



        registrazione.setOnClickListener(view -> {
            if (username.getText().toString().isEmpty() || passwd.getText().toString().isEmpty()||passwdC.getText().toString().isEmpty())
                Snackbar.make(view,"Tutti i campi devono essere inseriti",1000).show();

            else if(!passwd.getText().toString().equals(passwdC.getText().toString())){
                Snackbar.make(view,"Le due password inserite devono coincidere",1000).show();

            }

            else{




                ReadWriteFile readWriteFile=new ReadWriteFile();

                File file= new File(getFilesDir(),"utente");
                if(file.exists()){
                    try {
                        String txt=readWriteFile.leggi("utente",getFilesDir());

                        String[] credenziali = txt.split("\\s+");



                        ArrayList<String> userN=getUsername(credenziali);



                        if(userN.contains(username.getText().toString()))
                            Snackbar.make(view,"L'username inserito esiste già",1000).show();

                        else {




                            SessionManager sessionManager=new SessionManager(this);
                            count = sessionManager.sharedPreferences.getInt("counter", 0);


                            readWriteFile.scrivi("utente", count+":"+username.getText().toString() + " " + passwd.getText().toString() + "\n", getFilesDir());


                            sessionManager.editor.putInt("counter", (count+1)).commit();


                            Intent intent=new Intent(this,Login.class);
                            startActivity(intent);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{


                    try {


                        SessionManager sessionManager=new SessionManager(this);
                        count = sessionManager.sharedPreferences.getInt("counter", 0);

                        readWriteFile.scrivi("utente",count+":"+username.getText().toString()+" "+passwd.getText().toString()+"\n",getFilesDir());
                        sessionManager.editor.putInt("counter", (count+1)).commit();

                        Intent intent=new Intent(this,Login.class);
                        startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }
        });









    }

    private ArrayList<String> getUsername(String[] credenziali) {
        ArrayList<String> username=new ArrayList<>();
        ArrayList<String> parts=new ArrayList<>();

        for(int i=0;i<credenziali.length;i++)
        {
            if(i%2==0)
                parts.add(credenziali[i]);
        }

        for(int i=0;i<parts.size();i++)
        {
            username.add(parts.get(i).substring(parts.get(i).indexOf(":") +1));
        }

        return username;


    }

}