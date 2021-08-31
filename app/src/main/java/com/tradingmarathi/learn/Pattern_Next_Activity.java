package com.tradingmarathi.learn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;


public class Pattern_Next_Activity extends AppCompatActivity {

    TextView txt,txttitle;
    ImageView img;

    ReviewManager manager;
    ReviewInfo reviewInfo;

    private Dialog loadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern__next_);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        txt = findViewById(R.id.next_tetx);
        img = findViewById(R.id.next_image);
        txttitle = findViewById(R.id.next_title);

        String t = getIntent().getStringExtra("key");
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        txt.setText(t);
        txttitle.setText(title);

        Glide.with(this)
                .load(url)
                .into(img);

        loadingDialog.dismiss();
    }
}