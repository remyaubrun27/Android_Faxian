package com.example.faxianchina.faxian;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Frag_Timeline extends Fragment {


    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference timestorage;

    private ImageView tl;

    public Frag_Timeline(){


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        timestorage = storage.getReferenceFromUrl("gs://faxian-china.appspot.com/Timelinedone.png");


        View v = inflater.inflate(R.layout.frag__timeline, container,false);
        tl = (ImageView)v.findViewById(R.id.timepic);

        Glide.with(this).using(new FirebaseImageLoader()).load(timestorage).into(tl);

        return v;
    }
  //  @Override
  //  public void onDestroyView() {
      //  super.onDestroyView();
     //   Frag_Map f = (Frag_Map) getFragmentManager()
        //        .findFragmentById(R.id.map);
   //     if (f != null)
     //       getFragmentManager().beginTransaction().remove(f).commit();
   // }
}
