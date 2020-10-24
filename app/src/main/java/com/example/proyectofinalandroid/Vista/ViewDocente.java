package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalandroid.R;
import android.os.Bundle;

public class ViewDocente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_docente);

        getSupportActionBar().hide();

    }
}