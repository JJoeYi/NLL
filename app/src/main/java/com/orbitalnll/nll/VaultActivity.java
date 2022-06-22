package com.orbitalnll.nll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class VaultActivity extends AppCompatActivity
        implements BottomNavigationView.OnItemSelectedListener {

    private ArrayList<String> REQUIRED_RUNTIME_PERMISSIONS = new ArrayList<String>();
    //TODO: add logs
    private static final String TAG = "VaultActivity";
    private BottomNavigationView bNV;
    NavController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_vault);

        // Bottom Navigation
        bNV = findViewById(R.id.bottom_nav_layout).findViewById(R.id.bottom_navigation);
        bNV.setSelectedItemId(R.id.page_vault);
        bNV.setOnItemSelectedListener(this);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        controller = navHostFragment.getNavController();
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
                Toast.makeText(getApplicationContext(), "Play! pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), PlayActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;

            case R.id.page_vault:
                Toast.makeText(getApplicationContext(), "already on vault page", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

//    private void setCurrentFragment(Fragment f) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.vault_frame_layout, f).commit();
//    }


}
