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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LlenadoDeudas extends AppCompatActivity {
    Button btnAceptarDe;
    ImageButton btnCancelarDeuda;
    EditText txtConceptoDe, txtCantidadDe, txtFechaDe, txtComentarioDe;
    String planPagDe, kakeboDe;
    private FirebaseFirestore basededatos;
    final Calendar calendarioDe = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_llenado_deudas);
        basededatos = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnAceptarDe = findViewById(R.id.btnAceptarPlan);
        btnCancelarDeuda = findViewById(R.id.btnCancelarPlan);
        txtConceptoDe = findViewById(R.id.txtConceptoPlan);
        txtCantidadDe = findViewById(R.id.txtCantidadPlan);
        txtFechaDe = findViewById(R.id.txtFechaPlan);
        txtComentarioDe = findViewById(R.id.txtComentariosPlan);

        // Abrir el DatePicker al hacer clic
        txtFechaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioDe();
            }
        });

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

        Spinner categoria = findViewById(R.id.kakeboDe);
        ArrayAdapter<CharSequence> adc = ArrayAdapter.createFromResource(this, R.array.kakebo, simple_spinner_item);
        adc.setDropDownViewResource(simple_spinner_item);
        categoria.setAdapter(adc);

        Spinner subcategoria = findViewById(R.id.subcategoriaDeuda);
        Map<String, List<String>> subcategoriasMap = new HashMap<>();

        subcategoriasMap.put("Supervivencia", Arrays.asList("Alimentación", "Servicios del Hogar", "Transporte"));
        subcategoriasMap.put("Ocio", Arrays.asList("Cine", "Restaurant", "Videojuegos", "Fiesta"));
        subcategoriasMap.put("Cultural", Arrays.asList("Libros", "Museo", "Concierto"));
        subcategoriasMap.put("Otros", Arrays.asList("Regalos", "Donacion", "Plantas"));

        btnAceptarDe.setOnClickListener(this::aceptar);
        btnCancelarDeuda.setOnClickListener(v-> {
            Toast.makeText(LlenadoDeudas.this, "Registro cancelado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
    private void mostrarCalendarioDe() {
        int year1 = calendarioDe.get(Calendar.YEAR);
        int month1 = calendarioDe.get(Calendar.MONTH);
        int day = calendarioDe.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                LlenadoDeudas.this,
                (view, year, month, dayOfMonth) -> {
                    calendarioDe.set(Calendar.YEAR, year);
                    calendarioDe.set(Calendar.MONTH, month);
                    calendarioDe.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    actualizarFechaEnEditText1();
                },
                year1, month1, day
        );
        datePickerDialog.show();
    }
    private void actualizarFechaEnEditText1() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtFechaDe.setText(formato.format(calendarioDe.getTime()));
    }
    public void aceptar(View v) {
        if (validar()) {
            Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LlenadoDeudas.this, MenuPrinc.class);
            startActivity(intent);
        }
    }

    public boolean validar() {
        boolean retorno = true;
        String concepto = txtConceptoDe.getText().toString().trim();
        String cantida = txtCantidadDe.getText().toString().trim();
        //  String fecha = txtFechaDe.getText().toString().trim();
        String comentario = txtComentarioDe.getText().toString().trim();
        String fecha1 = txtFechaDe.getText().toString().trim();


        if (concepto.isEmpty()) {
            txtConceptoDe.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (cantida.isEmpty()) {
            txtCantidadDe.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (fecha1.isEmpty()){
            txtFechaDe.setError("Ingrese una fecha");
            retorno = false;
        }

        if (retorno) {
            try {
                int cantidad = Integer.parseInt(cantida);
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fecha = formato.parse(fecha1);
                pago_deuda(concepto, cantidad, fecha, planPagDe, kakeboDe, comentario);
            } catch (NumberFormatException e) {
                txtCantidadDe.setError("Introduce un número válido");
                retorno = false;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return retorno;
    }

    private void pago_deuda(String concepto, int cantidad, Date fecha, String planPagDe,String kakeboDe, String comentario) {
        Map<String, Object> mapi = new HashMap<>();
        mapi.put("concepto", concepto);
        mapi.put("cantidad", cantidad);
        mapi.put("fecha", fecha);
        mapi.put("plan_pagos", planPagDe);
        mapi.put("kakebo",kakeboDe);  //SE DEBE DE CAMBIAR, EL KAKEBO NO SE DEBE DE GUARDAR, SOLO LA SUBCATEGORIA+
        mapi.put("comentario", comentario);

        basededatos.collection("pago_deuda").add(mapi)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(LlenadoDeudas.this, "Pago guardado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoDeudas.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}