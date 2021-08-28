package com.tradingmarathi.learn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

import java.util.ArrayList;
import java.util.List;

public class CandleActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listView;
    List<String> title_list, story_list, img_list;
    ArrayAdapter<String> adapter;
    MyBasic myBasic;


    private MoPubView moPubView;

    private Dialog loadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candle);

        SdkConfiguration.Builder sdkConfiguration = new SdkConfiguration.Builder(getString(R.string.mob_pub_banner));
        MoPub.initializeSdk(this, sdkConfiguration.build(), initSdkListener());


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        listView = findViewById(R.id.listView);
        databaseReference = FirebaseDatabase.getInstance().getReference("basicbook"); //
        myBasic = new MyBasic();
        title_list = new ArrayList<>();
        story_list = new ArrayList<>();
        img_list = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,R.layout.basic_iteam,R.id.iteamtext,title_list); // what you want to show on List. It  Shows only title

        loadingDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title_list.clear();
                story_list.clear();
                img_list.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    myBasic = ds.getValue(MyBasic.class);
                    title_list.add(myBasic.getTi());
                    story_list.add(myBasic.getTx());
                    img_list.add(myBasic.getUrl());
                }

                listView.setAdapter(adapter); // show title on list
                loadingDialog.dismiss();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // When click on any of the textview it will redirect  to the paratiular activity
                        Intent intent = new Intent(CandleActivity.this,Candle_Next_Activity.class);
                        String p = story_list.get(i);
                        String urlstr = img_list.get(i);
                        intent.putExtra("key",p);
                        intent.putExtra("url",urlstr);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                bannerAd();

            }
        };
    }

    private void bannerAd(){

        moPubView = (MoPubView) findViewById(R.id.adview);
        moPubView.setAdUnitId(getString(R.string.mob_pub_banner)); // Enter your Ad Unit ID from www.mopub.com
        moPubView.loadAd();

    }

    @Override
    protected void onDestroy() {
        moPubView.destroy();
        super.onDestroy();
    }

}