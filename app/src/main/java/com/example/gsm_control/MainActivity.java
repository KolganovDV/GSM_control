package com.example.gsm_control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_READ_SMS = 1;
    private static final int REQUEST_CODE_PERMISSION_SEND_SMS = 2;
    private static final int REQUEST_CODE_PERMISSION_RECEIVE_SMS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                    REQUEST_CODE_PERMISSION_SEND_SMS);
        }
        permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_SMS},
                    REQUEST_CODE_PERMISSION_RECEIVE_SMS);
        }
        permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS},
                    REQUEST_CODE_PERMISSION_READ_SMS);
        }

    }
}