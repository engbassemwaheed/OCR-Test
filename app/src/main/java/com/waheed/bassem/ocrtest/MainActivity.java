package com.waheed.bassem.ocrtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.waheed.bassem.ocr.OcrInterface;
import com.waheed.bassem.ocr.OcrManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int CAMERA_PERMISSION = 1;
    private static final String API_KEY = "8e326757f088957";

    private TextView resultTextView;
    private MaterialButton materialButton;

    private OcrManager ocrManager;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

       ocrManager = OcrManager.getInstance(API_KEY);

        materialButton = findViewById(R.id.start_button);
        resultTextView = findViewById(R.id.result_text_view);

        materialButton.setOnClickListener(v -> {
           if (checkCameraPermission()) {
               startOCR();
           }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startOCR();
            }
        }
    }

    private void startOCR () {
        resultTextView.setVisibility(View.GONE);
        ocrManager.startOCR(fragmentManager, R.id.frame_layout, new OcrInterface() {
            @Override
            public void onOcrResult(ArrayList<String> text) {
                Log.d(TAG, "onOcrResult: text = " + text);
                String temp = "result \n" + text;
                resultTextView.setText(temp);
                materialButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int errorCode) {
                Log.d(TAG, "onError: errorCode = " + errorCode);
                String temp = "error code = " + errorCode;
//                        resultTextView.setText(temp);
                materialButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelPressed() {
                Log.d(TAG, "onCancelPressed");
                materialButton.setVisibility(View.VISIBLE);
            }
        });

        materialButton.setVisibility(View.GONE);
    }

    private boolean checkCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

}