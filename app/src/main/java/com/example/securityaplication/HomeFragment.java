package com.example.securityaplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String[] menuItems = {"text 1",
                "text 2",
                "text 3",
                "text 1",
                "text 2",
                "text 3",
                "text 1",
                "text 2",
                "text 3",
                "text 1",
                "text 2",
                "text 3",
                "text 1",
                "text 2",
                "text 3",
                "text 1",
                "text 2",
                "text 3",
                "text 1",
                "text 2",
                "text 3",
                "text 1",
                "text 2",
                "text 3",

        };
        // KAUAN--> SÓ QUEBREI ESSA PARTE EM DUAS PARA FICAR MAIS LEGIVEL
        ListView listView;// CRIA OBJETO LISTVIEW
        listView = (ListView) view.findViewById(R.id.listView); // REFERENCIA O OBJETO AO SEU ID XML



        //CRIA ADAPTER PARA ENVIAR OS ITENS DO ARRAY DE LISTA "MENUITENS" PARA O OBJETO LISTVIEW
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,
                menuItems
        );

        listView.setAdapter(listViewAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}