package com.example.faxianchina.faxian;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Settings extends Fragment {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference mapstorage;
    private StorageReference swordmstorage, archerstorage, cavalrystorage, spearmanstorage;
    private DatabaseReference mRef = database.getReference();
    private TextView counter,highscoreCounter;
    private Button reset;
    private Integer highscore;
    public Frag_Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_settings, container, false);
        reset = (Button)v.findViewById(R.id.resetButton);
        counter = (TextView)v.findViewById(R.id.battleCounter);
        highscoreCounter = (TextView)v.findViewById(R.id.highscore);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter.setText("Battles Won: "+dataSnapshot.child("Battles").child("battlesWon").getValue().toString()
                + "\nBattles Fought: "+ dataSnapshot.child("Battles").child("battlesFought").getValue().toString());
                highscoreCounter.setText("Highscore: "+dataSnapshot.child("Battles").child("highscore").getValue().toString());

                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRef.child("Coins").child("totalCoins").setValue(1000);
                        mRef.child("population").child("currPopulation").setValue(50);
                        mRef.child("Battles").child("battlesWon").setValue(0);
                        mRef.child("Battles").child("battlesFought").setValue(0);
                        mRef.child("Market_soldiers").child("Archer").child("amountBoughtCum").setValue(0);
                        mRef.child("Market_soldiers").child("Swordsmen").child("amountBoughtCum").setValue(0);
                        mRef.child("Market_soldiers").child("Spearmen").child("amountBoughtCum").setValue(0);
                        mRef.child("Market_soldiers").child("Cavalry").child("amountBoughtCum").setValue(0);
                        mRef.child("Market_weapons").child("Bow").child("amountBought").setValue(0);
                        mRef.child("Market_weapons").child("Sword").child("amountBought").setValue(0);
                        mRef.child("Market_weapons").child("Spear").child("amountBought").setValue(0);
                        mRef.child("Market_weapons").child("Horse").child("amountBought").setValue(0);


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;
    }

}
