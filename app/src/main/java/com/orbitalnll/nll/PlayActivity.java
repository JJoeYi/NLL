package com.orbitalnll.nll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener,
        BottomNavigationView.OnItemSelectedListener {

    private ArrayList<String> REQUIRED_RUNTIME_PERMISSIONS = new ArrayList<String>();
    //TODO: add logs
    private static final String TAG = "PlayActivity";
    private MaterialButton button;
    private BottomNavigationView bNV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_play);

        bNV = findViewById(R.id.bottom_nav_layout).findViewById(R.id.bottom_navigation);
        bNV.setOnItemSelectedListener(this);

        button = findViewById(R.id.play_btn);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.play_btn:
                Intent intent = new Intent(PlayActivity.this, UnityPlayerActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.page_smartrack:
//                Toast.makeText(getApplicationContext(), "smarTrack pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LivePreviewActivityMain.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;

            case R.id.page_play:
//                Toast.makeText(getApplicationContext(), "Play! pressed", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.page_vault:
//                Toast.makeText(getApplicationContext(), "vault pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), VaultActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
            default:
                return false;
        }
    }
}
