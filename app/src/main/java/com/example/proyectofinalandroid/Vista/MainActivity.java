package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinalandroid.Controlador.CtlDocente;
import com.example.proyectofinalandroid.Controlador.CtlEstudiante;
import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceDocente;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Junior url
    final String url = "http://192.168.1.92:1000";
    // Malejo url
    //final String url = "http://192.168.1.3:1000";


    EditText correo, contrasena;
    Button registrarse;
    CtlDocente controladorDocente;
    CtlEstudiante controladorEstudiante;
    Docente auxDocente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = (EditText) findViewById(R.id.jtxtCorreoLogin);
        contrasena = (EditText) findViewById(R.id.jtxtContraLogin);
        registrarse = (Button) findViewById(R.id.jbtnRegistrarse);
        controladorDocente = new CtlDocente();
        controladorEstudiante = new CtlEstudiante();
        auxDocente = null;

        // Línea para ocultar la barra de navegación por defecto del android studio
        getSupportActionBar().hide();
    }

    public void ingresar(View view) {
        if (validarCampos()) {
            // aquí inicia el procedimiento para buscar un docente por los datos ingresados
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            ServiceDocente serviceDocente = retrofit.create(ServiceDocente.class);
            Call<Docente> docent = serviceDocente.buscarPorCorreo(correo.getText().toString());
            docent.enqueue(new Callback<Docente>() {
                @Override
                public void onResponse(Call<Docente> call, Response<Docente> response) {
                    try {
                        if (response.isSuccessful()) {
                            Docente doc = response.body();
                            if (!doc.getContrasena().equalsIgnoreCase(contrasena.getText().toString())) {
                                imprimir("contraseña incorrecta");
                                auxDocente = null;
                                return;
                            }
                            Intent i = new Intent(getApplicationContext(), ViewDocente.class);
                            i.putExtra("docenteId", doc.getId());
                            i.putExtra("documento", doc.getDocumento());
                            startActivity(i);
                            return;
                        } else {
                            // en caso de no encontrar docente, hago el mismo procedimiento para buscar
                            // un estudiante
                            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
                            ServiceEstudiante serviceEstudiante = retrofit.create(ServiceEstudiante.class);
                            Call<Estudiante> student = serviceEstudiante.buscarPorCorreo(correo.getText().toString());
                            student.enqueue(new Callback<Estudiante>() {
                                @Override
                                public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                                    try {
                                        if (response.isSuccessful()) {
                                            Estudiante est = response.body();
                                            if (!est.getContrasena().equalsIgnoreCase(contrasena.getText().toString())) {
                                                imprimir("contraseña incorrecta");
                                                return;
                                            }
                                            Intent i = new Intent(getApplicationContext(), ViewEstudiante.class);
                                            i.putExtra("estudianteId", est.getId());
                                            startActivity(i);
                                            return;
                                        } else {
                                            imprimir("No se ha encontrado una cuenta por el correo: " + correo.getText().toString());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        imprimir(e.getMessage());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Estudiante> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Falló.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        imprimir(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Docente> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Falló.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Por favor rellenar los campos adecuadamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        if (correo.getText().toString().equals("") || contrasena.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void ingresarDocente() {

    }

    private void imprimir(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private Estudiante buscarEstudiante() {
        return null;
    }

    public void registrarse(View view) {
        // Creo un arreglo con dos opciones y las seteo en un alertDialog, esto es para que
        // el usuario elija si es docente o estudiante una vez presionado el botón 'registrarse'
        String[] roles = {"Estudiante", "Docente"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Eres docente o estudiante?");
        builder.setItems(roles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent i = new Intent(getApplicationContext(), ViewRegistroEstudiante.class);
                    startActivity(i);
                } else if (which == 1) {
                    showDialog();
                }
            }
        });
        builder.show();
    }

    void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_docente_codigo, null);

        EditText codigo = view.findViewById(R.id.jtxtCodigo);
        Button continuar = view.findViewById(R.id.jbtnValidar);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codigo.getText().toString().equals("A7B8C9")) {
                    Intent i = new Intent(getApplicationContext(), ViewRegistroDocente.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "¡El código ingresado es " +
                            "incorrecto!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
    }

    // método para manipular el botón 'atrás' del dispositivo
    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

}