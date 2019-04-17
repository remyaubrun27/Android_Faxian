package com.example.faxianchina.faxian;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_History extends Fragment {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference textstorage;
    private TextView history;
    public Frag_History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        textstorage = storage.getReferenceFromUrl("gs://faxian-china.appspot.com/Han_History.txt");

        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.frag_history, container, false);
        history = (TextView)v.findViewById(R.id.hanhistory);



        return v;
    }

}
