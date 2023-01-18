package it.itsar.provab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Login extends AppCompatActivity {

    EditText username,password;
    Button buttonA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        buttonA=findViewById(R.id.accedi);



        buttonA.setOnClickListener(view -> {
            ReadWriteFile readWriteFile=new ReadWriteFile();

                File file= new File(getFilesDir(),"utente");




            if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                Snackbar.make(view,"Tutti i campi devono essere inseriti",500).show();

            else if(file.exists()) {
                    String txt= null;
                    try {
                        txt = readWriteFile.leggi("utente",getFilesDir());


                        String[]  credenziali= txt.split("\\s+");



                        ArrayList<String> id=getID(credenziali);
                        ArrayList<String> userN=getUsername(credenziali);
                        ArrayList<String> passW=getPassword(credenziali);


                        System.out.println(Arrays.toString(userN.toArray()));

                        if(userN.contains(username.getText().toString()))
                        {
                            int index=userN.indexOf(username.getText().toString());

                            if(!password.getText().toString().equals(passW.get(index)))
                            Snackbar.make(view,"Password non corretta,riprova!!",500).show();
                            else
                            {

                                Utente user = new Utente(Integer.parseInt(id.get(index)),userN.get(index),passW.get(index));
                                SessionManager sessionManagement = new SessionManager(Login.this);
                                sessionManagement.saveSession(user);

                                Intent intent = new Intent(this, Home.class);
                                startActivity(intent);
                            }



                        }
                        else
                            Snackbar.make(view,"Utente non esistente",500).show();









                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else{
                    new AlertDialog.Builder(Login.this)
                            .setTitle("Warning")
                            .setMessage("Non esistono account su questa app, premi il tasto OK per andare alla pagina di registrazione")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(Login.this,Registration.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }


        });

    }




    private ArrayList<String> getID(String[] credenziali) {
        ArrayList<String> id=new ArrayList<>();
        ArrayList<String> parts=new ArrayList<>();

        for(int i=0;i<credenziali.length;i++)
        {
            if(i%2==0)
                parts.add(credenziali[i]);
        }

        for(int i=0;i<parts.size();i++)
        {
            id.add(parts.get(i).split(":")[0]);
        }

        return id;


    }










    private ArrayList<String> getUsername(String[] credenziali) {
        ArrayList<String> username=new ArrayList<>();
        ArrayList<String> parts=new ArrayList<>();

        for(int i=0;i<credenziali.length;i++)
        {
            if(i%2==0)
                parts.add(credenziali[i]);
        }

        /*for(int i=0;i<parts.size();i++)
        {
            username.add(parts.get(i).split(":")[0]);
        }*/


        for(int i=0;i<parts.size();i++)
        {
            username.add(parts.get(i).substring(parts.get(i).indexOf(":") +1));
        }

        return username;


    }

    private ArrayList<String> getPassword(String[] credenziali) {

        ArrayList<String> password=new ArrayList<>();
        for(int i=0;i<credenziali.length;i++)
        {
            if(i%2!=0)
                password.add(credenziali[i]);
        }

        return password;
    }



}