package com.example.safemonkey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.safemonkey.R;

import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter<ItemList> {


    private List<ItemList> items;

    public ItemArrayAdapter(Context context, int textViewResourceId, List<ItemList> items){
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            Context ctx = getContext();
            LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.adapter_view_layout, null);
        }
        ItemList lista = items.get(position);
        if (lista != null) {

            ((ImageView) v.findViewById(R.id.imgImagem)).setImageBitmap(lista.getImgImagem());
            ((TextView) v.findViewById(R.id.txtNomeItem)).setText(lista.getTxtNomeItem());
            ((TextView) v.findViewById(R.id.txtCategoria)).setText(lista.getTxtCategoria());
            ((TextView) v.findViewById(R.id.txtStatus)).setText(lista.getTxtStatus());
            ((TextView) v.findViewById(R.id.txtId)).setText(lista.getId());

        }
        return v;
    }
    public void removeItem(int position){
        this.items.remove(position);
        notifyDataSetChanged();
    }
}


