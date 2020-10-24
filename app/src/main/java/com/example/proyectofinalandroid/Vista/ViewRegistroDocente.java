package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalandroid.Controlador.CtlDocente;
import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Docente;
// Éste import tuve que agregarlo manualmente
import com.example.proyectofinalandroid.R;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ViewRegistroDocente extends AppCompatActivity {

    EditText documento, nombre, apellido, telefono, correo, contrasena, calendario;
    CtlDocente controlador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro_docente);

        controlador = new CtlDocente();
        documento = (EditText) findViewById(R.id.jtxtDocumento);
        nombre = (EditText) findViewById(R.id.jtxtNombre);
        apellido = (EditText) findViewById(R.id.jtxtApellido);
        telefono = (EditText) findViewById(R.id.jtxtTelefono);
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
                if (this.controlador.registrarse(generarDocente(), 0)) {
                    Toast.makeText(this, "¡Has sido registrado!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(this, "Algo salió mal.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private Docente generarDocente() {
        Docente docente = new Docente();
        docente.setDocumento(Long.parseLong(this.documento.getText().toString()));
        docente.setNombre(this.nombre.getText().toString());
        docente.setApellido(this.apellido.getText().toString());
        docente.setTelefono(Long.parseLong(this.telefono.getText().toString()));
        docente.setCorreo(this.correo.getText().toString());
        docente.setContrasena(this.contrasena.getText().toString());
        // para parsear de string a util date
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(this.calendario.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Ocurrió un error parseando la fecha.", Toast.LENGTH_SHORT).show();
        }
        docente.setFechaNacimiento(date1);
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