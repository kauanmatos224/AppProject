package com.example.securityaplication;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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

        // KAUAN--> SÓ QUEBREI ESSA PARTE EM DUAS PARA FICAR MAIS LEGIVEL
        ListView listView;// CRIA OBJETO LISTVIEW
        listView = (ListView) view.findViewById(R.id.listView); // REFERENCIA O OBJETO AO SEU ID XML



       /* String[] menuItems = {"text 1",
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
        };*/

        //Transformando a img da caneta em bitmap
        Drawable drawable = this.getResources().getDrawable(R.drawable.caneta_teste);
        Bitmap bitmaptest = ((BitmapDrawable)drawable).getBitmap();


        //Criando objetos de teste
        ItemList test = new ItemList(bitmaptest, "Caneta Bic",
                "Caneta", "Comigo");
        ItemList test2 = new ItemList(bitmaptest, "Caneta Bic",
                "Caneta", "Perdido");
        ItemList test3 = new ItemList(bitmaptest, "Caneta Bic",
                "Caneta", "Emprestado");

        //Adicionando esses objetos para a array do listView
        ArrayList<ItemList> ItemList = new ArrayList<>();
        ItemList.add(test);
        ItemList.add(test2);
        ItemList.add(test3);
        ItemList.add(test);//Varios testes para ver se da pra fazer scroll no home
        ItemList.add(test2);
        ItemList.add(test3);
        ItemList.add(test);
        ItemList.add(test2);
        ItemList.add(test3);
        ItemList.add(test);
        ItemList.add(test2);
        ItemList.add(test3);

        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(getActivity(), R.layout.adapter_view_layout,ItemList);
        listView.setAdapter(itemArrayAdapter);


        /*//CRIA ADAPTER PARA ENVIAR OS ITENS DO ARRAY DE LISTA "MENUITENS" PARA O OBJETO LISTVIEW
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,
                menuItems
        );

        listView.setAdapter(listViewAdapter);
*/
        // Inflate the layout for this fragment
        return view;
    }
}