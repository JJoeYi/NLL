package com.orbitalnll.nll;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private final List<String> REQUIRED_RUNTIME_PERMISSIONS =
            Arrays.asList(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ImageView imageView = findViewById(R.id.permissionImage);
        imageView.setOnClickListener(this);

        // Get permissions
        if (allRuntimePermissionsGranted()) {
            startActivity(new Intent(getApplicationContext(), LivePreviewActivityMain.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            getRuntimePermissions();
            Toast.makeText(this,
                    "Please accept all permissions! Click the image," +
                            "Go to Settings -> App -> nll to accept",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (allRuntimePermissionsGranted()) {
            startActivity(new Intent(getApplicationContext(), LivePreviewActivityMain.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private boolean allRuntimePermissionsGranted() {
        for (String permission : REQUIRED_RUNTIME_PERMISSIONS) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void getRuntimePermissions() {
        List<String> toRequestList = new ArrayList<>();
        for (String permission : REQUIRED_RUNTIME_PERMISSIONS) {
            if (!isPermissionGranted(this, permission)) {
                toRequestList.add(permission);
            }
        }

        if (!toRequestList.isEmpty()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, toRequestList.get(0))) {

            }
            ActivityCompat.requestPermissions(this,
                    toRequestList.toArray(new String[0]), 1);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.permissionImage) {
            startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
            Toast.makeText(this, "search for nll and accept the permissions!", Toast.LENGTH_SHORT).show();
        }
    }
}
