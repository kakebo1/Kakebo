package com.example.myapplication;

import static android.R.layout.simple_spinner_item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LlenadoPlanDeudas extends AppCompatActivity {
    // DECLARAR COMPONENTES
    Button btnAceptarDe;
    EditText txtConceptoDe, txtCantidadDe, txtFechaDe, txtComentarioDe;
    String planPagDe, kakeboDe;
    private FirebaseFirestore basededatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_llenado_plan_deudas);
        basededatos = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // INICIALIZAR COMPONENTES
        btnAceptarDe = findViewById(R.id.btnAceptarPlan);
        txtConceptoDe = findViewById(R.id.txtConceptoPlan);
        txtCantidadDe = findViewById(R.id.txtCantidadPlan);
        txtFechaDe = findViewById(R.id.txtFecha);
        txtComentarioDe = findViewById(R.id.txtComentariosPlan);

        /** SPINNER PLAN DE PAGOS **/
        Spinner plandepagos = findViewById(R.id.plandepagosDe);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.plazospagodeuda, simple_spinner_item);
        ad.setDropDownViewResource(simple_spinner_item);
        plandepagos.setAdapter(ad);

        plandepagos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int a, long l) {
                planPagDe = adapterView.getItemAtPosition(a).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            //   planPagDe = "Mensual"; // Valor por defecto
            }
        });

        /** SPINNER KAKEBO **/
        Spinner kakebo = findViewById(R.id.kakeboPlanDe);
        ArrayAdapter<CharSequence> adc = ArrayAdapter.createFromResource(this, R.array.kakebo, simple_spinner_item);
        adc.setDropDownViewResource(simple_spinner_item);
        kakebo.setAdapter(adc);
        kakebo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int ai, long l) {
              kakeboDe   = adapterView.getItemAtPosition(ai).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //   planPagDe = "Mensual"; // Valor por defecto
            }
        });
    }

    public void aceptar(View v) {
        if (validar()) {
            Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LlenadoPlanDeudas.this, MenuPrinc.class);
            startActivity(intent);
        }
    }

    public boolean validar() {
        boolean retorno = true;
        String concepto = txtConceptoDe.getText().toString().trim();
        String cantida = txtCantidadDe.getText().toString().trim();
        String fecha = txtFechaDe.getText().toString().trim();
        String comentario = txtComentarioDe.getText().toString().trim();


        if (concepto.isEmpty()) {
            txtConceptoDe.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (cantida.isEmpty()) {
            txtCantidadDe.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
     //   if (fecha.isEmpty()) {
       //     txtFechaDe.setError("Este campo NO puede quedar vacío");
         //   retorno = false;
       // }

        if (retorno) {
            try {
                int cantidad = Integer.parseInt(cantida);
                pago_deuda(concepto, cantidad, fecha, planPagDe, kakeboDe, comentario);
            } catch (NumberFormatException e) {
                txtCantidadDe.setError("Introduce un número válido");
                retorno = false;
            }
        }
        return retorno;
    }

    private void pago_deuda(String concepto, int cantidad, String fecha, String planPagDe,String kakeboDe, String comentario) {
        Map<String, Object> mapi = new HashMap<>();
        mapi.put("concepto", concepto);
        mapi.put("cantidad", cantidad);
        mapi.put("fecha", fecha);
        mapi.put("plan_pagos", planPagDe);
        mapi.put("kakebo",kakeboDe);  //SE DEBE DE CAMBIAR, EL KAKEBO NO SE DEBE DE GUARDAR, SOLO LA SUBCATEGORIA+
        mapi.put("comentario", comentario);

        basededatos.collection("pago_deuda").add(mapi)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(LlenadoPlanDeudas.this, "Pago guardado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoPlanDeudas.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}
