package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalandroid.R;
import android.os.Bundle;

public class ViewDocente extends AppCompatActivity {

    int idDocente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_docente);

        Bundle b = getIntent().getExtras();
        idDocente = b.getInt("docenteId");

        getSupportActionBar().hide();

    }
}