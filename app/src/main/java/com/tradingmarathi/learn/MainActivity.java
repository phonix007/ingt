package com.tradingmarathi.learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;


import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;

public class MainActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {

    InAppUpdateManager inAppUpdateManager;
    BottomNavigationView navigationView;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // hides status bar


        //update ke liye coding
        inAppUpdateManager = InAppUpdateManager.Builder(this, 101)
                .resumeUpdates(true)
                .mode(Constants.UpdateMode.IMMEDIATE)   // Immdiate Means Forcefully Install And Felexiable means no thanks option
                .snackBarAction("An update has just been downloaded")
                .snackBarAction("RESTART")
                .handler(this);
        inAppUpdateManager.checkForAppUpdate();

        navigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_cointainer, new HomeFragment()).commit();
        navigationView.setSelectedItemId(R.id.nav_home);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.nav_account:
                        fragment = new AccountFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_cointainer, fragment).commit();

                return true;
            }
        });
    }


    @Override
    public void onInAppUpdateError(int code, Throwable error) {

    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {

        if (status.isDownloaded()) {
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view,
                    "App has been downloaded successfullly",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inAppUpdateManager.completeUpdate();

                }
            });

            snackbar.show();

        }

    }

    @Override
    public void onBackPressed() { //double press to exit
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}