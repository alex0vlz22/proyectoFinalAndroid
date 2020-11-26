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

import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewEditarPerfilEstudiante extends AppCompatActivity {

    EditText calendario;
    Spinner gradoNumero, gradoLetra;
    Button boton;
    int idEstudiante;
    EditText documento, nombre, apellido, telefono, correo, contrasena;

    // Junior url
    final String url = "http://192.168.1.2:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_editar_perfil_estudiante);

        inicializarComponentes();

    }

    public void guardarCambios(View view) {
        if (validarCampos()) {
            imprimir("Por favor rellena los campos adecuadamente.");
        } else {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            ServiceEstudiante serviceEstudiante = retrofit.create(ServiceEstudiante.class);
            Call<Estudiante> student = serviceEstudiante.modificar(generarEstudiante());
            student.enqueue(new Callback<Estudiante>() {
                @Override
                public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                    try {
                        if (response.isSuccessful()) {
                            Estudiante est = response.body();
                            imprimir("Los cambios han sido guardados.");
                            onBackPressed();
                        } else {
                            imprimir("Algo salió mal guardando los cambios.");
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        imprimir(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Estudiante> call, Throwable t) {
                    Toast.makeText(ViewEditarPerfilEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    private void inicializarComponentes() {
        Bundle bundle = getIntent().getExtras();
        idEstudiante = bundle.getInt("idEstudiante");

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

        //boton.setOnClickListener(this::guardarCambios);

        // Línea para ocultar la barra de navegación por defecto del android studio
        getSupportActionBar().hide();

        generarCalendar();
        llenarSpinners();
        llenarEstudiante();
    }

    private void llenarEstudiante() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceEstudiante serviceEstudiante = retrofit.create(ServiceEstudiante.class);
        Call<Estudiante> estudiante = serviceEstudiante.buscarPorId(idEstudiante);
        estudiante.enqueue(new Callback<Estudiante>() {
            @Override
            public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                try {
                    if (response.isSuccessful()) {
                        Estudiante EstudianteBuscado = response.body();
                        llenarCamposEstudianteBuscado(EstudianteBuscado);
                        return;
                    } else {
                        imprimir("Algo ocurrió buscando el Estudiante.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imprimir(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Estudiante> call, Throwable t) {
                imprimir("Falló.");
            }
        });
    }

    private void llenarCamposEstudianteBuscado(Estudiante estudianteBuscado) {
        documento.setText(Long.toString(estudianteBuscado.getDocumento()));
        nombre.setText(estudianteBuscado.getNombre());
        apellido.setText(estudianteBuscado.getApellido());
        telefono.setText(Long.toString(estudianteBuscado.getTelefono()));
        correo.setText(estudianteBuscado.getCorreo());
        contrasena.setText(estudianteBuscado.getContrasena());
        calendario.setText(estudianteBuscado.getFechaNacimiento());
        llenarSpinnersEstudiante(estudianteBuscado);
    }

    private void llenarSpinnersEstudiante(Estudiante estudianteBuscado) {
        String gradosNumeros[] = {"Seleccionar", "5", "6", "7", "8", "9", "10", "11"};
        llenarSpinnerNumero(estudianteBuscado, 0, gradosNumeros);
        String gradosLetras[] = {"Seleccionar", "A", "B", "C", "D", "E", "F", "G"};
        llenarSpinnerLetra(estudianteBuscado, 0, gradosLetras);
    }

    private void llenarSpinnerLetra(Estudiante estudianteBuscado, int i, String[] array) {
        if (i == array.length) {
            return;
        }else if(i >= 6){
            String letra = String.valueOf(estudianteBuscado.getGrado().charAt(2));
            if(letra.equals(array[i])){
                gradoLetra.setSelection(i);
                return;
            }else{
                int y = i + 1;
                llenarSpinnerLetra(estudianteBuscado, y, array);
            }
        }else{
            if (array[i].equals(String.valueOf(estudianteBuscado.getGrado().charAt(1)))) {
                gradoLetra.setSelection(i);
                return;
            } else {
                int y = i + 1;
                llenarSpinnerLetra(estudianteBuscado, y, array);
            }
        }
    }

    private void llenarSpinnerNumero(Estudiante estudianteBuscado, int x, String[] array) {
        if (x == array.length) {
            return;
        }if(x >= 6){
            char first = estudianteBuscado.getGrado().charAt(0);
            char secnd = estudianteBuscado.getGrado().charAt(1);
            String grado = String.valueOf(first) + String.valueOf(secnd);
            if(array[x].equals(grado)){
                gradoNumero.setSelection(x);
                return;
            }else{
                int y = x + 1;
                llenarSpinnerNumero(estudianteBuscado, y, array);
            }
        }else{
            if (array[x].equals(String.valueOf(estudianteBuscado.getGrado().charAt(0)))) {
                gradoNumero.setSelection(x);
                return;
            } else {
                int y = x + 1;
                llenarSpinnerNumero(estudianteBuscado, y, array);
            }
        }
    }

    private void imprimir(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void llenarSpinners() {
        String gradosNumeros[] = {"Seleccionar", "5", "6", "7", "8", "9", "10", "11"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosNumeros);
        gradoNumero.setAdapter(adapter);

        String gradosLetras[] = {"Seleccionar", "A", "B", "C", "D", "E", "F", "G"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosLetras);
        gradoLetra.setAdapter(adapter2);
    }

    private void generarCalendar() {
        this.calendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                DatePickerDialog mDatePicker = new DatePickerDialog(ViewEditarPerfilEstudiante.this,
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
        Intent intent = new Intent(this, ViewEstudiante.class);
        intent.putExtra("estudianteId", idEstudiante);
        startActivity(intent);
    }
}