package com.example.faxianchina.faxian;

import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class Frag_Market extends Fragment {


private FirebaseStorage storage = FirebaseStorage.getInstance();
    // References to Firebase Storage
    private StorageReference bowstorage;
    private StorageReference swordstorage;
    private StorageReference spearstorage;
    private StorageReference horsestorage;

    private TextView bows, swords, spears,horses;
    private ImageView bow, sword, spear, horse;

    // Button objects
    private Button buyBowButton, buySwordButton, buySpearButton, buyHorseButton;

    // References to Firebase RealTime Database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = database.getReference();
    private DatabaseReference mRef2 = database.getReference();

    // Primitive data types
    private String money;
    private Integer haveBows;
    private Integer bowUnit,swordUnit, numBows, numSpears, numSwords, numHorses, spearUnit, horseUnit, bowTotal, swordTotal,
            spearTotal, horseTotal, balance;

    // NumberPicker objects
    private NumberPicker bowNum,swordNum, spearNum, horseNum;

    public Frag_Market(){

    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Variables to hold Storage database links for weapon photos
        bowstorage = storage.getReferenceFromUrl("gs://faxian-china.appspot.com/bow.png");
        swordstorage = storage.getReferenceFromUrl("gs://faxian-china.appspot.com/sword2.png");
        spearstorage = storage.getReferenceFromUrl("gs://faxian-china.appspot.com/spear.png");
        horsestorage = storage.getReferenceFromUrl("gs://faxian-china.appspot.com/horse.png");
        View v = inflater.inflate(R.layout.frag_market, container,false);

        // Buttons to buy weapons
        buyBowButton = (Button)v.findViewById(R.id.bowButton);
        buySwordButton = (Button)v.findViewById(R.id.swordButton);
        buySpearButton = (Button)v.findViewById(R.id.spearButton);
        buyHorseButton = (Button)v.findViewById(R.id.horseButton);

        //Text Views displaying amount of each weapon bought
        bows = (TextView)v.findViewById(R.id.haveBows);
        swords = (TextView)v.findViewById(R.id.haveSwords);
        spears = (TextView)v.findViewById(R.id.haveSpears);
        horses = (TextView)v.findViewById(R.id.haveHorses);

        // Initializing the Number Pickers and their value bounds
        bowNum = (NumberPicker)v.findViewById(R.id.bowpicker);
        bowNum.setMinValue(1);
        bowNum.setMaxValue(25);
        swordNum = (NumberPicker) v.findViewById(R.id.swordpicker);
        swordNum.setMinValue(1);
        swordNum.setMaxValue(25);
        spearNum = (NumberPicker) v.findViewById(R.id.spearpicker);
        spearNum.setMinValue(1);
        spearNum.setMaxValue(25);
        horseNum = (NumberPicker) v.findViewById(R.id.horsepicker);
        horseNum.setMinValue(1);
        horseNum.setMaxValue(25);

        //initialize values
        numBows = 0;
        numSpears = 0;
        numSwords = 0;
        numHorses = 0;

        // Initailizing the ImageView objects with the result from the storage links
        bow = (ImageView) v.findViewById(R.id.bow);
        Glide.with(this).using(new FirebaseImageLoader()).load(bowstorage).into(bow);
        sword = (ImageView) v.findViewById(R.id.sword);
        Glide.with(this).using(new FirebaseImageLoader()).load(swordstorage).into(sword);
        spear = (ImageView) v.findViewById(R.id.spear);
        Glide.with(this).using(new FirebaseImageLoader()).load(spearstorage).into(spear);
        horse = (ImageView) v.findViewById(R.id.horse);
        Glide.with(this).using(new FirebaseImageLoader()).load(horsestorage).into(horse);



        /* Interaction with database on buying Bows
        *  1. Get the amount of bows currently owned from database and current balance.
        *  2. Caluclate total of bows user wants to buy multipled by price of each bow
        *  3. Update the UI Bow counter to the amount bought
        * */
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                money = dataSnapshot.child("Coins").child("totalCoins").getValue().toString();
                balance = Integer.parseInt(money);
                haveBows = Integer.parseInt(dataSnapshot.child("Market_weapons").child("Bow").child("amountBoughtCum").getValue().toString());

                bowUnit = bowNum.getValue();
                bowTotal = bowUnit * 15;

                bowNum.setOnScrollListener(new NumberPicker.OnScrollListener() {

                    @Override
                    public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                            bowUnit = bowNum.getValue();
                            bowTotal = bowUnit * 15;
                        }
                    }
                });
                bows.setText(dataSnapshot.child("Market_weapons").child("Bow").child("amountBought").getValue().toString());

                /* Interaction with Buy Button for bows
                * 1. Verify that user has enough money to purchase the desired amount of bows
                *
                * 2. If yes, grab and subtract the price of X bows from total balance
                * 2a. Change the counter of bows owned
                * 2b. Fire a Toast message, confirming the purchase.
                *
                * 3. If no, fire a Toast message, telling user that they have insufficient funds and/or insufficient weapons purchased
                * */
                buyBowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (balance == bowTotal || balance > bowTotal) {
                            //Database value change
                            mRef.child("Coins").child("totalCoins").setValue(balance - bowTotal);
                            numBows = numBows + bowUnit;
                            mRef.child("Market_weapons").child("Bow").child("amountBought").setValue(numBows);
                            //bows = Integer.parseInt(dataSnapshot.child("Market_weapons").child("Bow").child("amountBoughtCum").getValue().toString());
                            // ^ commented that out to make it work
                            bows.setText(dataSnapshot.child("Market_weapons").child("Bow").child("amountBoughtCum").getValue().toString());

                            final Toast bowsToast = Toast.makeText(v.getContext().getApplicationContext(), "You bought " + bowUnit + " bows!", Toast.LENGTH_SHORT);
                            bowsToast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bowsToast.cancel();
                                }
                            }, 2000);
                        } else {
                            final Toast bowsToast = Toast.makeText(v.getContext().getApplicationContext(), "Insufficient funds, no bows bought!", Toast.LENGTH_SHORT);
                            bowsToast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bowsToast.cancel();
                                }
                            }, 2000);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /* Interaction with database on buying Swords
         *  1. Get the amount of swords currently owned from database and current balance.
         *  2. Caluclate total of swords user wants to buy multipled by price of each swords
         *  3. Update the UI Swords counter to the amount bought
         * */
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                money = dataSnapshot.child("Coins").child("totalCoins").getValue().toString();
                balance = Integer.parseInt(money);

                // haveBows = Integer.parseInt(dataSnapshot.child("Market_weapons").child("amountBoughtCum").getValue().toString());
                swordUnit = swordNum.getValue();
                swordTotal = swordUnit * 30;

                swordNum.setOnScrollListener(new NumberPicker.OnScrollListener() {

                    @Override
                    public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                            swordUnit = swordNum.getValue();
                            swordTotal = swordUnit * 30;
                        }
                    }
                });
                swords.setText(dataSnapshot.child("Market_weapons").child("Sword").child("amountBought").getValue().toString());

                /* Interaction with Buy Button for swords
                 * 1. Verify that user has enough money to purchase the desired amount of swords
                 *
                 * 2. If yes, grab and subtract the price of X swords from total balance
                 * 2a. Change the counter of swords owned
                 * 2b. Fire a Toast message, confirming the purchase.
                 *
                 * 3. If no, fire a Toast message, telling user that they have insufficient funds and/or insufficient weapons purchased
                 * */
                buySwordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (balance == swordTotal || balance > swordTotal) {
                            //Database value change
                            mRef.child("Coins").child("totalCoins").setValue(balance - swordTotal);
                            mRef.child("Market_weapons").child("Sword").child("amountBought").setValue(swordUnit);
                            numSwords = numSwords + swordUnit;
                            mRef.child("Market_weapons").child("Sword").child("amountBought").setValue(numSwords);
                            final Toast swordsToast = Toast.makeText(v.getContext().getApplicationContext(), "You bought " + swordUnit + " swords!", Toast.LENGTH_SHORT);
                            swordsToast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    swordsToast.cancel();
                                }
                            }, 2000);
                        } else {
                            final Toast swordsToast = Toast.makeText(v.getContext().getApplicationContext(), "Insufficient funds, no swords bought!", Toast.LENGTH_SHORT);
                            swordsToast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    swordsToast.cancel();
                                }
                            }, 2000);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /* Interaction with database on buying Spears
         *  1. Get the amount of spears currently owned from database and current balance.
         *  2. Caluclate total of spears user wants to buy multipled by price of each swords
         *  3. Update the UI Spears counter to the amount bought
         * */
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                money = dataSnapshot.child("Coins").child("totalCoins").getValue().toString();
                balance = Integer.parseInt(money);

                // haveBows = Integer.parseInt(dataSnapshot.child("Market_weapons").child("amountBoughtCum").getValue().toString());
                spearUnit = spearNum.getValue();
                spearTotal = spearUnit * 60;

                spearNum.setOnScrollListener(new NumberPicker.OnScrollListener() {

                    @Override
                    public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                            spearUnit = spearNum.getValue();
                            spearTotal = spearUnit * 60;
                        }
                    }
                });
                spears.setText(dataSnapshot.child("Market_weapons").child("Spear").child("amountBought").getValue().toString());

                /* Interaction with Buy Button for spears
                 * 1. Verify that user has enough money to purchase the desired amount of spears
                 *
                 * 2. If yes, grab and subtract the price of X spears from total balance
                 * 2a. Change the counter of spears owned
                 * 2b. Fire a Toast message, confirming the purchase.
                 *
                 * 3. If no, fire a Toast message, telling user that they have insufficient funds and/or insufficient weapons purchased
                 * */
                buySpearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (balance == spearTotal || balance > spearTotal) {
                            //Database value change
                            mRef.child("Coins").child("totalCoins").setValue(balance - spearTotal);
                            mRef.child("Market_weapons").child("Spear").child("amountBought").setValue(spearUnit);
                            numSpears = numSpears + spearUnit;
                            mRef.child("Market_weapons").child("Spear").child("amountBought").setValue(numSpears);
                            final Toast spearsToast = Toast.makeText(v.getContext().getApplicationContext(), "You bought " + spearUnit + " spears!", Toast.LENGTH_SHORT);
                            spearsToast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    spearsToast.cancel();
                                }
                            }, 2000);
                        } else {
                            final Toast spearsToast = Toast.makeText(v.getContext().getApplicationContext(), "Insufficient funds, no spears bought!", Toast.LENGTH_SHORT);
                            spearsToast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    spearsToast.cancel();
                                }
                            }, 2000);

                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /* Interaction with database on buying Horses
         *  1. Get the amount of horses currently owned from database and current balance.
         *  2. Caluclate total of horses user wants to buy multipled by price of each swords
         *  3. Update the UI Horses counter to the amount bought
         * */
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                money = dataSnapshot.child("Coins").child("totalCoins").getValue().toString();
                balance = Integer.parseInt(money);

                horseUnit = horseNum.getValue();
                horseTotal = horseUnit * 180;

                horseNum.setOnScrollListener(new NumberPicker.OnScrollListener() {

                    @Override
                    public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                            horseUnit = horseNum.getValue();
                            horseTotal = horseUnit * 180;
                        }
                    }
                });
                horses.setText(dataSnapshot.child("Market_weapons").child("Horse").child("amountBought").getValue().toString());

                /* Interaction with Buy Button for horses
                 * 1. Verify that user has enough money to purchase the desired amount of horses
                 *
                 * 2. If yes, grab and subtract the price of X horses from total balance
                 * 2a. Change the counter of horses owned
                 * 2b. Fire a Toast message, confirming the purchase.
                 *
                 * 3. If no, fire a Toast message, telling user that they have insufficient funds and/or insufficient weapons purchased
                 * */
                buyHorseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (balance == horseTotal || balance > horseTotal) {
                            //Database value change
                            mRef.child("Coins").child("totalCoins").setValue(balance - horseTotal);
                            mRef.child("Market_weapons").child("Horse").child("amountBought").setValue(horseUnit);
                            numHorses = numHorses + horseUnit;
                            mRef.child("Market_weapons").child("Horse").child("amountBought").setValue(numHorses);
                            final Toast horseToast = Toast.makeText(v.getContext().getApplicationContext(), "You bought " + horseUnit + " horses!", Toast.LENGTH_SHORT);
                            horseToast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    horseToast.cancel();
                                }
                            }, 2000);
                        } else {
                            final Toast horseToast = Toast.makeText(v.getContext().getApplicationContext(), "Insufficient funds, no horses bought!", Toast.LENGTH_SHORT);
                            horseToast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    horseToast.cancel();
                                }
                            }, 2000);
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