package com.orbitalnll.nll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> REQUIRED_RUNTIME_PERMISSIONS = new ArrayList<String>();
    //TODO: add logs
    private static final String TAG = "PlayActivity";
    private MaterialButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_play);

        button = findViewById(R.id.play_btn);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.play_btn) {
            Intent intent = new Intent(PlayActivity.this, UnityPlayerActivity.class);
            startActivity(intent);
        }
    }
}
