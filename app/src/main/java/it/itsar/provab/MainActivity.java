package it.itsar.provab;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private Button bA,bR,bS;
    private Intent intent;
    private RecyclerView recyclerView;
    private Image [] images={
            new Image(R.drawable.images),
            new Image(R.drawable.images2),
            new Image(R.drawable.images3),
            new Image(R.drawable.images4),
    };

    private ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        checkLogin();

        recyclerView=findViewById(R.id.recyclerview);
        bA=findViewById(R.id.accedi);
        bR=findViewById(R.id.registrati);
        bS=findViewById(R.id.salta);




        recyclerView.setHasFixedSize(true);

        imageAdapter= new ImageAdapter(images,this);
        recyclerView.setAdapter(imageAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);





        bA.setOnClickListener(view -> {
            intent=new Intent(this,Login.class);
            startActivity(intent);
        });

        bR.setOnClickListener(view -> {
            intent=new Intent(this,Registration.class);
            startActivity(intent);
        });

        bS.setOnClickListener(view -> {
            intent=new Intent(this,Home.class);
            startActivity(intent);
            finish();
        });



    }

    private void checkLogin() {
        SessionManager sessionManagement = new SessionManager(this);
        int userID = sessionManagement.getSession();

        if(userID != -1){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }

    }


}