package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalandroid.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewClases extends AppCompatActivity {

    Button gestionar, listado, solicitudes;
    int idDocente;
    long documento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clases);

        gestionar = (Button) findViewById(R.id.jbtnGestionar);
        listado = (Button) findViewById(R.id.jbtnListado);
        solicitudes = (Button) findViewById(R.id.jbtnSolicitudes);

        Bundle b = getIntent().getExtras();
        idDocente = b.getInt("docenteId");
        documento = b.getLong("documento");

        getSupportActionBar().hide();

    }

    public void crear(View v) {
        Intent i = new Intent(this, ViewRegistroClase.class);
        i.putExtra("idDocente", idDocente);
        i.putExtra("documento", documento);
        startActivity(i);
    }

    public void listado(View v) {
        Intent i = new Intent(this, ListadoClases.class);
        i.putExtra("idDocente", idDocente);
        i.putExtra("documento", documento);
        startActivity(i);
    }

    public void solicitudes(View v) {
        Intent i = new Intent(this, ViewSolicitudes.class);
        i.putExtra("idDocente", idDocente);
        i.putExtra("documento", documento);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ViewDocente.class);
        i.putExtra("docenteId", idDocente);
        i.putExtra("documento", documento);
        startActivity(i);
    }
}