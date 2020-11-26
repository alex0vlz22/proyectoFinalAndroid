package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectofinalandroid.Controlador.CtlEstudiante;
import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Modelo.Estudiante;
// Éste import tuve que agregarlo manualmente
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceDocente;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ViewRegistroEstudiante extends AppCompatActivity {

    EditText calendario;
    Spinner gradoNumero, gradoLetra;
    Button boton;

    EditText documento, nombre, apellido, telefono, correo, contrasena;
    CtlEstudiante controlador;

    // Junior url
    final String url = "http://192.168.1.2:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro);

        controlador = new CtlEstudiante();
        documento = (EditText) findViewById(R.id.campoTitulo);
        nombre = (EditText) findViewById(R.id.jtxtDescripcion);
        apellido = (EditText) findViewById(R.id.jtxtApellido);
        telefono = (EditText) findViewById(R.id.jtxtLimite);
        correo = (EditText) findViewById(R.id.jtxtCorreo);
        contrasena = (EditText) findViewById(R.id.jtxtContraseña);

        calendario = (EditText) findViewById(R.id.jtxtFecha);
        gradoNumero = (Spinner) findViewById(R.id.jSpnGradoNumero);
        gradoLetra = (Spinner) findViewById(R.id.jSpnGradoLetra);

        boton = (Button) findViewById(R.id.jbtnRegistrarme);

        boton.setOnClickListener(this::registrarse);

        // Línea para ocultar la barra de navegación por defecto del android studio
        getSupportActionBar().hide();

        generarCalendar();
        llenarSpinners();
    }


    public void registrarse(View view) {
        if (validarCampos()) {
            Toast.makeText(this, "Llena todos los campos adecuadamente.", Toast.LENGTH_SHORT).show();
        } else {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            ServiceDocente serviceDocente = retrofit.create(ServiceDocente.class);
            Call<Docente> docent = serviceDocente.buscarPorCorreo(correo.getText().toString());
            docent.enqueue(new Callback<Docente>() {
                @Override
                public void onResponse(Call<Docente> call, Response<Docente> response) {
                    try {
                        if (response.isSuccessful()) {
                            Docente doc = response.body();
                            imprimir("Ya hay una cuenta registrada por éste correo.");
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
                                            imprimir("Ya hay una cuenta registrada por éste correo.");
                                            return;
                                        } else {
                                            // En caso de que no se encuentre un docente ni estudiante por el correo
                                            // ingresado, llega hasta aquí.
                                            try {
                                                if (controlador.registrarse(generarEstudiante(), 0)) {
                                                    Toast.makeText(getApplicationContext(), "¡Has sido registrado!", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(i);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Algo salió mal.", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (OcurrioUnErrorGuardandoException e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        imprimir(e.getMessage());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Estudiante> call, Throwable t) {
                                    Toast.makeText(ViewRegistroEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ViewRegistroEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void imprimir(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private Estudiante generarEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setDocumento(Long.parseLong(this.documento.getText().toString()));
        estudiante.setNombre(this.nombre.getText().toString());
        estudiante.setApellido(this.apellido.getText().toString());
        estudiante.setTelefono(Long.parseLong(this.telefono.getText().toString()));
        estudiante.setCorreo(this.correo.getText().toString());
        estudiante.setContrasena(this.contrasena.getText().toString());
        estudiante.setFechaNacimiento(this.calendario.getText().toString());
        String grado = gradoNumero.getSelectedItem().toString() + gradoLetra.getSelectedItem().toString();
        estudiante.setGrado(grado);
        return estudiante;
    }

    private boolean validarCampos() {
        if (documento.getText().toString().equals("") || nombre.getText().toString().equals("") ||
                apellido.getText().toString().equals("") || telefono.getText().toString().equals("") ||
                correo.getText().toString().equals("") || contrasena.getText().toString().equals("") ||
                calendario.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private void llenarSpinners() {
        String gradosNumeros[] = {"Seleccionar", "5", "6", "7", "8", "9", "10", "11"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosNumeros);
        gradoNumero.setAdapter(adapter);

        String gradosLetras[] = {"Seleccionar", "A", "B", "C", "D", "E", "F", "G"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosLetras);
        gradoLetra.setAdapter(adapter2);

        /* ----------- CODIGO PARA GENERAR LLENAR SPINNER, TOMADO DE PROYECTO VIEJO ---------------

        List<> listaMarcas = ctlMarca.listar();
        List<> listaNombresMarcas = new ArrayList<String>();
        for (int i = 0; i < listaMarcas.size(); i++) {
            listaNombresMarcas.add(listaMarcas.get(i).getNombre());
        }
        adapterMarcas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaNombresMarcas);
        marca.setAdapter(adapterMarcas); */
    }

    private void generarCalendar() {
        this.calendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                DatePickerDialog mDatePicker = new DatePickerDialog(ViewRegistroEstudiante.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear,
                                                  int selectedmonth, int selectedday) {
                                calendario.setText(selectedyear + "/" + selectedmonth + "/" + selectedday);
                            }
                        }, mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH),
                        mcurrentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }

    // método para manipular el botón 'atrás' del dispositivo
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}