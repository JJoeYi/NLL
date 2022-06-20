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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ToggleButton;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.annotation.KeepName;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import com.orbitalnll.nll.camera.CameraSource;
import com.orbitalnll.nll.camera.CameraSourcePreview;
import com.orbitalnll.nll.camera.GraphicOverlay;
import com.orbitalnll.nll.posedetector.PoseDetectorProcessor;
import com.orbitalnll.nll.preference.PreferenceUtils;
import com.orbitalnll.nll.preference.SettingsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Live preview demo for ML Kit APIs. */
@KeepName
public final class LivePreviewActivityMain extends AppCompatActivity
    implements CompoundButton.OnCheckedChangeListener, OnItemSelectedListener {
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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    ToggleButton facingSwitch = findViewById(R.id.facing_switch);
    facingSwitch.setOnCheckedChangeListener(this);

    ImageView settingsButton = findViewById(R.id.settings_button);
    //TODO: fix pressing settings button
    settingsButton.setOnClickListener(
        v -> {
          Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
          intent.putExtra(
              SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.LIVE_PREVIEW);
          startActivity(intent);
        });

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
    Log.d(TAG, "Set facing");
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
      } else {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
      }
    }
    preview.stop();
    startCameraSource();
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
}
