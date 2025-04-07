package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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

public class LlenadoPlanEgresos extends AppCompatActivity {
//DECLARAR VARIABLES
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
        setContentView(R.layout.act_llenado_plan_egresos);

        basededatos = FirebaseFirestore.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //INICIALIZAR COMPONENTES
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
        });
    }
        private void mostrarCalendarioEgPlan() {
            int año = calendarioEgPlan.get(Calendar.YEAR);
            int mes = calendarioEgPlan.get(Calendar.MONTH);
            int día = calendarioEgPlan.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    LlenadoPlanEgresos.this,
                    (view, year, month, dayOfMonth) -> {
                        calendarioEgPlan.set(Calendar.YEAR, year);
                        calendarioEgPlan.set(Calendar.MONTH, month);
                        calendarioEgPlan.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        actualizarFechaEnEditText();
                    },
                    año, mes, día
            );
            datePickerDialog.show();
        }
        private void actualizarFechaEnEditText() {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            txtFechaEgPlan.setText(formato.format(calendarioEgPlan.getTime()));
        }
        public void aceptar3(View v) {
            if (validar3()) {
                Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LlenadoPlanEgresos.this, MenuPrinc.class);
                startActivity(intent);
                finish();  // Finaliza la actividad actual después de abrir el menú principal
            }
        }
        public boolean validar3() {
            boolean retorno = true;

            String concepto = txtConceptoEgPlan.getText().toString().trim();
            String cantidad = txtCantidadEgPlan.getText().toString().trim();
            String fecha2 = txtFechaEgPlan.getText().toString().trim();
            String comentario = txtComentarioEgPlan.getText().toString().trim();

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
                int cantida = Integer.parseInt(cantidad);
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fecha = null;
                try {
                    fecha = formato.parse(fecha2);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                planEgreso(concepto, cantida, fecha, comentario);
            }

            return retorno;
        }
        private void planEgreso(String concepto, int cantidad, Date fecha, String comentario) {
            Map<String, Object> mapiii = new HashMap<>();
            mapiii.put("concepto", concepto);
            mapiii.put("cantidad", cantidad);
            mapiii.put("fecha", fecha);
            mapiii.put("comentario", comentario);


            basededatos.collection("plan_egresos").add(mapiii)
                    .addOnSuccessListener(documentReference -> {
                        //       Toast.makeText(LlenadoEgresos.this, "Pago guardado con éxito", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(LlenadoPlanEgresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                    });
        }
    }
