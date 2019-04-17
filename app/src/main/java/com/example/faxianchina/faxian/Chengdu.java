package com.example.faxianchina.faxian;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chengdu extends Fragment {


    private TextView userArchers,userSwordsmen,userSpearmen,userCavalry;//Textviews for user army
    private TextView oppArchers, oppSwordsmen,oppSpearmen,oppCavalry;//textviews for enemy army
    private TextView city, userArmyLabel, oppArmyLabel;

    private Button backButton, attackButton;
    private String userPower,cityPower;

    private String userLabel,oppLabel;//Labels
    private String enemyArchers, enemySwordsmen,enemySpearmen,enemyCavalry;//enemy army
    private String myArchers,mySwordsmen,mySpearmen,myCavalry;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = database.getReference();
    private Integer battleWon,battleFought,archerPower,swordsmenPower,spearmenPower,cavalryPower;
    public Chengdu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_chengdu, container, false);
        //buttons
        backButton = (Button)v.findViewById(R.id.back);
        attackButton = (Button)v.findViewById(R.id.attack);
        //Your army
        userArchers = (TextView)v.findViewById(R.id.yourArchers);
        userSwordsmen = (TextView)v.findViewById(R.id.yourSwordsmen);
        userSpearmen = (TextView)v.findViewById(R.id.yourSpearmen);
        userCavalry= (TextView)v.findViewById(R.id.yourCavalry);



        //Enemy army
        oppArchers = (TextView)v.findViewById(R.id.enemyArchers);
        oppSwordsmen = (TextView)v.findViewById(R.id.enemySwordsmen);
        oppSpearmen = (TextView)v.findViewById(R.id.enemySpearmen);
        oppCavalry = (TextView)v.findViewById(R.id.enemyCavalry);
        // Labels
        city = (TextView)v.findViewById(R.id.cityLabel);
        userArmyLabel = (TextView)v.findViewById(R.id.yourArmyLabel);
        oppArmyLabel = (TextView)v.findViewById(R.id.enemyArmyLabel);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Getting values from database
                //Get labals
                userLabel = dataSnapshot.child("Labels").child("userArmyLabel").getValue().toString();
                oppLabel = dataSnapshot.child("Labels").child("enemyArmyLabel").getValue().toString();
                //Get enemy army numbers
                enemyArchers = dataSnapshot.child("Cities").child("Chengdu").child("troopCount").child("Archers").getValue().toString();
                enemySwordsmen = dataSnapshot.child("Cities").child("Chengdu").child("troopCount").child("Swordsmen").getValue().toString();
                enemySpearmen = dataSnapshot.child("Cities").child("Chengdu").child("troopCount").child("Spearmen").getValue().toString();
                enemyCavalry = dataSnapshot.child("Cities").child("Chengdu").child("troopCount").child("Cavalry").getValue().toString();
                //Get your army numbers
                myArchers = dataSnapshot.child("Market_soldiers").child("Archer").child("amountBoughtCum").getValue().toString();
                mySwordsmen = dataSnapshot.child("Market_soldiers").child("Swordsmen").child("amountBoughtCum").getValue().toString();
                mySpearmen = dataSnapshot.child("Market_soldiers").child("Spearmen").child("amountBoughtCum").getValue().toString();
                myCavalry = dataSnapshot.child("Market_soldiers").child("Cavalry").child("amountBoughtCum").getValue().toString();





                //Setting values
                //Labels
                userArmyLabel.setText(userLabel);
                oppArmyLabel.setText(oppLabel);
                //clear values before appending DB values. First enemy then user
                oppArchers.setText("Archers");userArchers.setText("Archers");
                oppSwordsmen.setText("Swordsmen"); userSwordsmen.setText("Swordsmen");
                oppSpearmen.setText("Spearmen");userSpearmen.setText("Spearmen");
                oppCavalry.setText("Cavalry");userCavalry.setText("Cavalry");

                //Appending enemy army numbers to hard coded labels
                oppArchers.setText("Archers: " + enemyArchers);
                oppSwordsmen.setText("Swordsmen: " + enemySwordsmen);
                oppSpearmen.setText("Spearmen: " + enemySpearmen);
                oppCavalry.setText("Cavalry: " + enemyCavalry);

                //Appending the amount of troops you bought to the hard coded labels
                userArchers.setText("Archers: " + myArchers);
                userSwordsmen.setText("Swordsmen: " + mySwordsmen);
                userSpearmen.setText("Spearmen: " + mySpearmen);
                userCavalry.setText("Cavalry: " + myCavalry);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        cityPower = dataSnapshot.child("Cities").child("Chengdu").child("cityPower").getValue().toString();
                        Integer newCityPower = Integer.parseInt(cityPower);
                        archerPower = (Integer.parseInt(dataSnapshot.child("Market_soldiers").child("Archer").child("amountBoughtCum").getValue().toString()))*
                                (Integer.parseInt(dataSnapshot.child("Market_soldiers").child("Archer").child("offensive").getValue().toString()));
                        swordsmenPower = (Integer.parseInt(dataSnapshot.child("Market_soldiers").child("Swordsmen").child("amountBoughtCum").getValue().toString()))*
                                (Integer.parseInt(dataSnapshot.child("Market_soldiers").child("Swordsmen").child("offensive").getValue().toString()));
                        spearmenPower = (Integer.parseInt(dataSnapshot.child("Market_soldiers").child("Spearmen").child("amountBoughtCum").getValue().toString()))*
                                (Integer.parseInt(dataSnapshot.child("Market_soldiers").child("Spearmen").child("offensive").getValue().toString()));
                        cavalryPower = (Integer.parseInt(dataSnapshot.child("Market_soldiers").child("Cavalry").child("amountBoughtCum").getValue().toString()))*
                                (Integer.parseInt(dataSnapshot.child("Market_soldiers").child("Cavalry").child("offensive").getValue().toString()));
                        Integer newUserPower = (archerPower+swordsmenPower+spearmenPower+cavalryPower);


                        // If lost
                        String coins = dataSnapshot.child("Coins").child("totalCoins").getValue().toString();
                        Integer money = Integer.parseInt(coins);
                        Integer moneyRobbed = 50;

                        String pop = dataSnapshot.child("population").child("currPopulation").getValue().toString();
                        Integer population = Integer.parseInt(pop);
                        int peopleKilled = 5;

                        //If won
                        int moneyWon = 500;
                        int peopleAcquired = 35;
                        final Toast toast;
                        if (newUserPower > newCityPower) {
                            mRef.child("Coins").child("totalCoins").setValue(money + moneyWon);
                            mRef.child("population").child("currPopulation").setValue(population + peopleAcquired);
                            toast = Toast.makeText(v.getContext().getApplicationContext(), "You won! Congratulations!\nYour population increased and you are promply rewarded\n+500 coins, +35 people", Toast.LENGTH_SHORT);
                            toast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 2000);
                            battleWon = Integer.parseInt(dataSnapshot.child("Battles").child("battlesWon").getValue().toString());
                            mRef.child("Battles").child("battlesWon").setValue(battleWon + 1);
                            battleFought = Integer.parseInt(dataSnapshot.child("Battles").child("battlesFought").getValue().toString());
                            mRef.child("Battles").child("battlesFought").setValue(battleFought + 1);
                        } else {
                            toast = Toast.makeText(v.getContext().getApplicationContext(), "You lost!\nYour army has been depleted and bank robbed", Toast.LENGTH_SHORT);
                            toast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 2000);
                          mRef.child("Coins").child("totalCoins").setValue(money-moneyRobbed);
                          mRef.child("population").child("currPopulation").setValue(population-peopleKilled);
                            battleFought = Integer.parseInt(dataSnapshot.child("Battles").child("battlesFought").getValue().toString());
                            mRef.child("Battles").child("battlesFought").setValue(battleFought + 1);
                        }

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
