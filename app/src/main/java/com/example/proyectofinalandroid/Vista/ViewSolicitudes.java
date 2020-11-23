package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.EstudianteClase;
import com.example.proyectofinalandroid.Modelo.Solicitud;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceEstudianteClase;
import com.example.proyectofinalandroid.Util.ServiceSolicitud;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewSolicitudes extends AppCompatActivity {

    int idDocente;
    List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
    EstudianteClase estudianteClase = new EstudianteClase();
    ListView lista;
    long documento;

    // Junior url
    final String url = "http://192.168.1.92:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_solicitudes);

        lista = (ListView) findViewById(R.id.jlstSolicitudes);

        Bundle bundle = getIntent().getExtras();
        idDocente = bundle.getInt("idDocente");
        documento = bundle.getLong("documento");

        getSupportActionBar().hide();
        llenarLista();

    }

    private void llenarLista() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceSolicitud serviceSolicitud = retrofit.create(ServiceSolicitud.class);
        Call<List<Solicitud>> listaX = serviceSolicitud.buscarPorIdDocente(idDocente);
        listaX.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaSolicitudes = response.body();
                        if (listaSolicitudes.size() != 0) {
                            ArrayAdapter<Solicitud> adapter = new ArrayAdapter<Solicitud>(ViewSolicitudes.this,
                                    android.R.layout.simple_list_item_1, listaSolicitudes);
                            lista.setAdapter(adapter);

                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(ViewSolicitudes.this, listaSolicitudes.get(i).getCodigo(), Toast.LENGTH_SHORT).show();
                                    preguntaAceptarRechazar(i);
                                }
                            });
                        } else {
                            String listaVacia[] = {"No hay solicitudes aún."};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewSolicitudes.this,
                                    android.R.layout.simple_list_item_1, listaVacia);
                            lista.setAdapter(adapter);
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Toast.makeText(ViewSolicitudes.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void preguntaAceptarRechazar(int i) {
        String[] opciones = {"Aceptar", "Rechazar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Estudiante: " + listaSolicitudes.get(i).getNombreEstudiante() + "\n" +
                "Est.  grado: " + listaSolicitudes.get(i).getGradoEstudiante());
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Se aceptó la solicitud
                    aceptarEstudiante(i);
                    // luego de aceptar o eliminar se debe llamar de nuevo el método de listar.
                } else if (which == 1) {
                    // Se rechazó la solicitud
                    rechazarEstudiante(i, 0);
                }
            }
        });
        builder.show();
    }

    private void aceptarEstudiante(int i) {
        construirEstudianteClase(i);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceEstudianteClase serviceEstudianteClase = retrofit.create(ServiceEstudianteClase.class);
        Call<EstudianteClase> estCls = serviceEstudianteClase.guardar(estudianteClase);
        estCls.enqueue(new Callback<EstudianteClase>() {
            @Override
            public void onResponse(Call<EstudianteClase> call, Response<EstudianteClase> response) {
                try {
                    if (response.isSuccessful()) {
                        EstudianteClase estClsNuevo = response.body();
                        if (estClsNuevo == null) {
                            imprimir("Algo salió mal al aceptar el estudiante.");
                        } else {
                            imprimir("El estudiante ha sido aceptado.");
                            rechazarEstudiante(i, 1);
                            llenarLista();
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EstudianteClase> call, Throwable t) {
                Toast.makeText(ViewSolicitudes.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rechazarEstudiante(int i, int aux) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceSolicitud serviceSolicitud = retrofit.create(ServiceSolicitud.class);
        Call<Solicitud> sol = serviceSolicitud.eliminar(listaSolicitudes.get(i).getId());
        sol.enqueue(new Callback<Solicitud>() {
            @Override
            public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                try {
                    if (response.isSuccessful()) {
                        Solicitud solicitud = response.body();
                        if (solicitud == null) {
                            imprimir("Algo salió mal al rechazar el estudiante.");
                        } else {
                            if (aux == 0) {
                                imprimir("La solicitud fue rechazada.");
                            }
                            llenarLista();
                        }
                        return;
                    } else {
                        imprimir("NO SE ENCONTRÓ TAL SOLICITUD.");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Solicitud> call, Throwable t) {
                Toast.makeText(ViewSolicitudes.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void construirEstudianteClase(int i) {
        estudianteClase.setIdClase(listaSolicitudes.get(i).getIdClase());
        estudianteClase.setIdEstudiante(listaSolicitudes.get(i).getIdEstudiante());
        estudianteClase.setNombreClase(listaSolicitudes.get(i).getNombreClase());
    }

    public void imprimir(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewClases.class);
        intent.putExtra("docenteId", idDocente);
        intent.putExtra("documento", documento);
        startActivity(intent);
    }
}