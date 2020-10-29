package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalandroid.R;
import android.os.Bundle;

public class ViewEstudiante extends AppCompatActivity {

    int idEstudiante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_estudiante);

        Bundle b = getIntent().getExtras();
        idEstudiante = b.getInt("estudianteId");

        getSupportActionBar().hide();

    }
}