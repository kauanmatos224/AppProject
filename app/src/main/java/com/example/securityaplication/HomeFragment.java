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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

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

        // KAUAN--> SÓ QUEBREI ESSA PARTE EM DUAS PARA FICAR MAIS LEGIVEL
        ListView listView;// CRIA OBJETO LISTVIEW
        listView = (ListView) view.findViewById(R.id.listView); // REFERENCIA O OBJETO AO SEU ID XML


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
                    bmpImg = (BitmapFactory.decodeFile(pathImg));
                }

                ItemList test = new ItemList(bmpImg, cursor.getString(1),
                        cursor.getString(3), cursor.getString(5));

                ItemList.add(test);

            }
            while(cursor.moveToNext());
            ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(getActivity(), R.layout.adapter_view_layout,ItemList);
            listView.setAdapter(itemArrayAdapter);
        }
        return view;
    }
}