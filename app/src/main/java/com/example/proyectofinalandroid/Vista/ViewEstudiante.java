package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalandroid.R;
import android.os.Bundle;

public class ViewEstudiante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_estudiante);

        getSupportActionBar().hide();

    }
}