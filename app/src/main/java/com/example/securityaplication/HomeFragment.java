package com.example.securityaplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
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
public class HomeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener  {

    ItemArrayAdapter adapter;
    private ListView listax;
    public int dataId = 0;
    ItemList test;
    int itemPos = 0;

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
        //ListView listView;// CRIA OBJETO LISTVIEW
        //listView = (ListView) view.findViewById(R.id.listView); // REFERENCIA O OBJETO AO SEU ID XML
        //  listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        Cursor cursor = database.rawQuery("select * from tb_mats;", null);
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
                    bmpImg = (BitmapFactory.decodeFile(pathImg));
                    if(bmpImg==null){
                        bmpImg = ((BitmapDrawable)drawable).getBitmap();
                    }
                }

                test = new ItemList(bmpImg, cursor.getString(1),
                        cursor.getString(3), cursor.getString(5), cursor.getString(0));

                ItemList.add(test);

            }
            while(cursor.moveToNext());

            adapter = new ItemArrayAdapter(getActivity(), R.layout.adapter_view_layout,ItemList);
            listax.setAdapter(adapter);
        }


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(position1 == 0){
                    showPopup(adapterView);
                }
            }
        });*/

        listax.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                ItemList item = adapter.getItem(position);//pega valores de uma linha a partir da posição do item na lista array.
                itemPos = position; //posição dos elementos no listView.

                //Toast.makeText(getContext(),"Item selected: "+ item.getId(),   Toast.LENGTH_SHORT).show();
                dataId = Integer.parseInt(item.getId());
                showPopup(adapterView);


            }
        });
        // listView.setItemChecked(position1, true);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(getActivity(), v, Gravity.END,R.attr.actionOverflowMenuStyle, 0);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menupopup);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        String sqlCommand = "delete from tb_mats where(_id="+dataId+");";
        switch(menuItem.getItemId()){
            case R.id.itemAlterar:
                //Toast.makeText(getActivity(), "Item 1 clicked", Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).telaUpdate(dataId);

                return true;
            case R.id.itemExcluir:
                //Toast.makeText(getActivity(), "Item 2 clicked:"+dataId, Toast.LENGTH_LONG).show();
                try {
                    database.execSQL(sqlCommand);
                    Toast.makeText(getActivity(), "Item excluido", Toast.LENGTH_LONG).show();
                    adapter.removeItem(itemPos);
                    adapter.notifyDataSetChanged();
                }catch (Exception ex){
                    Toast.makeText(getActivity(), "Não foi possível excluir o item", Toast.LENGTH_LONG).show();
                }

                return true;
            case R.id.itemEmprestar:
                //Toast.makeText(getActivity(), "Item 3 clicked", Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).telaEmprestar(dataId);

                return true;
            case R.id.itemRecuperar:
                ((MainActivity)getActivity()).telaRecuperar(dataId);
                return true;
            default:
                return false;
        }}
}