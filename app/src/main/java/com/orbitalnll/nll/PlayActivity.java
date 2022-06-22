package com.orbitalnll.nll;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity
        implements BottomNavigationView.OnItemSelectedListener {

    private ArrayList<String> REQUIRED_RUNTIME_PERMISSIONS = new ArrayList<String>();
    //TODO: add logs
    private static final String TAG = "PlayActivity";
    private BottomNavigationView bNV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_play);

        REQUIRED_RUNTIME_PERMISSIONS.add(Manifest.permission.CAMERA);
        REQUIRED_RUNTIME_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        REQUIRED_RUNTIME_PERMISSIONS.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        // Bottom Navigation
        bNV = findViewById(R.id.bottom_nav_layout).findViewById(R.id.bottom_navigation);
        bNV.setSelectedItemId(R.id.page_play);
        bNV.setOnItemSelectedListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // For Bottom Navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.page_smartrack:
                Toast.makeText(getApplicationContext(), "smarTrack pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LivePreviewActivityMain.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;

            case R.id.page_play:
                Toast.makeText(getApplicationContext(), "already on Play! page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.page_vault:
                Toast.makeText(getApplicationContext(), "vault pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), VaultActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
            default:
                return false;
        }
    }
}
