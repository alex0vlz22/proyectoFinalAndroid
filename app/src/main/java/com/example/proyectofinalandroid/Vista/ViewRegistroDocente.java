package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalandroid.Controlador.CtlDocente;
import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Docente;
// Éste import tuve que agregarlo manualmente
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceDocente;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ViewRegistroDocente extends AppCompatActivity {

    // Junior url
    //    final String url = "http://192.168.1.63:1000";
    // Malejo url
    final String url = "http://192.168.1.9:1000";

    EditText documento, nombre, apellido, telefono, correo, contrasena, calendario;
    CtlDocente controlador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro_docente);

        controlador = new CtlDocente();
        documento = (EditText) findViewById(R.id.campoTitulo);
        nombre = (EditText) findViewById(R.id.jtxtDescripcion);
        apellido = (EditText) findViewById(R.id.jtxtApellido);
        telefono = (EditText) findViewById(R.id.jtxtLimite);
        correo = (EditText) findViewById(R.id.jtxtCorreo);
        contrasena = (EditText) findViewById(R.id.jtxtContraseña);
        calendario = (EditText) findViewById(R.id.jtxtFecha);

        // Línea para ocultar la barra de navegación por defecto del android studio
        getSupportActionBar().hide();

        generarCalendar();

    }

    // método crud

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
                                                if (controlador.registrarse(generarDocente(), 0)) {
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
                                    Toast.makeText(ViewRegistroDocente.this, "Falló.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ViewRegistroDocente.this, "Falló.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void imprimir(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private Docente generarDocente() {
        Docente docente = new Docente(Long.parseLong(this.documento.getText().toString()), this.nombre.getText().toString()
                , this.apellido.getText().toString(), Long.parseLong(this.telefono.getText().toString()), this.correo.getText().toString(),
                this.contrasena.getText().toString(), this.calendario.getText().toString());
        /*
        docente.setDocumento(Long.parseLong(this.documento.getText().toString()));
        docente.setNombre(this.nombre.getText().toString());
        docente.setApellido(this.apellido.getText().toString());
        docente.setTelefono(Long.parseLong(this.telefono.getText().toString()));
        docente.setCorreo(this.correo.getText().toString());
        docente.setContrasena(this.contrasena.getText().toString());
        docente.setFechaNacimiento(this.calendario.getText().toString());
*/
        return docente;
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

    // métodos de cambios en el onCreate

    private void generarCalendar() {
        this.calendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                DatePickerDialog mDatePicker = new DatePickerDialog(ViewRegistroDocente.this,
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}