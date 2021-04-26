package com.example.securityaplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemArrayAdapter extends ArrayAdapter<ItemList> {

    // private static final String TAG= "ItemArrayAdapter";
    private Context mContext;
    private int mResource;

    public ItemArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ItemList> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent,false);

        ImageView imageView = convertView.findViewById(R.id.imgImagem);

        TextView txtNomeItem = convertView.findViewById(R.id.txtNomeItem);

        TextView txtCategoria = convertView.findViewById(R.id.txtCategoria);

        TextView txtStatus = convertView.findViewById(R.id.txtStatus);

        imageView.setImageBitmap(getItem(position).getImgImagem());

        txtNomeItem.setText(getItem(position).getTxtNomeItem());

        txtCategoria.setText(getItem(position).getTxtCategoria());

        txtStatus.setText(getItem(position).getTxtStatus());

        return convertView;
    }
}
