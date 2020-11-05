package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalandroid.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ViewDocente extends AppCompatActivity {

    int idDocente;
    long documento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_docente);

        Bundle b = getIntent().getExtras();
        idDocente = b.getInt("docenteId");
        documento = b.getLong("documento");

        getSupportActionBar().hide();

    }

    public void irAForos(View view) {
        Intent i = new Intent(getApplicationContext(), ViewRegistroForo.class);
        i.putExtra("docenteId", idDocente);
        i.putExtra("documento", documento);
        startActivity(i);
    }
}