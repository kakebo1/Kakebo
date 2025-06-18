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
import android.net.Uri;
import androidx.annotation.Nullable;
import com.google.firebase.storage.FirebaseStorage;

public class LlenadoEgresos extends AppCompatActivity {

    Button btnAceptarEg;
    ImageButton btnAgregarImg, btnCancelarEgreso;
    EditText txtConceptoEg, txtCantidadEg, txtFechaEg, txtComentarioEg;
    Switch switchDeuda;
    RadioButton fijo, variable;
    private FirebaseFirestore basededatos;
    final Calendar calendarioEg = Calendar.getInstance();

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

        btnAceptarEg = findViewById(R.id.btnAceptarPlan);
        btnCancelarEgreso = findViewById(R.id.btnCancelarEg);
        txtConceptoEg = findViewById(R.id.txtConceptoEgReal);
        txtCantidadEg = findViewById(R.id.txtCantidadEgReal);
        txtFechaEg = findViewById(R.id.txtFechaEgReal);
        txtComentarioEg = findViewById(R.id.txtComentariosEgReal);
        switchDeuda = findViewById(R.id.switchDeuda);
        fijo = findViewById(R.id.btnFijo);
        variable = findViewById(R.id.btnVariable);


        // Abrir el DatePicker al hacer clic
        txtFechaEg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioIng();
            }

            private void mostrarCalendarioIng() {
                int year1 = calendarioEg.get(Calendar.YEAR);
                int month1 = calendarioEg.get(Calendar.MONTH);
                int day = calendarioEg.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LlenadoEgresos.this,
                        (view, year, month, dayOfMonth) -> {
                            calendarioEg.set(Calendar.YEAR, year);
                            calendarioEg.set(Calendar.MONTH, month);
                            calendarioEg.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            actualizarFechaEnEditText();
                        },
                        year1, month1, day
                );
                datePickerDialog.show();
            }

            private void actualizarFechaEnEditText() {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                txtFechaEg.setText(formato.format(calendarioEg.getTime()));
            }
        });


        Spinner categoria = findViewById(R.id.categoriaEgReal);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.kakebo, simple_spinner_item);
        ad.setDropDownViewResource(simple_spinner_item);
        categoria.setAdapter(ad);

        Spinner subcategoria = findViewById(R.id.subcategoriaEgReal);
        Map<String, List<String>> subcategoriasMap = new HashMap<>();

        subcategoriasMap.put("Supervivencia", Arrays.asList("Alimentación", "Servicios del Hogar", "Transporte"));
        subcategoriasMap.put("Ocio", Arrays.asList("Cine", "Restaurant", "Videojuegos", "Fiesta"));
        subcategoriasMap.put("Cultural", Arrays.asList("Libros", "Museo", "Concierto"));
        subcategoriasMap.put("Otros", Arrays.asList("Regalos", "Donacion", "Plantas"));

        btnAceptarEg.setOnClickListener(this::aceptar);

        btnCancelarEgreso.setOnClickListener(v-> {
            Toast.makeText(LlenadoEgresos.this, "Registro cancelado", Toast.LENGTH_SHORT).show();
            finish();
        });

        List<String> categorias = new ArrayList<>(subcategoriasMap.keySet());
        ArrayAdapter<String> categoriaAdapter = new ArrayAdapter<>(this, simple_spinner_item, categorias);
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(categoriaAdapter);

        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = categorias.get(position);
                actualizarSubcategorias(categoriaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            private void actualizarSubcategorias (String categoria){
                    List<String> subcategorias;
                    if(subcategoriasMap.containsKey(categoria)){
                        subcategorias = subcategoriasMap.get(categoria);
                    } else{
                        subcategorias = new ArrayList<>();
                    }

                assert subcategorias != null;
                ArrayAdapter<String> subcategoriaAdapter = new ArrayAdapter<>(LlenadoEgresos.this, simple_spinner_item, subcategorias);
                    subcategoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subcategoria.setAdapter(subcategoriaAdapter);
            }
        });
    }

    public void aceptar(View v) {
        if (validar()) {
            Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LlenadoEgresos.this, MenuPrinc.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean validar() {
        boolean retorno = true;

        String concepto = txtConceptoEg.getText().toString().trim();
        String cantidadStr = txtCantidadEg.getText().toString().trim();
        String fecha = txtFechaEg.getText().toString().trim();
        String comentario = txtComentarioEg.getText().toString().trim();
        String categoriaSel = ((Spinner) findViewById(R.id.categoriaEgReal)).getSelectedItem().toString();
        String subcategoriaSel = ((Spinner) findViewById(R.id.subcategoriaEgReal)).getSelectedItem().toString();

        if (concepto.isEmpty()) {
            txtConceptoEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }

        if (cantidadStr.isEmpty()) {
            txtCantidadEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }

        if (retorno) {
            int cantidad1 = Integer.parseInt(cantidadStr);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fecha1 = null;
            try{
                fecha1 = format.parse(fecha);
            } catch (ParseException e){
                throw new RuntimeException(e);
            }
            egreso(concepto, cantidad1, fecha1, comentario, categoriaSel, subcategoriaSel);
        }
        return retorno;
    }

    private void egreso(String concepto, int cantidad, Date fecha, String comentario, String categoria, String subcategoria) {
        Map<String, Object> mapii = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mapii.put("concepto", concepto);
        mapii.put("cantidad", cantidad);
        mapii.put("fecha", format.format(fecha));
        mapii.put("comentario", comentario);
        mapii.put("categoria", categoria);
        mapii.put("subcategoria", subcategoria);

        String tabla;
        if(switchDeuda.isChecked()){
            tabla = "pago_deuda";
        } else{
            tabla = "egresos";
        }
        basededatos.collection(tabla).add(mapii).addOnSuccessListener(documentReference -> {
            Toast.makeText(LlenadoEgresos.this, "Egreso guardado", Toast.LENGTH_SHORT).show();
        });
    }
}