package com.example.faxianchina.faxian;

import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.media.MediaPlayer;

import org.w3c.dom.Text;

import com.example.faxianchina.faxian.Methods;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout frame;
    private TabLayout tabLayout;
    private FirebaseStorage storage = FirebaseStorage.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        createTextView(R.id.numofcoins);
        createTextView(R.id.numofppl);

        createStorage("gs://faxian-china.appspot.com/coins_icon.png");
        createStorage("gs://faxian-china.appspot.com/popu.png");

        createImage(R.id.coins);
        createImage(R.id.population);



        //download file
//        Glide.with(this).using(new FirebaseImageLoader()).load(createStorage("gs://faxian-china.appspot.com/coins_icon.png")).into(coinsImage);
//        Glide.with(this).using(new FirebaseImageLoader()).load(createStorage("gs://faxian-china.appspot.com/popu.png")).into(popuImage);

        // get the reference of FrameLayout and TabLayout
        frame = (ConstraintLayout) findViewById(R.id.framelayout);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        createTab();


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
        // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new Frag_Timeline();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                        break;
                    case 1:
                        fragment = new Frag_Map();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                        break;
                    case 2:
                        fragment = new Frag_Train();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                        break;
                    case 3:
                        fragment = new Frag_Market();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                        break;
                    case 4:
                        fragment = new Frag_History();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                        break;
                    case 5:
                        fragment = new Frag_Settings();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, fragment);
                for(int i = 0; i<=5;i++){
                    tab.getPosition();
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        View view1 = getLayoutInflater().inflate(R.layout.iconlayout, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.timeline_button);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1),0,true);

//        TabLayout.Tab map = tabLayout.newTab();
//        View view2 = getLayoutInflater().inflate(R.layout.iconlayout, null);
//        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.map);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));
//
//        TabLayout.Tab train = tabLayout.newTab();
//        View view3 = getLayoutInflater().inflate(R.layout.iconlayout, null);
//        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.trainbutton);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));
//
//
//        TabLayout.Tab market = tabLayout.newTab();
//        View view4 = getLayoutInflater().inflate(R.layout.iconlayout, null);
//        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.marketbutton);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));
//
//        TabLayout.Tab history = tabLayout.newTab();
//        View view5 = getLayoutInflater().inflate(R.layout.iconlayout, null);
//        view5.findViewById(R.id.icon).setBackgroundResource(R.drawable.history_button);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view5));
//
//        TabLayout.Tab setting = tabLayout.newTab();
//        View view6 = getLayoutInflater().inflate(R.layout.iconlayout, null);
//        view6.findViewById(R.id.icon).setBackgroundResource(R.drawable.settingbutton);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view6));


        DatabaseReference mRef = createDB();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String coinVal = dataSnapshot.child("Coins").child("totalCoins").getValue().toString();
                TextView coinNum = createTextView(R.id.numofcoins);
                coinNum.setText(coinVal);

                String popVal = dataSnapshot.child("population").child("currPopulation").getValue().toString();
                TextView popNum = createTextView(R.id.numofppl);
                popNum.setText(popVal);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT);
            }
        });

    }


    private void createTab(){
        //TabLayout.Tab map = tabLayout.newTab();

        View view2 = getLayoutInflater().inflate(R.layout.iconlayout, null);

        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.map);

        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        TabLayout.Tab train = tabLayout.newTab();
        View view3 = getLayoutInflater().inflate(R.layout.iconlayout, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.trainbutton);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));


        TabLayout.Tab market = tabLayout.newTab();
        View view4 = getLayoutInflater().inflate(R.layout.iconlayout, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.marketbutton);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));

        TabLayout.Tab history = tabLayout.newTab();
        View view5 = getLayoutInflater().inflate(R.layout.iconlayout, null);
        view5.findViewById(R.id.icon).setBackgroundResource(R.drawable.history_button);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view5));

        TabLayout.Tab setting = tabLayout.newTab();
        View view6 = getLayoutInflater().inflate(R.layout.iconlayout, null);
        view6.findViewById(R.id.icon).setBackgroundResource(R.drawable.settingbutton);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view6));

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();

    }

    private DatabaseReference createDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();
        return dbRef;
    }

    private StorageReference createStorage(String url){
        StorageReference storageReference = storage.getReferenceFromUrl(url);
        return storageReference;
    }
    private TextView createTextView(int id){
        TextView view = (TextView)findViewById(id);
        return view;
    }

    public ImageView createImage(int id){
        ImageView image = (ImageView) findViewById(id);
        return image;
    }
}



