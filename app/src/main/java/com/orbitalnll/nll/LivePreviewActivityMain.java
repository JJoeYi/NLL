/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orbitalnll.nll;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.annotation.KeepName;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import com.orbitalnll.nll.camera.CameraSource;
import com.orbitalnll.nll.camera.CameraSourcePreview;
import com.orbitalnll.nll.camera.GraphicOverlay;
import com.orbitalnll.nll.posedetector.PoseDetectorProcessor;
import com.orbitalnll.nll.preference.PreferenceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Live preview demo for ML Kit APIs. */
@KeepName
public final class LivePreviewActivityMain extends AppCompatActivity
    implements CompoundButton.OnCheckedChangeListener, OnItemSelectedListener, BottomNavigationView.OnItemSelectedListener {
  private static final String POSE_DETECTION = "Pose Detection";
  private static final String TAG = "LivePreviewActivity";

  private static final String PUSH_UP = "Push Up";
  private static final String SQUAT = " Squat";
  private static final String SIT_UP = "Sit Up";


  private CameraSource cameraSource = null;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private String selectedModel = POSE_DETECTION;
  private String selectedEx = PUSH_UP;
  private ArrayList<String> REQUIRED_RUNTIME_PERMISSIONS = new ArrayList<String>();
  private PoseDetectorProcessor myPP;
  private BottomNavigationView bNV;
  private ToggleButton cameraFlip;
  private ToggleButton flashFlip;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //TODO: add logs
    Log.d(TAG, "onCreate");

    setContentView(R.layout.activity_vision_live_preview);

    REQUIRED_RUNTIME_PERMISSIONS.add(Manifest.permission.CAMERA);
    REQUIRED_RUNTIME_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    REQUIRED_RUNTIME_PERMISSIONS.add(Manifest.permission.READ_EXTERNAL_STORAGE);

    //TODO: ask for permission through android systems. Right now need to go to settings -> nll
    // -> permissions. Refer to EntryChoiceActivity. note that its in kotlin.
//    if (!allRuntimePermissionsGranted()) {
//      getRuntimePermissions()
//    }

    preview = findViewById(R.id.preview_view);
    if (preview == null) {
      Log.d(TAG, "Preview is null");
    }
    graphicOverlay = findViewById(R.id.graphic_overlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    // Bottom Navigation
    bNV = findViewById(R.id.bottom_nav_layout).findViewById(R.id.bottom_navigation);
    bNV.setSelectedItemId(R.id.page_smartrack);
    bNV.setOnItemSelectedListener(this);




    Spinner spinner = findViewById(R.id.spinner);
    List<String> exerciseList = new ArrayList<>();
    exerciseList.add(PUSH_UP);
    exerciseList.add(SQUAT);
    exerciseList.add(SIT_UP);


    // Creating adapter for spinner
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_style, exerciseList);
    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // attaching data adapter to spinner
    spinner.setAdapter(dataAdapter);
    spinner.setOnItemSelectedListener(this);

    // Camera Flip
    cameraFlip = findViewById(R.id.facing_switch);
    cameraFlip.setOnCheckedChangeListener(this);

    // Flash Flip
    flashFlip = findViewById(R.id.flash_button);
    flashFlip.setOnCheckedChangeListener(this);

   //TODO: settings button removed

    createCameraSource(selectedModel);
    startCameraSource();
  }

  @Override
  public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedEx = parent.getItemAtPosition(pos).toString();
    Log.d(TAG, "Selected exercise: " + selectedEx);
    preview.stop();
    createCameraSource(selectedEx);
    startCameraSource();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    int id = buttonView.getId();

    switch (id) {
      case R.id.facing_switch:
        Log.d(TAG, "Set facing");
        if (cameraSource != null) {

          // turn off flash before flipping camera
          if (flashFlip.isChecked()) {
            cameraSource.updateCameraFlash(Camera.Parameters.FLASH_MODE_OFF);
            flashFlip.setChecked(false);
          }

          if (cameraFlip.isChecked()) {
//            cameraFlip.setChecked(false);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
          } else {
//            cameraFlip.setChecked(true);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
          }
        }
        preview.stop();
        startCameraSource();


      case R.id.flash_button:
        Log.d(TAG, "Set flash");
        if (cameraSource != null && getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
          if (flashFlip.isChecked()) {
            if (cameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_BACK) {
              cameraSource.updateCameraFlash(Camera.Parameters.FLASH_MODE_TORCH);
            }

            if (cameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_FRONT) {
              Toast.makeText(this, "No flash allowed on front camera", Toast.LENGTH_SHORT).show();
              flashFlip.setChecked(false);
            }
          } else {
            cameraSource.updateCameraFlash(Camera.Parameters.FLASH_MODE_OFF);
          }

        }
    }

  }

  private void createCameraSource(String exercise) {
    // If there's no existing cameraSource, create one.
    if (cameraSource == null) {
      cameraSource = new CameraSource(this, graphicOverlay);
    }

    // pass Pose dectector into CameraSource + Options
    PoseDetectorOptionsBase poseDetectorOptions =
            PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
    Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
    boolean shouldShowInFrameLikelihood =
            PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
    boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
    boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
    boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);

    cameraSource.setMachineLearningFrameProcessor(new PoseDetectorProcessor(
            this,
            poseDetectorOptions,
            shouldShowInFrameLikelihood,
            visualizeZ,
            rescaleZ,
            runClassification,
            /* isStreamMode = */ true,
            exercise));

    myPP = (new PoseDetectorProcessor( // NEW SHIT
            this,
            poseDetectorOptions,
            shouldShowInFrameLikelihood,
            visualizeZ,
            rescaleZ,
            runClassification,
            /* isStreamMode = */ true));

//    try {
//      switch (exercise) {
//        case PUSH_UP:
//          break;
//
//        case SQUAT:
//          break;
//
//        case SIT_UP:
//          break;
//
//        default:
//          Log.e(TAG, "Unknown selected exercise: " + exercise);
//      }
//
//    } catch (RuntimeException e) {
//      Log.e(TAG, "Can not process selected exercise: " + exercise, e);
//      Toast.makeText(
//              getApplicationContext(),
//              "Can not process selected exercise: " + e.getMessage(),
//              Toast.LENGTH_LONG)
//          .show();
//    }
  }

  /**
   * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  private void startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null");
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null");
        }
        preview.start(cameraSource, graphicOverlay);
      } catch (IOException e) {
        Log.e(TAG, "Unable to start camera source.", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    createCameraSource(selectedModel);
    startCameraSource();
  }

  /** Stops the camera. */
  @Override
  protected void onPause() {
    super.onPause();
    preview.stop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (cameraSource != null) {
      cameraSource.release();
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.page_smartrack:
        Toast.makeText(getApplicationContext(), "already on SmarTrack page!", Toast.LENGTH_SHORT).show();
        return true;

      case R.id.page_play:
        Toast.makeText(getApplicationContext(), "play pressed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), PlayActivity.class));
        return true;

      case R.id.page_vault:
        Toast.makeText(getApplicationContext(), "vault pressed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), VaultActivity.class));
        return true;
      default:
        return false;
    }
  }

}
