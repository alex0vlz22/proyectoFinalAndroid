package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Telephony;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.Modelo.Participacion;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceDocente;
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

public class viewForoEstudiante extends AppCompatActivity {
    private Context mcontext = this;
    EditText comentario;
    Foro foro = new Foro();
    List<Participacion> listaParticipacionesEstudiante;
    ListView lista;
    int idForo, idClase;
    int idDocente;
    List<Participacion> listaParticipaciones;
    Docente docente;
    TextView lblTitulo, lblDescripcion, lblDocente;
    int idEstudiante;
    Estudiante estRetorno;
    // Junior url
    //final String url = "http://192.168.1.92:1000";
    // Malejo url
    final String url = "http://192.168.1.5:1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foro_estudiante);

        lblDescripcion = (TextView) findViewById(R.id.lblDescripcion);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);
        lblDocente = (TextView) findViewById(R.id.lblNombreDocente);
        lista = (ListView) findViewById(R.id.lstViewParticipaciones);
        comentario = (EditText) findViewById(R.id.txtComentario);
        getSupportActionBar().hide();

        Bundle b = getIntent().getExtras();
        idForo = b.getInt("idForo");
        idEstudiante = b.getInt("idEstudiante");
        idClase = b.getInt("idClase");

        setForo(idForo);
        listarParticipaciones();
    }

    public void comentar(View view) {
        if (comentario.getText().toString().equals("") || comentario.getText().toString().length() < 6) {
            Toast.makeText(this, "Ingrese al menos 6 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            if (limiteExcedido(idEstudiante, idForo)) {
                Toast.makeText(this, "Has excedido el numero máximo de participaciones para este foro", Toast.LENGTH_LONG).show();
            } else {
                String[][] mensajeRes = {{""}};

                Participacion p = new Participacion(comentario.getText().toString(), idEstudiante, idForo);
                try {

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
                    ServiceParticipacion serviceParticipacion = retrofit.create(ServiceParticipacion.class);
                    Call<Participacion> estAfter = serviceParticipacion.guardar(p);
                    estAfter.enqueue(new Callback<Participacion>() {
                        @Override
                        public void onResponse(Call<Participacion> call, Response<Participacion> response) {
                            try {
                                if (response.isSuccessful()) {
                                    Participacion participacion = response.body();
                                    if (participacion == null) {
                                        Toast.makeText(viewForoEstudiante.this, "No se pudo guardar", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(viewForoEstudiante.this, "Se guardó la participacion", Toast.LENGTH_SHORT).show();

                                        listarParticipaciones();
                                        comentario.setText("");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<Participacion> call, Throwable t) {
                            Toast.makeText(viewForoEstudiante.this, "Error en el API REST al guardar la participacion", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private boolean limiteExcedido(int idEstudiante, int idForo) {
        final boolean[][] retorno = {{true}};
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceParticipacion serviceParticipacion = retrofit.create(ServiceParticipacion.class);
        Call<List<Participacion>> part = serviceParticipacion.listarParticipacionesPorParticipanteEnForo(idEstudiante, idForo);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            listaParticipacionesEstudiante = part.execute().body();
            if (listaParticipacionesEstudiante.size() == 0) {
                Toast.makeText(viewForoEstudiante.this, "Aun no has participado en este foro", Toast.LENGTH_SHORT).show();
                return false;
            } else if (listaParticipacionesEstudiante.size() >= foro.getLimiteParticipaciones()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return true;
        }
        /*
        part.enqueue(new Callback<List<Participacion>>() {
            @Override
            public void onResponse(Call<List<Participacion>> call, Response<List<Participacion>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaParticipacionesEstudiante = response.body();
                        if (listaParticipacionesEstudiante.size() == 0) {
                            Toast.makeText(viewForoEstudiante.this, "Aun no has participado en este foro", Toast.LENGTH_SHORT).show();
                            retorno[0][0] = false;
                            return;
                        } else if (listaParticipacionesEstudiante.size() >= foro.getLimiteParticipaciones()) {
                            retorno[0][0] = true;
                            return;
                        } else {
                            retorno[0][0] = false;
                            return;
                        }
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Participacion>> call, Throwable t) {
                Toast.makeText(viewForoEstudiante.this, "No se pudo obtener el numero de veces que has participado.", Toast.LENGTH_SHORT).show();
            }
        });
        return retorno[0][0];
        */


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
                                Estudiante estu = getEstu(listaParticipaciones.get(i).getIdParticipante());
                                if (estRetorno != null) {
                                    textoParticipaciones.add(estu.getNombre() + " " + estu.getApellido() + " : " + listaParticipaciones.get(i).getDescripcion());
                                } else {
                                    textoParticipaciones.add(listaParticipaciones.get(i).getDescripcion());
                                }
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
                            lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                                    builder.setTitle("Vas a eliminar tu comentario");
                                    builder.setMessage("¿Quieres Eliminar este comentario?");

                                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int is) {
                                            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
                                            ServiceParticipacion serviceParticipacion = retrofit.create(ServiceParticipacion.class);
                                            Call<Participacion> parti = serviceParticipacion.eliminar(listaParticipaciones.get(i).getId());

                                            parti.enqueue(new Callback<Participacion>() {
                                                @Override
                                                public void onResponse(Call<Participacion> call, Response<Participacion> response) {

                                                    if (response.isSuccessful()) {
                                                        Toast.makeText(viewForoEstudiante.this, "Se eliminó la participacion", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(viewForoEstudiante.this, "No se eliminó la participacion", Toast.LENGTH_SHORT).show();
                                                    }
                                                    listarParticipaciones();
                                                    return;

                                                }

                                                @Override
                                                public void onFailure(Call<Participacion> call, Throwable t) {
                                                    Toast.makeText(viewForoEstudiante.this, "Falló algo en el ApiRest.", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        }
                                    });
                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    return true;
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

    private Estudiante getEstu(int idParticipante) {
        try {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            ServiceEstudiante serviceEstudiante = retrofit.create(ServiceEstudiante.class);
            Call<Estudiante> student = serviceEstudiante.buscarPorId(idParticipante);
            student.enqueue(new Callback<Estudiante>() {
                @Override
                public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {

                    if (response.isSuccessful()) {
                        estRetorno = response.body();
                    } else {
                        estRetorno = null;
                    }
                    return;

                }

                @Override
                public void onFailure(Call<Estudiante> call, Throwable t) {
                    Toast.makeText(viewForoEstudiante.this, "Falló algo en el ApiRest.", Toast.LENGTH_SHORT).show();
                }
            });
            return estRetorno;
        } catch (Exception e) {
            Toast.makeText(viewForoEstudiante.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
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
                            lblDocente.setText("Nombre del docente: " + docente.getNombre() + " " + docente.getApellido());
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
                            lblTitulo.setText("Titulo del Foro: " + foro.getTitulo());
                            lblDescripcion.setText("Descripcion: " + foro.getDescripcion());
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