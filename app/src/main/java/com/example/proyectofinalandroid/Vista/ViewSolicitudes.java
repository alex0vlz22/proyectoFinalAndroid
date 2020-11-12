package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.proyectofinalandroid.R;

public class ViewSolicitudes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_solicitudes);

        getSupportActionBar().hide();

    }



}