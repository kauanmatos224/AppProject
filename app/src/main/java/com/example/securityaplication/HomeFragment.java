package com.example.securityaplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    ItemArrayAdapter adapter;
    private ListView listax;

    //objeto de banco de dados
    SQLiteDatabase database;

    //no construtor do HomeFragment, trás como parâmentro da MainActivity a conexão com banco de dados.
    public HomeFragment(SQLiteDatabase sqlite) {

        database = sqlite;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listax = (ListView)view.findViewById(R.id.listViewx);

        // KAUAN--> SÓ QUEBREI ESSA PARTE EM DUAS PARA FICAR MAIS LEGIVEL
       // ListView listView;// CRIA OBJETO LISTVIEW
       // listView = (ListView) view.findViewById(R.id.listViewx); // REFERENCIA O OBJETO AO SEU ID XML


        Cursor cursor = database.rawQuery("select * from tb_mats", null);
        if(cursor.moveToFirst()){
            ArrayList<ItemList> ItemList = new ArrayList<>();
            do{

               /*Drawable drawable = this.getResources().getDrawable(R.drawable.caneta_teste);
               Bitmap bitmaptest = ((BitmapDrawable)drawable).getBitmap();
                */
                Bitmap bmpImg;
                String pathImg = cursor.getString(2);
                @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = this.getResources().getDrawable(R.drawable.sem_foto);

                if(pathImg.equals("") || pathImg.equals("null")){
                    bmpImg = ((BitmapDrawable)drawable).getBitmap();
                }else{
                    try {
                        bmpImg = (BitmapFactory.decodeFile(pathImg));
                        if(bmpImg==null){
                            bmpImg = ((BitmapDrawable)drawable).getBitmap();
                        }
                    }catch (Exception ex){
                        bmpImg = ((BitmapDrawable)drawable).getBitmap();
                    }
                }

                ItemList test = new ItemList(bmpImg, cursor.getString(1),
                        cursor.getString(3), cursor.getString(5));

                ItemList.add(test);

            }
            while(cursor.moveToNext());
            adapter = new ItemArrayAdapter(getActivity(), R.layout.adapter_view_layout,ItemList);
            listax.setAdapter(adapter);
        }
        listax.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                ItemList item= adapter.getItem(position);

                //Toast.makeText(getContext(), item.getTxtNomeItem().toString(),   Toast.LENGTH_SHORT).show();
                //o toast mostra o nome do item que foi clicado pelo usuario

                showPopup(adapterView);

            }
        });
        return view;
    }

    //metodo do menu popup
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menupopup);
        popup.show();
    }

    @Override
    //aqui sao as funcoes do menu popup, nesse caso ainda so mostram qual item foi clicado
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.itemAlterar:
                Toast.makeText(getActivity(), "Alterar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.itemExcluir:
                Toast.makeText(getActivity(), "Excluir", Toast.LENGTH_LONG).show();
                return true;
            case R.id.itemEmprestar:
                Toast.makeText(getActivity(), "Emprestar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.itemRecuperar:
                Toast.makeText(getActivity(), "Recuperar", Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }}
}