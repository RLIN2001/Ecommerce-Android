package it.itsar.provab;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    EditText username,passwd,passwdC;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button registrazione;
    boolean found;
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

            else if(!passwd.getText().toString().equals(passwdC.getText().toString()))
                Snackbar.make(view,"Le due password inserite devono coincidere",1000).show();

            else
                checkUser(view);
            });



    }

    private void checkUser(View view) {
        found=false;
        db.collection("utenti")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("username").equals(username.getText().toString())) {
                                    found = true;
                                    break;
                                }
                            }

                            if(found ==true)
                                Snackbar.make(view,"account già esistente, riprova con un altro username",1000).show();
                            else {
                                addUser();
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });


    }

    private void addUser() {

        Map<String, Object> user = new HashMap<>();
        user.put("username", username.getText().toString());
        user.put("password", passwd.getText().toString());

        db.collection("utenti")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Intent intent = new Intent(Registration.this, Login.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }



}