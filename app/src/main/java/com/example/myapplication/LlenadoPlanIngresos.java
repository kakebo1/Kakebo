package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

public class LlenadoPlanIngresos extends AppCompatActivity {
    Button btnAceptarIngPlan;
    ImageButton btnAgregarImgIngPlan, btnCancelarIngPlan;
    EditText txtConceptoIngPlan, txtCantidadIngPlan, txtFechaIngPlan, txtComentarioIngPlan;
    TextView txtQuincenaIngPlan;
    private FirebaseFirestore basededatos;

    final Calendar calendarioIngPlan = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_llenado_plan_ingresos);
        basededatos = FirebaseFirestore.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //INICIALIZAR COMPONENTES
        btnAceptarIngPlan = findViewById(R.id.btnAceptarPlanIng);

        btnCancelarIngPlan = findViewById(R.id.btnCancelarIngPlan);
        txtConceptoIngPlan = findViewById(R.id.txtConceptoIngPlan);
        txtCantidadIngPlan =findViewById(R.id.txtCantidadIngPlan);
        txtFechaIngPlan = findViewById(R.id.txtFechaIngPlan);
        txtComentarioIngPlan= findViewById(R.id.txtComentarioIngPlan);
        //INICIALIZAR TEXTVIEW DE QUINCENA
        txtQuincenaIngPlan = findViewById(R.id.txtQuincenaIngPlan);

        // ABRIR DATE PICKER
        txtFechaIngPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioIngPlan();
            }
        });

        btnAceptarIngPlan.setOnClickListener(this::aceptar);

        btnCancelarIngPlan.setOnClickListener(v -> {
            Toast.makeText(LlenadoPlanIngresos.this, "Registro cancelado", Toast.LENGTH_SHORT).show();
            finish();
        });

        Spinner categoria = findViewById(R.id.categoriaPlanIng);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.ingresos, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categoria.setAdapter(ad);


    }

    private void mostrarCalendarioIngPlan() {
        int year1 = calendarioIngPlan.get(Calendar.YEAR);
        int month1 = calendarioIngPlan.get(Calendar.MONTH);
        int day = calendarioIngPlan.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(
                LlenadoPlanIngresos.this,
                (view, year, month, dayOfMonth) -> {
                    calendarioIngPlan.set(Calendar.YEAR, year);
                    calendarioIngPlan.set(Calendar.MONTH, month);
                    calendarioIngPlan.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    actualizarFechaEnEditText();
                    //Modificar el TextView para mostrar la quincena
                    if (dayOfMonth < 15){
                        txtQuincenaIngPlan.setText("Quincena 1");
                    }else {
                        txtQuincenaIngPlan.setText("Quincena 2");
                    }
                },
                year1, month1, day
        );
        datePickerDialog.show();
    }

    private void actualizarFechaEnEditText() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtFechaIngPlan.setText(formato.format(calendarioIngPlan.getTime()));
    }

    public void aceptar(View v){
        if (validar()) {
            Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LlenadoPlanIngresos.this, MenuPrinc.class);
            startActivity(intent);
        }
    }

    public boolean validar(){
        boolean retorno = true;
        String concepto = txtConceptoIngPlan.getText().toString().trim();
        String cantidad = txtCantidadIngPlan.getText().toString().trim();
        String comentario = txtComentarioIngPlan.getText().toString().trim();
        String fecha1 = txtFechaIngPlan.getText().toString().trim();
        String categoriaSel = ((Spinner) findViewById(R.id.categoriaPlanIng)).getSelectedItem().toString();


        if (concepto.isEmpty()) {
            txtConceptoIngPlan.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (cantidad.isEmpty()){
            txtCantidadIngPlan.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (fecha1.isEmpty()){
            txtFechaIngPlan.setError("Ingrese una fecha");
            retorno = false;
        }
        if(categoriaSel.isEmpty()){
            Toast.makeText(LlenadoPlanIngresos.this, "Seleccione una categoria", Toast.LENGTH_SHORT).show();
        }
        if (retorno) {
            try {
                int cantida = Integer.parseInt(cantidad);
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fecha = formato.parse(fecha1);
                plan_ingresos(concepto, cantida, fecha, comentario, categoriaSel);

            } catch (NumberFormatException e) {
                txtCantidadIngPlan.setError("Introduce un número válido");
                retorno = false;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return retorno;
    }

    private void plan_ingresos(String concepto, int cantida, Date fecha, String comentario, String categoria) {

        Map<String, Object> mapiiis = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mapiiis.put("concepto", concepto);
        mapiiis.put("cantidad", cantida);
        mapiiis.put("fecha", format.format(fecha));
        mapiiis.put ("comentario", comentario);
        mapiiis.put("categoria", categoria);

        basededatos.collection("plan_ingresos").add(mapiiis)
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoPlanIngresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}