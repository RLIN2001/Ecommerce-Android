package it.itsar.provab;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    SearchView searchView;
    ProdottoAdapter prodottoAdapter;


    Prodotto []prodotti;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView textView,titolo;



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



        ReadWriteFile readWriteFile=new ReadWriteFile();
        File file= new File(getActivity().getFilesDir(),"prodotti");

        if(!file.exists()) {

            int []image= {
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

            try {
                readWriteFile.scrivi("prodotti","id:1/nome:SAMSUNG Galaxy A23/descrizione:sono la descrizione del telefono SAMSUNG Galaxy A23/categoria:cellulari/foto:"+image[0]+"/prezzo:350\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:2/nome:APPLE iPhone 14 Pro/descrizione:sono la descrizione del telefono APPLE iPhone 14 Pro/categoria:cellulari/foto:"+image[1]+"/prezzo:1469\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:3/nome:OPPO RENO 8 Lite/descrizione:sono la descrizione del telefono OPPO RENO 8 Lite/categoria:cellulari/foto:"+image[2]+"/prezzo:400\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:4/nome:Xiaomi Redmi 9A/descrizione:sono la descrizione del telefono Xiaomi Redmi 9A/categoria:cellulari/foto:"+image[3]+"/prezzo:129\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:5/nome:XIAOMI 11T PRO/descrizione:sono la descrizione del telefono XIAOMI 11T PRO/categoria:cellulari/foto:"+image[4]+"/prezzo:700\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:6/nome:HP 15S-FQ5022NL/descrizione:sono la descrizione del computer HP 15S-FQ5022NL HP 15S-FQ5022NL, 15,6 pollici, processore Intel® Core™ i5, INTEL Iris Xe Graphics, 16 GB SSD, 512 GB/categoria:computer/foto:"+image[5]+"/prezzo:870\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:7/nome:HP LENOVO Essential V15-ALC/descrizione:sono la descrizione del computer LENOVO Essential V15-ALC/categoria:computer/foto:"+image[6]+"/prezzo:700\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:8/nome:APPLE MB PRO 16 M1 MAX 1TB SG/descrizione:sono la descrizione del macbook APPLE MB PRO 16 M1 MAX 1TB SG/categoria:computer/foto:"+image[7]+"/prezzo:3949\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:9/nome:ACER PREDATOR TRITON 500 SE/descrizione:sono la descrizione del computer ACER PREDATOR TRITON 500 SE/categoria:computer/foto:"+image[8]+"/prezzo:3700\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:10/nome:Tablet SAMSUNG Galaxy Tab A7/descrizione:sono la descrizione del tablet SAMSUNG Galaxy Tab A7 /categoria:tablet/foto:"+image[9]+"/prezzo:220\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:11/nome:Tablet APPLE IPAD PRO 11/descrizione:sono la descrizione del tablet APPLE IPAD PRO 11/categoria:tablet/foto:"+image[10]+"/prezzo:1600\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:12/nome:Apple airpods/descrizione:sono la descrizione delle cuffie APPLE AIRPODS WITH CHARGING CUFFIE WIRELESS/categoria:auricolari e vivavoce/foto:"+image[11]+"/prezzo:160\n",getActivity().getFilesDir());
                readWriteFile.scrivi("prodotti","id:13/nome:Mouse HP wired mouse 1000/descrizione:sono la descrizione del mouse/categoria:periferiche pc e accessori/foto:"+image[12]+"/prezzo:8\n",getActivity().getFilesDir());



                //readWriteFile.scrivi("prodotti","id:5/nome:IPhone 13pro/descrizione:sono una descrizione/categoria:cellulari/foto:"+image[0]+"/prezzo:700\n",getActivity().getFilesDir());
                //readWriteFile.scrivi("prodotti","id:6/nome:Macbook/descrizione:sono una descrizione/categoria:cellulari/foto:"+image[0]+"/prezzo:700\n",getActivity().getFilesDir());
                getActivity().recreate();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else
        {
            String txt = null;
            try {
                txt = readWriteFile.leggi("prodotti",getActivity().getFilesDir());

                String []split=txt.split("[/\n]");

                ArrayList<String> id=getDati("id",split);
                ArrayList<String> nome=getDati("nome", split);
                ArrayList<String> descrizione=getDati("descrizione", split);
                ArrayList<String> categoria=getDati("categoria", split);
                ArrayList<String> foto=getDati("foto", split);
                ArrayList<String> prezzo=getDati("prezzo", split);





                prodotti=new Prodotto[id.size()];

                for(int i=0;i<id.size();i++)
                    prodotti[i]=new Prodotto(id.get(i),nome.get(i),descrizione.get(i),categoria.get(i),Integer.parseInt(foto.get(i)),Double.parseDouble(prezzo.get(i)));



                prodottoAdapter=new ProdottoAdapter(prodotti,getActivity());
                recyclerView.setAdapter(prodottoAdapter);




                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        filtraLista(s);


                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        if(s.equals("")) {
                            prodottoAdapter.setListaFiltro(prodotti);
                            recyclerView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            titolo.setVisibility(View.VISIBLE);

                        }

                        return true;
                    }
                });






            } catch (IOException e) {
                e.printStackTrace();
            }


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


    private void filtraLista(String s) {
        ArrayList<Prodotto> prod=new ArrayList<>();
        for(Prodotto pr: prodotti)
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