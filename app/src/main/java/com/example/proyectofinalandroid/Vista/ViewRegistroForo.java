package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Controlador.CtlForo;
import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Clase;
import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceClase;
import com.example.proyectofinalandroid.Util.ServiceDocente;
import com.example.proyectofinalandroid.Util.ServiceForo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewRegistroForo extends AppCompatActivity {
    Button boton;

    TextView titulo, descripcion, limiteParticipacion;
    Switch estado;
    CtlForo controladorForo;
    Spinner grado;
    List<Clase> listaClases = new ArrayList<Clase>();
    int idDocente;
    List<String> grados = new ArrayList<>();
    Docente docente = new Docente();
    long documento;

    // Junior url
    final String url = "http://192.168.1.92:1000";
    // Malejo url
    //final String url = "http://192.168.1.9:1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro_foro);

        grado = (Spinner) findViewById(R.id.jspnGrado);
        titulo = (TextView) findViewById(R.id.campoTitulo);
        descripcion = (TextView) findViewById(R.id.jtxtDescripcion);
        limiteParticipacion = (TextView) findViewById(R.id.jtxtLimite);
        estado = (Switch) findViewById(R.id.switchDisponible);
        controladorForo = new CtlForo();
        boton = (Button) findViewById(R.id.jbtnRegistrarme);
        boton.setOnClickListener(this::registrarForo);

        getSupportActionBar().hide();

        Bundle b = getIntent().getExtras();
        idDocente = b.getInt("docenteId");
        documento = b.getLong("documento");

        llenarSpinnerGrados();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceDocente serviceDocente = retrofit.create(ServiceDocente.class);
        Call<Docente> docent = serviceDocente.buscar(documento);
        docent.enqueue(new Callback<Docente>() {
            @Override
            public void onResponse(Call<Docente> call, Response<Docente> response) {
                try {
                    if (response.isSuccessful()) {
                        docente = response.body();
                        return;
                    }
                    Toast.makeText(ViewRegistroForo.this, "Error buscando el docente.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Docente> call, Throwable t) {
                Toast.makeText(ViewRegistroForo.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarSpinnerGrados() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceClase serviceClase = retrofit.create(ServiceClase.class);
        Call<List<Clase>> clases = serviceClase.buscarPorDocente(idDocente);
        clases.enqueue(new Callback<List<Clase>>() {
            @Override
            public void onResponse(Call<List<Clase>> call, Response<List<Clase>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaClases = response.body();
                        llenarSpinner(listaClases, 0);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Clase>> call, Throwable t) {
                Toast.makeText(ViewRegistroForo.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarSpinner(List<Clase> listaClases, int x) {
        if (listaClases.size() == x) {
            if (x == 0) {
                grados.add("Seleccionar");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grados);
                grado.setAdapter(adapter);
                return;
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grados);
                grado.setAdapter(adapter);
                return;
            }
        } else if (x == 0) {
            grados.add("Seleccionar");
            grados.add(listaClases.get(x).getGrado());
            llenarSpinner(listaClases, x + 1);
        } else {
            grados.add(listaClases.get(x).getGrado());
            llenarSpinner(listaClases, x + 1);
        }
    }

    public void registrarForo(View view) {
        if (validarCampos()) {
            Toast.makeText(this, "Llena todos los campos adecuadamente.", Toast.LENGTH_SHORT).show();
        } else {

            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.63:1000").addConverterFactory(GsonConverterFactory.create()).build();
            ServiceForo serviceForo = retrofit.create(ServiceForo.class);
            boolean switchActivo = false;
            try {
                if (estado.isChecked()) {
                    switchActivo = true;
                }
                Foro foro = new Foro(titulo.getText().toString(), descripcion.getText().toString(),
                        switchActivo, Integer.valueOf(limiteParticipacion.getText().toString()), docente.getId());
                foro.setGrado(grado.getSelectedItem().toString());
                if (controladorForo.registrarse(foro, 0)) {
                    Toast.makeText(getApplicationContext(), "¡El foro ha sido registrado!", Toast.LENGTH_SHORT).show();
                    limpiar();
                } else {
                    Toast.makeText(getApplicationContext(), "Algo salió mal.", Toast.LENGTH_SHORT).show();
                }
            } catch (OcurrioUnErrorGuardandoException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void limpiar() {

        titulo.setText("");
        descripcion.setText("");
        limiteParticipacion.setText("");
        estado.setChecked(false);
    }

    private boolean validarCampos() {
        if (titulo.getText().toString().equals("") || descripcion.getText().toString().equals("") ||
                limiteParticipacion.getText().toString().equals("") || grado.getSelectedItem().toString().equals("Seleccionar")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewDocente.class);
        intent.putExtra("docenteId", idDocente);
        intent.putExtra("documento", documento);
        startActivity(intent);
    }
}