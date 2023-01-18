package it.itsar.provab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       navigationView=findViewById(R.id.nav);

        getSupportActionBar().hide();
       getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();




        navigationView.setOnItemSelectedListener(item->{
            if(item.getItemId()==R.id.homeMenuItem) {
                changeFragment(HomeFragment.class);
            }
                else if(item.getItemId()==R.id.profileMenuItem) {
                changeFragment(ProfileFragment.class);

            }
            else if(item.getItemId()==R.id.seatchMenuItem)
                changeFragment(SearchFragment.class);

            return true;
        });

    }

    private void changeFragment(Class fragrmentClass){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragrmentClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }





}