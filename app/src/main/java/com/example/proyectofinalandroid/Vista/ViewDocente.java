package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalandroid.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewDocente extends AppCompatActivity {

    int idDocente;
    long documento;
    Button clases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_docente);

        Bundle b = getIntent().getExtras();
        idDocente = b.getInt("docenteId");
        documento = b.getLong("documento");

        getSupportActionBar().hide();

    }

    public void clases(View view) {
        Intent intentClases = new Intent(this, ViewClases.class);
        intentClases.putExtra("docenteId", idDocente);
        intentClases.putExtra("documento", documento);
        startActivity(intentClases);
    }

    public void irAForos(View view) {
        Intent i = new Intent(getApplicationContext(), ViewRegistroForo.class);
        i.putExtra("docenteId", idDocente);
        i.putExtra("documento", documento);
        startActivity(i);
    }

    public void listarForos(View view) {
        Intent intent = new Intent(getApplicationContext(), ListadoForos.class);
        intent.putExtra("docenteId", idDocente);
        intent.putExtra("documento", documento);
        startActivity(intent);
    }

    public void editarPerfil(View view){
        Intent intent = new Intent(this, ViewEditarPerfilDocente.class);
        intent.putExtra("idDocente", idDocente);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}