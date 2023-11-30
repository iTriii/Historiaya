package com.example.log_in;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.List;
//FOR UPDATE ONLY
public class QR_Scanner extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    ImageButton back;
    private CompoundBarcodeView qr_scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);
        qr_scanner = findViewById(R.id.qr_scanner);
        checkCameraPermission();

        back =findViewById(R.id.back);
        back.setOnClickListener(v -> openStoreManager());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startScanning();
        }
    }

    private void startScanning() {
        qr_scanner.decodeContinuous(result -> {
            if (result.getText() != null) {
                // Handle the scanned result (result.getText())
                Toast.makeText(this, "Scanned: " + result.getText(), Toast.LENGTH_SHORT).show();
                // You can pass this data back to your Store activity or use it in any other way
                finish(); // Close the QR scanner activity
            }
        });

        qr_scanner.setStatusText(""); // Remove the status text if you don't need it

        List<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE); // Specify the format to QR_CODE
        qr_scanner.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));

        qr_scanner.resume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qr_scanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qr_scanner.pause();
    }
    private void openStoreManager() {
        Intent intent = new Intent(this, StoreManager.class);
        startActivity(intent);
    }
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
        finish();
    }
}
