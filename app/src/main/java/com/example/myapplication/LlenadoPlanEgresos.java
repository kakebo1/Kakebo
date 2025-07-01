package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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

public class LlenadoPlanEgresos extends AppCompatActivity {

    EditText txtConceptoEgPlan, txtFechaEgPlan, txtCantidadEgPlan, txtComentarioEgPlan;
    Button btnAceptarEgPlan;
    ImageButton btnCancelarEgPlan, btnAgregarImgEgPlan;
    RadioButton btnfijoEgPlan, btnvariableEgPlan;
    private FirebaseFirestore basededatos;
    final Calendar calendarioEgPlan = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_llenado_plan_egresos);

        basededatos = FirebaseFirestore.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtConceptoEgPlan = findViewById(R.id.txtConceptoEgPlan);
        txtComentarioEgPlan = findViewById(R.id.txtComentarioEgPlan);
        txtCantidadEgPlan = findViewById(R.id.txtCantidadEgPlan);
        txtFechaEgPlan = findViewById(R.id.txtFechaEgPlan);
        btnAceptarEgPlan = findViewById(R.id.btnAceptarEgPlan);
        btnCancelarEgPlan = findViewById(R.id.btnCancelarEgPlan);

        txtFechaEgPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioEgPlan();
            }

            private void mostrarCalendarioEgPlan() {
                int year1 = calendarioEgPlan.get(Calendar.YEAR);
                int month1 = calendarioEgPlan.get(Calendar.MONTH);
                int day = calendarioEgPlan.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LlenadoPlanEgresos.this,
                        (view, year, month, dayOfMonth) -> {
                            calendarioEgPlan.set(Calendar.YEAR, year);
                            calendarioEgPlan.set(Calendar.MONTH, month);
                            calendarioEgPlan.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            actualizarFechaEnEditText();
                        },
                        year1, month1, day
                );
                datePickerDialog.show();
            }

            private void actualizarFechaEnEditText() {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                txtFechaEgPlan.setText(formato.format(calendarioEgPlan.getTime()));
            }
        });

        Spinner categoria = findViewById(R.id.kakeboEgPlan);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.kakebo, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categoria.setAdapter(ad);

        Spinner subcategoria = findViewById(R.id.subcategoriaEgPlan);
        Map<String, List<String>> subcategoriasMap = new HashMap<>();

        subcategoriasMap.put("Supervivencia", Arrays.asList("Alimentación", "Servicios del Hogar", "Transporte"));
        subcategoriasMap.put("Ocio", Arrays.asList("Cine", "Restaurant", "Videojuegos", "Fiesta"));
        subcategoriasMap.put("Cultural", Arrays.asList("Libros", "Museo", "Concierto"));
        subcategoriasMap.put("Otros", Arrays.asList("Regalos", "Donacion", "Plantas"));

        btnAceptarEgPlan.setOnClickListener(this::aceptar);

        btnCancelarEgPlan.setOnClickListener(v -> {
            Toast.makeText(LlenadoPlanEgresos.this, "Registro cancelado", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<String> subcategoriaAdapter = new ArrayAdapter<>(LlenadoPlanEgresos.this, android.R.layout.simple_spinner_item, subcategorias);
                subcategoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subcategoria.setAdapter(subcategoriaAdapter);
            }
        });
    }

    public void aceptar(View v) {
        if (validar()) {
            Intent intent = new Intent(LlenadoPlanEgresos.this, LlenadoPlanEgresos.class);
            startActivity(intent);
            finish();  // Finaliza la actividad actual después de abrir el menú principal
        }
    }

    public boolean validar() {
        boolean retorno = true;

        String concepto = txtConceptoEgPlan.getText().toString().trim();
        String cantidad = txtCantidadEgPlan.getText().toString().trim();
        String fecha = txtFechaEgPlan.getText().toString().trim();
        String comentario = txtComentarioEgPlan.getText().toString().trim();
        String categoriaSel = ((Spinner) findViewById(R.id.kakeboEgPlan)).getSelectedItem().toString();
        String subcategoriaSel = ((Spinner) findViewById(R.id.subcategoriaEgPlan)).getSelectedItem().toString();

        if (concepto.isEmpty()) {
            txtConceptoEgPlan.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }

        //  int cantidad = 0;
        if (cantidad.isEmpty()) {
            txtCantidadEgPlan.setError("Este campo NO puede quedar vacío");
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
            planEgreso(concepto, cantidad1, fecha1, comentario, categoriaSel, subcategoriaSel);
        }
        return retorno;
    }

    private void planEgreso(String concepto, int cantidad, Date fecha, String comentario, String categoria, String subcategoria) {
        Map<String, Object> mapiii = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mapiii.put("concepto", concepto);
        mapiii.put("cantidad", cantidad);
        mapiii.put("fecha", format.format(fecha));
        mapiii.put("comentario", comentario);
        mapiii.put("categoria", categoria);
        mapiii.put("subcategoria", subcategoria);

        basededatos.collection("plan_egresos").add(mapiii)
                .addOnSuccessListener(documentReference -> {
                           Toast.makeText(LlenadoPlanEgresos.this, "Plan de egreso guardado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoPlanEgresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}