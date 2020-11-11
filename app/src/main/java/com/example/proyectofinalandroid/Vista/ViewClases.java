package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalandroid.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ViewClases extends AppCompatActivity {

    Button gestionar, listado, solicitudes;
    int idDocente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clases);

        gestionar = (Button) findViewById(R.id.jbtnGestionar);
        listado = (Button) findViewById(R.id.jbtnListado);
        solicitudes = (Button) findViewById(R.id.jbtnSolicitudes);

        Bundle b = getIntent().getExtras();
        idDocente = b.getInt("docenteId");

        getSupportActionBar().hide();

    }

    public void crear(View v){
        Intent i = new Intent(this, ViewRegistroClase.class);
        i.putExtra("idDocente", idDocente);
        startActivity(i);
    }

    public void listado(View v){
        Intent i = new Intent(this, ListadoClases.class);
        i.putExtra("idDocente", idDocente);
        startActivity(i);
    }

}