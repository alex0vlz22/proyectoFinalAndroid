package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalandroid.Modelo.Clase;
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.Modelo.Solicitud;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceClase;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;
import com.example.proyectofinalandroid.Util.ServiceSolicitud;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewEstudiante extends AppCompatActivity {

    int idEstudiante;
    Estudiante estudianteIngresado = new Estudiante();
    Solicitud nuevaSolicitud = new Solicitud();

    // Junior url
    final String url = "http://192.168.1.2:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_estudiante);

        Bundle b = getIntent().getExtras();
        idEstudiante = b.getInt("estudianteId");

        llenarEstudianteIngresado();
        getSupportActionBar().hide();

    }

    private void llenarEstudianteIngresado() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceEstudiante serviceEstudiante = retrofit.create(ServiceEstudiante.class);
        Call<Estudiante> estudiante = serviceEstudiante.buscarPorId(idEstudiante);
        estudiante.enqueue(new Callback<Estudiante>() {
            @Override
            public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                try {
                    if (response.isSuccessful()) {
                        estudianteIngresado = response.body();
                        return;
                    } else {
                        imprimir("Error buscándote como estudiante en la base de datos.");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imprimir(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Estudiante> call, Throwable t) {
                Toast.makeText(ViewEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UnirmeAUnaClase(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.codigo_clase_estudiante, null);

        EditText codigo = view.findViewById(R.id.jtxtCodigo);
        Button solicitarUnirse = view.findViewById(R.id.jbtnSolicitarUnirse);

        solicitarUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codigo.getText().toString().equals("")) {
                    Toast.makeText(ViewEstudiante.this, "Por favor, ingresa un código...",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Verifico que exista una clase por dicho código
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
                    ServiceClase serviceClase = retrofit.create(ServiceClase.class);
                    Call<Clase> clase = serviceClase.buscarPorCodigo(codigo.getText().toString());
                    clase.enqueue(new Callback<Clase>() {
                        @Override
                        public void onResponse(Call<Clase> call, Response<Clase> response) {
                            try {
                                if (response.isSuccessful()) {
                                    Clase c = response.body();
                                    if (c == null) {
                                        imprimir("No se ha encontrado una clase por éste código.");
                                        return;
                                    } else {
                                        try {
                                            validarSolicitud(codigo.getText().toString(), c.getIdDocente(), c);
                                            codigo.setText("");
                                            return;
                                        } catch (Exception e) {
                                            imprimir(e.getMessage());
                                            return;
                                        }
                                    }
                                } else {
                                    imprimir("No se encontró una clase por este código.");
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                imprimir(e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<Clase> call, Throwable t) {
                            Toast.makeText(ViewEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
    }

    private void validarSolicitud(String codigo, int idDocente, Clase c) {
        // Se valida si ya hay una solicitud de dicho estudiante hacia la misma clase.
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceSolicitud serviceSolicitud = retrofit.create(ServiceSolicitud.class);
        Call<Solicitud> solicitud = serviceSolicitud.buscarPorIdEstudiante(idEstudiante);
        solicitud.enqueue(new Callback<Solicitud>() {
            @Override
            public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                try {
                    if (response.isSuccessful()) {
                        Solicitud s = response.body();
                        if (s.getCodigo().equals(codigo)) {
                            imprimir("Ya has solicitado unirte a esta clase.");
                            return;
                        } else {
                            generarNuevaSolicitud(codigo, idDocente, c);
                            return;
                        }
                    } else {
                        generarNuevaSolicitud(codigo, idDocente, c);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imprimir(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Solicitud> call, Throwable t) {
                Toast.makeText(ViewEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void imprimir(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void generarNuevaSolicitud(String codigo, int idDocente, Clase c) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceSolicitud serviceSolicitud = retrofit.create(ServiceSolicitud.class);

        nuevaSolicitud.setCodigo(codigo);
        nuevaSolicitud.setIdEstudiante(idEstudiante);
        nuevaSolicitud.setIdDocente(idDocente);
        nuevaSolicitud.setNombreClase(c.getNombre());
        nuevaSolicitud.setGradoClase(c.getGrado());
        nuevaSolicitud.setNombreEstudiante(estudianteIngresado.getNombre() + " " + estudianteIngresado.getApellido());
        nuevaSolicitud.setGradoEstudiante(estudianteIngresado.getGrado());
        nuevaSolicitud.setIdClase(c.getId());

        Call<Solicitud> solicitud = serviceSolicitud.guardarSolicitud(nuevaSolicitud);
        solicitud.enqueue(new Callback<Solicitud>() {
            @Override
            public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                try {
                    if (response.isSuccessful()) {
                        Solicitud s = response.body();
                        if (s == null) {
                            imprimir("No se ha podido generar la solicitud.");
                            return;
                        } else {
                            imprimir("Se ha generado la solicitud.");
                            return;
                        }
                    } else {
                        imprimir("Algo salió mal generando la solicitud.");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imprimir(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Solicitud> call, Throwable t) {
                Toast.makeText(ViewEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }

    public void misClases(View view) {
        Intent intent = new Intent(this, ViewListadoClasesEstudiante.class);
        intent.putExtra("estudianteId", idEstudiante);
        startActivity(intent);
    }

    public void editarPerfil(View view){
        Intent intent = new Intent(this, ViewEditarPerfilEstudiante.class);
        intent.putExtra("idEstudiante", idEstudiante);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}