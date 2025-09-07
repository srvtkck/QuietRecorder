package com.example.quietrecorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.quietrecorder.sensors.ShakeDetector;
import com.example.quietrecorder.recording.RecordingService;

public class MainActivity extends AppCompatActivity {

    private ShakeDetector shakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startBtn = findViewById(R.id.startBtn);
        Button stopBtn = findViewById(R.id.stopBtn);

        // İzin kontrolü
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }

        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecordingService.class);
            startService(intent);
        });

        stopBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecordingService.class);
            stopService(intent);
        });

        // Shake ile başlatma
        shakeDetector = new ShakeDetector(this, () -> {
            Intent intent = new Intent(this, RecordingService.class);
            startService(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeDetector.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.stop();
    }
}
