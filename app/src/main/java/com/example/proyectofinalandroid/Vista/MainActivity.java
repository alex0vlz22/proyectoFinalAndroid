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
import com.example.proyectofinalandroid.Exception.ContrasenaIncorrectaExcepcion;
import com.example.proyectofinalandroid.Exception.UsuarioNoEncontradoException;
import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.R;

public class MainActivity extends AppCompatActivity {

    EditText correo, contrasena;
    Button registrarse;
    CtlDocente controladorDocente;
    CtlEstudiante controladorEstudiante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = (EditText) findViewById(R.id.jtxtCorreoLogin);
        contrasena = (EditText) findViewById(R.id.jtxtContraLogin);
        registrarse = (Button) findViewById(R.id.jbtnRegistrarse);
        controladorDocente = new CtlDocente();
        controladorEstudiante = new CtlEstudiante();

        // Línea para ocultar la barra de navegación por defecto del android studio
        getSupportActionBar().hide();
    }

    public void ingresar(View view) {
        if (validarCampos()) {
            if (buscarDocente() != null) {
                Intent i = new Intent(this, ViewDocente.class);
                startActivity(i);
            } else if (buscarEstudiante() != null) {
                Intent i = new Intent(this, ViewEstudiante.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "No se ha encontrado un usuario por dicho correo.", Toast.LENGTH_SHORT).show();
            }
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

    private Docente buscarDocente() {
        try {
            Docente d = this.controladorDocente.buscar(correo.getText().toString(), contrasena.getText().toString(), 0);
            return d;
        } catch (ContrasenaIncorrectaExcepcion e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        } catch (UsuarioNoEncontradoException e) {
            return null;
        }
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