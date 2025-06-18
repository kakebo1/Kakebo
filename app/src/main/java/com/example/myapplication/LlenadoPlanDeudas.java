package com.example.myapplication;

import static android.R.layout.simple_spinner_item;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class LlenadoPlanDeudas extends AppCompatActivity {
    EditText txtConceptoPlanDe, txtFechaPlanDe, txtCantidadPlanDe, txtComentarioPlanDe;
    Button btnAceptarPlanDe;
    ImageButton btnCancelarPlanDe, btnAgregarImgPlanDe;
    private FirebaseFirestore basededatos;
    final Calendar calendarioPlanDe = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_llenado_plan_deudas);

        basededatos = FirebaseFirestore.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtConceptoPlanDe = findViewById(R.id.txtConceptoPlanDe);
        txtComentarioPlanDe = findViewById(R.id.txtComentariosPlanDe);
        txtCantidadPlanDe = findViewById(R.id.txtCantidadPlanDe);
        txtFechaPlanDe = findViewById(R.id.txtFechaPlanDe);
        btnAceptarPlanDe = findViewById(R.id.btnAceptarPlanDe);
        btnCancelarPlanDe = findViewById(R.id.btnCancelarPlanDe);

        txtFechaPlanDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioEgPlan();
            }

            private void mostrarCalendarioEgPlan() {
                int year1 = calendarioPlanDe.get(Calendar.YEAR);
                int month1 = calendarioPlanDe.get(Calendar.MONTH);
                int day = calendarioPlanDe.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LlenadoPlanDeudas.this,
                        (view, year, month, dayOfMonth) -> {
                            calendarioPlanDe.set(Calendar.YEAR, year);
                            calendarioPlanDe.set(Calendar.MONTH, month);
                            calendarioPlanDe.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            actualizarFechaEnEditText();
                        },
                        year1, month1, day
                );
                datePickerDialog.show();
            }

            private void actualizarFechaEnEditText() {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                txtFechaPlanDe.setText(formato.format(calendarioPlanDe.getTime()));
            }
        });

        Spinner categoria = findViewById(R.id.kakeboPlanDe);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.kakebo, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categoria.setAdapter(ad);

        Spinner subcategoria = findViewById(R.id.subcategoriaPlanDe);
        Map<String, List<String>> subcategoriasMap = new HashMap<>();

        subcategoriasMap.put("Supervivencia", Arrays.asList("Alimentación", "Servicios del Hogar", "Transporte"));
        subcategoriasMap.put("Ocio", Arrays.asList("Cine", "Restaurant", "Videojuegos", "Fiesta"));
        subcategoriasMap.put("Cultural", Arrays.asList("Libros", "Museo", "Concierto"));
        subcategoriasMap.put("Otros", Arrays.asList("Regalos", "Donacion", "Plantas"));

        Spinner planPagosDe = findViewById(R.id.plandepagosDe);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.plazospagodeuda, simple_spinner_item);
        adapter.setDropDownViewResource(simple_spinner_item);
        planPagosDe.setAdapter(adapter);

        btnAceptarPlanDe.setOnClickListener(this::aceptar);

        btnCancelarPlanDe.setOnClickListener(v -> {
            Toast.makeText(LlenadoPlanDeudas.this, "Registro cancelado", Toast.LENGTH_SHORT).show();
            finish();
        });

        List<String> categorias = new ArrayList<>(subcategoriasMap.keySet());
        ArrayAdapter<String> categoriaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(categoriaAdapter);

        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSel = categorias.get(position);
                actualizarSubcategorias(categoriaSel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            private void actualizarSubcategorias(String categoria){
                List<String> subcategorias;
                if(subcategoriasMap.containsKey(categoria)){
                    subcategorias = subcategoriasMap.get(categoria);
                } else{
                    subcategorias = new ArrayList<>();
                }
                assert subcategorias != null;
                ArrayAdapter<String> subcategoriaAdapter = new ArrayAdapter<>(LlenadoPlanDeudas.this, android.R.layout.simple_spinner_item, subcategorias);
                subcategoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subcategoria.setAdapter(subcategoriaAdapter);
            }
        });
    }

    public void aceptar(View v) {
        if (validar()) {
            Intent intent = new Intent(LlenadoPlanDeudas.this, LlenadoPlanDeudas.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean validar() {
        boolean retorno = true;

        String concepto = txtConceptoPlanDe.getText().toString().trim();
        String cantidad = txtCantidadPlanDe.getText().toString().trim();
        String fecha = txtFechaPlanDe.getText().toString().trim();
        String comentario = txtComentarioPlanDe.getText().toString().trim();
        String categoriaSel = ((Spinner) findViewById(R.id.kakeboPlanDe)).getSelectedItem().toString();
        String subcategoriaSel = ((Spinner) findViewById(R.id.subcategoriaPlanDe)).getSelectedItem().toString();
        String planDePagos = ((Spinner) findViewById(R.id.plandepagosDe)).getSelectedItem().toString();

        if (concepto.isEmpty()) {
            txtConceptoPlanDe.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (cantidad.isEmpty()) {
            txtCantidadPlanDe.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (fecha.isEmpty()) {
            txtFechaPlanDe.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (retorno) {
            int cantidad1 = Integer.parseInt(cantidad);
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fecha1 = null;
            try {
                fecha1 = formato.parse(fecha);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            plan_deudas(concepto, cantidad1, fecha1, comentario, categoriaSel, subcategoriaSel, planDePagos);
        }
        return retorno;
    }

    private void plan_deudas(String concepto, int cantidad, Date fecha, String comentario, String categoria, String subcategoria, String planPagosDe) {
        Map<String, Object> mapiii = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mapiii.put("concepto", concepto);
        mapiii.put("cantidad", cantidad);
        mapiii.put("fecha", format.format(fecha));
        mapiii.put("comentario", comentario);
        mapiii.put("categoria", categoria);
        mapiii.put("subcategoria", subcategoria);
        mapiii.put("plan_pagos", planPagosDe);

        basededatos.collection("plan_deuda").add(mapiii)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(LlenadoPlanDeudas.this, "Plan de egreso guardado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoPlanDeudas.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}