package com.example.myapplication;

import static android.R.layout.simple_spinner_item;

import android.app.DatePickerDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LlenadoEgresos extends AppCompatActivity {
    //DECLARAR VARIABLES
    Button btnAceptarEg;
    ImageButton btnAgregarImg, btnCancelarEg;
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

        // INICIALIZAR COMPONENTES
        txtConceptoEg= findViewById(R.id.txtConceptoEg);
        txtCantidadEg= findViewById(R.id.txtCantidadEg);
        txtFechaEg= findViewById(R.id.txtFechaEg);
        txtComentarioEg= findViewById(R.id.txtComentarioEg);
        btnAceptarEg = findViewById(R.id.btnAceptarEg);
        btnCancelarEg = findViewById(R.id.btnCancelarEg);
        switchDeuda = findViewById(R.id.switchDeuda);
        txtFechaEg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioEg();
            }
        });
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
    private void mostrarCalendarioEg() {
        int año = calendarioEg.get(Calendar.YEAR);
        int mes = calendarioEg.get(Calendar.MONTH);
        int día = calendarioEg.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                LlenadoEgresos.this,
                (view, year, month, dayOfMonth) -> {
                    calendarioEg.set(Calendar.YEAR, year);
                    calendarioEg.set(Calendar.MONTH, month);
                    calendarioEg.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    actualizarFechaEnEditText();
                },
                año, mes, día
        );
        datePickerDialog.show();
    }
    private void actualizarFechaEnEditText() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtFechaEg.setText(formato.format(calendarioEg.getTime()));
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
        String cantidad = txtCantidadEg.getText().toString().trim();
        String fecha2 = txtFechaEg.getText().toString().trim();
        String comentario = txtComentarioEg.getText().toString().trim();

        if (concepto.isEmpty()) {
            txtConceptoEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }

      //  int cantidad = 0;
        if (cantidad.isEmpty()) {
            txtCantidadEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (retorno) {
            int cantida = Integer.parseInt(cantidad);
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fecha = null;
            try {
                fecha = formato.parse(fecha2);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            egreso(concepto, cantida, fecha, comentario);
        }

        return retorno;
    }


    private void egreso(String concepto, int cantidad, Date fecha, String comentario) {
        Map<String, Object> mapiii = new HashMap<>();
        mapiii.put("concepto", concepto);
        mapiii.put("cantidad", cantidad);
        mapiii.put("fecha", fecha);
        mapiii.put("comentario", comentario);
        //VARIABLE TABLA

        String tabla;
        // SI EL SWITCH DEUDA ESTÁ ACTIVO LA TABLA SERÁ DEUDA,
        if (switchDeuda.isChecked()){
            tabla = "pago_deuda";
            // SI NO, LA TABLA SE LLAMARÁ EGRESOS
        } else{
            tabla = "egresos";
        }
        // LOS DATOS SE MANDAN A LA TABLA ANTERIORMENTE LLAMADA
        basededatos.collection(tabla).add(mapiii)
                .addOnSuccessListener(documentReference -> {
             //       Toast.makeText(LlenadoEgresos.this, "Pago guardado con éxito", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoEgresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
    }

