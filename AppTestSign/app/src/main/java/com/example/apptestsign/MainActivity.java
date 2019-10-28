package com.example.apptestsign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private SignCert sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sc.checkAppSignature(getApplicationContext());
        sc.checkAppSignature(this);
    }
}
