package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.Modelo.Participacion;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;
import com.example.proyectofinalandroid.Util.ServiceForo;
import com.example.proyectofinalandroid.Util.ServiceParticipacion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewForoDocente extends AppCompatActivity {
    TextView lblYaHanParticipado, lblFaltantes, lblParticipaciones;
    int idDocente, idForo, auxiliar_para_calcular_entrada = 0;
    long documento;
    Estudiante estRetorno;
    List<Participacion> listaRetornada = new ArrayList<Participacion>();
    List<Estudiante> listaEstudiantesQueParticiparon = new ArrayList<Estudiante>();
    List<Estudiante> listaEstudiantesFaltantes = new ArrayList<Estudiante>();
    ListView listaYaHanParticipado, listaFaltanPorParticipar, listaParticipaciones;

    // Junior url
    final String url = "http://192.168.1.2:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foro_docente);
        inicializarComponentes(); // Método donde se instancian los items necesarios par que la
        // ventana funcione.
    }

    private void inicializarComponentes() {
        getSupportActionBar().hide(); // Método para ocultar la barra de navegación por defecto de
        // android.

        obtenerDatosBundle(); // Método para recibir los datos enviados por medio del Intent.

        inicializarComponentesVista(); // Método para igualar variables a las listas de la vista.

        llenarListas(); // Método para comenzar a llenar todas las listas con sus datos
        // respectivos.
    }

    private void inicializarComponentesVista() {
        this.listaYaHanParticipado = (ListView) findViewById(R.id.jlstYaHanParticipado);
        this.listaFaltanPorParticipar = (ListView) findViewById(R.id.jlstFaltanPorParticipar);
        this.listaParticipaciones = (ListView) findViewById(R.id.jlstParticipaciones);
        lblFaltantes = (TextView) findViewById(R.id.lblFaltantes);
        lblParticipaciones = (TextView) findViewById(R.id.lblParticipaciones);
        lblYaHanParticipado = (TextView) findViewById(R.id.lblYaHanParticipado);
    }

    private void obtenerDatosBundle() {
        Bundle bundle = getIntent().getExtras();
        this.idDocente = bundle.getInt("idDocente");
        this.documento = bundle.getLong("documento");
        this.idForo = bundle.getInt("idForo");
    }

    private void llenarListas() {
        llenarListaParticipaciones();
        llenarListaYaHanParticipado();
        llenarListaFaltanPorParticipar();
    }

    private void llenarListaYaHanParticipado() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceForo serviceForo = retrofit.create(ServiceForo.class);
        Call<List<Estudiante>> part = serviceForo.listarEstudiantesParticiparon(idForo);
        part.enqueue(new Callback<List<Estudiante>>() {
            @Override
            public void onResponse(Call<List<Estudiante>> call, Response<List<Estudiante>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaEstudiantesQueParticiparon = response.body();
                        if (listaEstudiantesQueParticiparon.size() != 0) {
                            lblYaHanParticipado.setText(" Ya han participado :  " + listaEstudiantesQueParticiparon.size());
                            List<String> textoParticipaciones = new ArrayList<>();
                            for (int i = 0; i < listaEstudiantesQueParticiparon.size(); i++) {
                                textoParticipaciones.add(listaEstudiantesQueParticiparon.get(i).getNombre() + " " + listaEstudiantesQueParticiparon.get(i).getApellido());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewForoDocente.this,
                                    android.R.layout.simple_list_item_1, textoParticipaciones);
                            listaYaHanParticipado.setAdapter(adapter);
/*                            listaYaHanParticipado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //imprimir(listaEstudiantesQueParticiparon.get(position).getNombre()
                                    //        + ' ' + listaEstudiantesQueParticiparon.get(position).getApellido());
                                }
                            });*/
                        } else {
                            String listaVacia[] = {"Aún no ha participado Nadie..."};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewForoDocente.this,
                                    android.R.layout.simple_list_item_1, listaVacia);
                            listaYaHanParticipado.setAdapter(adapter);
                        }
                        return;
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Estudiante>> call, Throwable t) {
                imprimir("Falló.");
            }
        });
    }

    private void llenarListaFaltanPorParticipar() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceForo serviceForo = retrofit.create(ServiceForo.class);
        Call<List<Estudiante>> part = serviceForo.listarEstudiantesNoParticiparon(idForo);
        part.enqueue(new Callback<List<Estudiante>>() {
            @Override
            public void onResponse(Call<List<Estudiante>> call, Response<List<Estudiante>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaEstudiantesFaltantes = response.body();
                        if (listaEstudiantesFaltantes.size() != 0) {
                            lblFaltantes.setText(" Faltan por participar :  " + listaEstudiantesFaltantes.size());
                            List<String> textoParticipaciones = new ArrayList<>();
                            for (int i = 0; i < listaEstudiantesFaltantes.size(); i++) {
                                textoParticipaciones.add(listaEstudiantesFaltantes.get(i).getNombre() + " " + listaEstudiantesFaltantes.get(i).getApellido());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewForoDocente.this,
                                    android.R.layout.simple_list_item_1, textoParticipaciones);
                            listaFaltanPorParticipar.setAdapter(adapter);
                        } else {
                            String listaVacia[] = {"¡Ya participaron Todos!"};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewForoDocente.this,
                                    android.R.layout.simple_list_item_1, listaVacia);
                            listaFaltanPorParticipar.setAdapter(adapter);
                        }
                        return;
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Estudiante>> call, Throwable t) {
                imprimir("Falló.");
            }
        });
    }

    private void llenarListaParticipaciones() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceParticipacion serviceParticipacion = retrofit.create(ServiceParticipacion.class);
        Call<List<Participacion>> part = serviceParticipacion.listarParticipacionesPorForo(idForo);
        part.enqueue(new Callback<List<Participacion>>() {
            @Override
            public void onResponse(Call<List<Participacion>> call, Response<List<Participacion>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaRetornada = response.body();
                        if (listaRetornada.size() != 0) {
                            lblParticipaciones.setText(" Participaciones :  " + listaRetornada.size());
                            List<String> textoParticipaciones = new ArrayList<>();
                            for (int i = 0; i < listaRetornada.size(); i++) {
                                textoParticipaciones.add(listaRetornada.get(i).getDescripcion());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewForoDocente.this,
                                    android.R.layout.simple_list_item_1, textoParticipaciones);
                            listaParticipaciones.setAdapter(adapter);
                            listaParticipaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    imprimirNombre(listaRetornada.get(position).getIdParticipante());
                                }
                            });
                        } else {
                            String listaVacia[] = {"Aún no hay participaciones."};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewForoDocente.this,
                                    android.R.layout.simple_list_item_1, listaVacia);
                            listaParticipaciones.setAdapter(adapter);
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Participacion>> call, Throwable t) {
                imprimir("Falló.");
            }
        });
    }

    private void imprimirNombre(int idParticipante) {
        for (int i = 0; i < listaEstudiantesQueParticiparon.size(); i++) {
            if (listaEstudiantesQueParticiparon.get(i).getId() == idParticipante) {
                Toast.makeText(this, listaEstudiantesQueParticiparon.get(i).getNombre() + " " + listaEstudiantesQueParticiparon.get(i).getApellido(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void imprimir(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ListadoForos.class);
        intent.putExtra("docenteId", idDocente);
        intent.putExtra("documento", documento);
        startActivity(intent);
    }
}