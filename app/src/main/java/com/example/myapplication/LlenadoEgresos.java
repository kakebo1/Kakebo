package com.example.myapplication;

import static android.R.layout.simple_spinner_item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LlenadoEgresos extends AppCompatActivity {
    //DECLARAR VARIABLES
    Button btnAceptarEg;
    //Hhh
    ImageButton btnAgregarImg, btnCancelarEg;
    EditText txtConceptoEg, txtCantidadEg, txtFechaEg, txtComentarioEg;
    Switch deuda;
    RadioButton fijo, variable;
    private FirebaseFirestore basededatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_llenado_egresos);

        basededatos = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // INICIALIZAR COMPONENTES
        txtConceptoEg= findViewById(R.id.txtConceptoEg);
        txtCantidadEg= findViewById(R.id.txtCantidadEg);
        txtFechaEg= findViewById(R.id.txtFechaEg);
        txtComentarioEg= findViewById(R.id.txtComentarioEg);
        btnAceptarEg = findViewById(R.id.btnAceptarEg);
        btnCancelarEg = findViewById(R.id.btnCancelarEg);

        /** SPINNER CATEGORÍA **/
        // SE DEBE DE CAMBIAR LAS OPCIONES, LAS CORRESPONDIENTES A CADA CATEGORIA DE KAKEBO


        Spinner categoria = findViewById(R.id.categoriaspn);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.kakebo, simple_spinner_item);
        ad.setDropDownViewResource(simple_spinner_item);
        categoria.setAdapter(ad);

        // ASIGNAR EVENTO AL BOTÓN
        btnAceptarEg.setOnClickListener(this::aceptar2);

        /** SPINNER KAKEBO **/

        Spinner kakebo = findViewById(R.id.kakebospn);
        ArrayAdapter<CharSequence> adc = ArrayAdapter.createFromResource(this, R.array.kakebo, simple_spinner_item);
        ad.setDropDownViewResource(simple_spinner_item);
        kakebo.setAdapter(adc);

        btnCancelarEg.setOnClickListener(v -> {
            Toast.makeText(LlenadoEgresos.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }


    public void aceptar2(View v) {
        if (validar2()) {
            Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LlenadoEgresos.this, MenuPrinc.class);
            startActivity(intent);
            finish();  // Finaliza la actividad actual después de abrir el menú principal
        }
    }

    public boolean validar2() {
        boolean retorno = true;

        String concepto = txtConceptoEg.getText().toString().trim();
        String cantidadStr = txtCantidadEg.getText().toString().trim();
        String fecha = txtFechaEg.getText().toString().trim();
        String comentario = txtComentarioEg.getText().toString().trim();

        if (concepto.isEmpty()) {
            txtConceptoEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }

        int cantidad = 0;
        if (cantidadStr.isEmpty()) {
            txtCantidadEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        } else {
            try {
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                txtCantidadEg.setError("Introduce un número válido");
                retorno = false;
            }
        }

        if (retorno) {
            egreso(concepto, cantidad, fecha, comentario);
        }

        return retorno;
    }


    private void egreso(String concepto, int cantidad, String fecha, String comentario) {
        Map<String, Object> mapiii = new HashMap<>();
        mapiii.put("concepto", concepto);
        mapiii.put("cantidad", cantidad);
        mapiii.put("fecha", fecha);
        mapiii.put("comentario", comentario);


        basededatos.collection("egresos").add(mapiii)
                .addOnSuccessListener(documentReference -> {
             //       Toast.makeText(LlenadoEgresos.this, "Pago guardado con éxito", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoEgresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
    }

