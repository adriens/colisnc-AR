package com.example.myfirstapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;


public class MainActivity extends AppCompatActivity {

    TextView barcodeResult;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeResult = (TextView)findViewById(R.id.barcode_result);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void NextView(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, FirstView.class);
        startActivity(intent);
    }

    public void scanBarcode(View v){

        Intent intent = new Intent(this, BarcodeReaderActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         if(requestCode==0){
             if(resultCode== CommonStatusCodes.SUCCESS){
                 if(data!=null){
                     Barcode barcode = data.getParcelableExtra("barcode");
                     barcodeResult.setText("Barcode value :"+barcode.displayValue);
                 }
                 else{
                     barcodeResult.setText("No Barcode found");
                 }
             }

         }
         else {
             super.onActivityResult(requestCode, resultCode, data);
         }
    }
}
