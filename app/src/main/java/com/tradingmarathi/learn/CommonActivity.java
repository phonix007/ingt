package com.tradingmarathi.learn;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommonActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<questionModel> questionModelArrayList;
    qusAdapter qusAdapter;
    FirebaseFirestore db;
    private Dialog loadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        questionModelArrayList = new ArrayList<questionModel>();
        qusAdapter = new qusAdapter(CommonActivity.this, questionModelArrayList);

        loadingDialog.show();

        recyclerView.setAdapter(qusAdapter);

        EventChangeListner();

    }

    private void EventChangeListner() {
        db.collection("questionscoll")  // Collection Name
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();
                            Log.e("FireStore Error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                questionModelArrayList.add(dc.getDocument().toObject(questionModel.class)); //
                            }
                            qusAdapter.notifyDataSetChanged(); //
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();
                        }
                    }
                });
    }

}