package it.itsar.provab;
import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    SearchView searchView;
    ProdottoAdapter prodottoAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Prodotto [] prodotto;
    ArrayList<Prodotto> listaProdotto=new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView textView,titolo;
    int []image= new int[]{
        R.drawable.prod1,
                R.drawable.prod2,
                R.drawable.prod3,
                R.drawable.prod4,
                R.drawable.prod5,
                R.drawable.prod6,
                R.drawable.prod7,
                R.drawable.prod8,
                R.drawable.prod9,
                R.drawable.prod10,
                R.drawable.prod11,
                R.drawable.prod12,
                R.drawable.prod13,

    };
    int counter=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView=view.findViewById(R.id.searchView);
        searchView.clearFocus();

        textView=view.findViewById(R.id.msg);
        titolo=view.findViewById(R.id.nomeP);

        recyclerView=view.findViewById(R.id.recyclerview);
        layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);





                CollectionReference idsRef = db.collection("prodotti");

                idsRef.orderBy("id")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                            addProdotto(document);
                                    }

                                    prodotto = listaProdotto.toArray(new Prodotto[listaProdotto.size()]);
                                    prodottoAdapter=new ProdottoAdapter(prodotto,getActivity());
                                    recyclerView.setAdapter(prodottoAdapter);


                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });



                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        filtraLista(s);


                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        if(s.equals("")) {
                            prodottoAdapter.setListaFiltro(prodotto);
                            recyclerView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            titolo.setVisibility(View.VISIBLE);
                        }

                        return true;
                    }
                });






            }

    private void addProdotto(QueryDocumentSnapshot document) {
        String id=(String)document.getData().get("id");
        String nome= (String) document.getData().get("nome");
        long prezzo= (long) document.getData().get("prezzo");
        String descrizione=(String) document.getData().get("descrizione");
        String categoria=(String) document.getData().get("categoria");


        System.out.println(nome+""+image[counter]+"\n");
        listaProdotto.add(new Prodotto(id,nome,descrizione,categoria,image[counter],(double) prezzo));
        counter++;

    }


    private void filtraLista(String s) {
        ArrayList<Prodotto> prod=new ArrayList<>();
        for(Prodotto pr: prodotto)
        {
            if(pr.getNome().toLowerCase().contains(s.toLowerCase()))
                prod.add(pr);
        }

        if(prod.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            titolo.setVisibility(View.GONE);
        }
        else

            prodottoAdapter.setListaFiltro(prod.toArray(new Prodotto[prod.size()]));

    }







}