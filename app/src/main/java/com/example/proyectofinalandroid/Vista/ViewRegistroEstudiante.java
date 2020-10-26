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
import com.example.proyectofinalandroid.Modelo.Estudiante;
// Éste import tuve que agregarlo manualmente
import com.example.proyectofinalandroid.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ViewRegistroEstudiante extends AppCompatActivity{

    EditText calendario;
    Spinner gradoNumero, gradoLetra;
    Button boton;

    EditText documento, nombre, apellido, telefono, correo, contrasena;
    CtlEstudiante controlador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro);

        controlador = new CtlEstudiante();
        documento = (EditText) findViewById(R.id.jtxtDocumento);
        nombre = (EditText) findViewById(R.id.jtxtNombre);
        apellido = (EditText) findViewById(R.id.jtxtApellido);
        telefono = (EditText) findViewById(R.id.jtxtTelefono);
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
            try {
                if (this.controlador.registrarse(generarEstudiante(), 0)) {
                    Toast.makeText(this, "¡Has sido registrado!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Algo salió mal.", Toast.LENGTH_SHORT).show();
                }
            } catch (OcurrioUnErrorGuardandoException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
        // para parsear de string a util date
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(this.calendario.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Ocurrió un error parseando la fecha.", Toast.LENGTH_SHORT).show();
        }
        estudiante.setFechaNacimiento(date1);
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