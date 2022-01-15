package com.waheed.bassem.ocrtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();

        String apiKey = "8e326757f088957";
        OcrManager ocrManager = OcrManager.getInstance(apiKey);

        MaterialButton materialButton = findViewById(R.id.start_button);
        TextView resultTextView = findViewById(R.id.result_text_view);

        materialButton.setOnClickListener(v -> {
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
        });
    }

}