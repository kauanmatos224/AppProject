package com.example.safemonkey;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.safemonkey.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {



    public AddFragment() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Intent intent = new Intent(getActivity(),ac_cadastro.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_add, container, false);
    }
}