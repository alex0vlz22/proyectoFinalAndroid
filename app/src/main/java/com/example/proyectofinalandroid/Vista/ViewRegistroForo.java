package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Controlador.CtlForo;
import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceDocente;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;
import com.example.proyectofinalandroid.Util.ServiceForo;

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
    //    final String url = "http://192.168.1.63:1000";
    // Malejo url
    final String url = "http://192.168.1.9:1000";
    Docente docente = new Docente();
    long documento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro_foro);
        titulo = (TextView) findViewById(R.id.jtxtTitulo);
        descripcion = (TextView) findViewById(R.id.jtxtDescripcion);
        limiteParticipacion = (TextView) findViewById(R.id.jtxtLimite);
        estado = (Switch) findViewById(R.id.switchDisponible);
        controladorForo = new CtlForo();
        boton = (Button) findViewById(R.id.jbtnRegistrarme);
        boton.setOnClickListener(this::registrarForo);
        getSupportActionBar().hide();
        Bundle b = getIntent().getExtras();
        int idDocente = b.getInt("docenteId");
        documento = b.getLong("documento");
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
                    Toast.makeText(ViewRegistroForo.this, "El docente no existe -_-", Toast.LENGTH_SHORT).show();
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
                Foro foro = new Foro(titulo.getText().toString(), descripcion.getText().toString(), switchActivo, Integer.valueOf(limiteParticipacion.getText().toString()), docente);
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
                limiteParticipacion.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }


}