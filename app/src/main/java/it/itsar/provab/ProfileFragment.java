package it.itsar.provab;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class ProfileFragment extends Fragment {

    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonExit=view.findViewById(R.id.logout);
        Button buttonCart=view.findViewById(R.id.carrello);
        textView=view.findViewById(R.id.username);

        SessionManager sessionManagement = new SessionManager(getActivity());

        checkLogin();


        buttonExit.setOnClickListener(view1 -> {

            sessionManagement.removeSession();

            File f= new File(getActivity().getFilesDir(),"carrello");

            if (f.delete()) {
                moveToLogin();
            }
            else{
                moveToLogin();


            }

        });

        buttonCart.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Carrello.class);
            startActivity(intent);
        });



    }

    private void checkLogin() {

        SessionManager sessionManagement = new SessionManager(getActivity());
        int userID = sessionManagement.getSession();

        if(userID == -1){

            new AlertDialog.Builder(getActivity())
                    .setTitle("Attenzione")
                    .setMessage("Non sei loggato, premi accedi per passare al login")
                    .setPositiveButton("accedi",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            moveToLogin();

                        }
                    })
                    .setNegativeButton("annulla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), Home.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
        else
        {
            textView.setText(sessionManagement.sharedPreferences.getString("username",""));
        }




    }


    private void moveToLogin() {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish();

    }
}