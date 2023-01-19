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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    EditText username,password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button buttonA;
    boolean found=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        buttonA=findViewById(R.id.accedi);



        buttonA.setOnClickListener(view -> {


            if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                Snackbar.make(view,"Tutti i campi devono essere inseriti",500).show();

            else
                checkUser(view);


        });

    }

    private void checkUser(View view) {
        db.collection("utenti")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("username").equals(username.getText().toString())){
                                    if(!password.getText().toString().equals(document.getData().get("password"))){
                                        Snackbar.make(view,"Password non corretta,riprova!!",500).show();
                                    }
                                    else
                                       login(document.getId());


                                    found=true;
                                    break;

                                }
                            }

                            if(found==false)
                                Snackbar.make(view,"Utente non esistente",500).show();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void login(String id) {
        Utente user = new Utente(1,username.getText().toString(),password.getText().toString(),id);
        SessionManager sessionManagement = new SessionManager(Login.this);
        sessionManagement.saveSession(user);

        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
    }


}