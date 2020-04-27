package com.example.myfirstapp;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class BarcodeReaderActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener{

    private static final String TAG = FirstView.class.getSimpleName();
    private BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_reader);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);

    }


    @Override
    public void onScanned(Barcode barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        System.out.println("\n\n\nON SCANNED\n\n\n");
        Toast.makeText(this, "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
        //createArView();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
