package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.Modelo.Participacion;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceDocente;
import com.example.proyectofinalandroid.Util.ServiceForo;
import com.example.proyectofinalandroid.Util.ServiceParticipacion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class viewForoEstudiante extends AppCompatActivity {
    Foro foro = new Foro();
    ListView lista;
    int idForo, idClase;
    int idDocente;
    List<Participacion> listaParticipaciones;
    Docente docente;
    TextView lblTitulo, lblDescripcion, lblDocente;
    int idEstudiante;

    // Junior url
    final String url = "http://192.168.1.92:1000";
    // Malejo url
    //final String url = "http://192.168.1.3:1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foro_estudiante);

        lblDescripcion = (TextView) findViewById(R.id.lblDescripcion);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);
        lblDocente = (TextView) findViewById(R.id.lblNombreDocente);
        lista = (ListView) findViewById(R.id.lstViewParticipaciones);

        getSupportActionBar().hide();

        Bundle b = getIntent().getExtras();
        idForo = b.getInt("idForo");
        idEstudiante = b.getInt("idEstudiante");
        idClase = b.getInt("idClase");

        setForo(idForo);
        listarParticipaciones();
    }

    private void listarParticipaciones() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceParticipacion serviceParticipacion = retrofit.create(ServiceParticipacion.class);
        Call<List<Participacion>> part = serviceParticipacion.listarParticipacionesPorForo(idForo);
        part.enqueue(new Callback<List<Participacion>>() {
            @Override
            public void onResponse(Call<List<Participacion>> call, Response<List<Participacion>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaParticipaciones = response.body();
                        if (listaParticipaciones.size() != 0) {

                            List<String> textoParticipaciones = new ArrayList<>();
                            for (int i = 0; i < listaParticipaciones.size(); i++) {
                                textoParticipaciones.add(listaParticipaciones.get(i).toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, textoParticipaciones);
                            lista.setAdapter(adapter);
                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(viewForoEstudiante.this, listaParticipaciones.get(i).getDescripcion(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            String listaVacia[] = {"Aún no hay comentarios. ¡Sé el Primero!"};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(viewForoEstudiante.this,
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
            public void onFailure(Call<List<Participacion>> call, Throwable t) {
                Toast.makeText(viewForoEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setDocente(int idDocente) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceDocente serviceDocente = retrofit.create(ServiceDocente.class);
        Call<Docente> doc = serviceDocente.buscarPorId(idDocente);
        doc.enqueue(new Callback<Docente>() {
            @Override
            public void onResponse(Call<Docente> call, Response<Docente> response) {
                try {
                    if (response.isSuccessful()) {
                        docente = response.body();
                        if (docente != null) {
                            lblDocente.setText(docente.getNombre() + " " + docente.getApellido());
                        } else {
                            Toast.makeText(viewForoEstudiante.this, "No se encontró el docente", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Docente> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setForo(int idForo) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceForo serviceForo = retrofit.create(ServiceForo.class);
        Call<Foro> foros = serviceForo.buscar(idForo);
        foros.enqueue(new Callback<Foro>() {
            @Override
            public void onResponse(Call<Foro> call, Response<Foro> response) {
                try {
                    if (response.isSuccessful()) {
                        foro = response.body();
                        if (foro != null) {
                            lblTitulo.setText(foro.getTitulo());
                            lblDescripcion.setText(foro.getDescripcion());
                            idDocente = foro.getIdDocente();
                            setDocente(foro.getIdDocente());
                        } else {
                            Toast.makeText(viewForoEstudiante.this, "No se encontró el Foro", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Foro> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ViewClaseEstudiante.class);
        intent.putExtra("idClase", idClase);
        intent.putExtra("idEstudiante", idEstudiante);
        startActivity(intent);
    }
}