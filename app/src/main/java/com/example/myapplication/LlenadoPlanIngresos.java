package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
//DECLARAR VARIABLES
    Button btnAceptarIngPlan;
    ImageButton btnAgregarImgIngPlan, btnCancelarIngPlan;
    EditText txtConceptoIngPlan, txtCantidadIngPlan, txtFechaIngPlan, txtComentarioIngPlan;
    TextView txtQuincenaIngPlan;
    private FirebaseFirestore basededatos;
    //
    final Calendar calendarioIngPlan = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_llenado_plan_ingresos);
        basededatos = FirebaseFirestore.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //INICIALIZAR COMPONENTES
        btnAceptarIngPlan = findViewById(R.id.btnAceptarIngPlan);
        btnAgregarImgIngPlan = findViewById(R.id.btnAgregarImgIngPlan);
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
        btnCancelarIngPlan.setOnClickListener(v -> {
            Toast.makeText(LlenadoPlanIngresos.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
    private void mostrarCalendarioIngPlan() {
        int año = calendarioIngPlan.get(Calendar.YEAR);
        int mes = calendarioIngPlan.get(Calendar.MONTH);
        int día = calendarioIngPlan.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
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
                año, mes, día
        );
        datePickerDialog.show();
    }
    private void actualizarFechaEnEditText() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtFechaIngPlan.setText(formato.format(calendarioIngPlan.getTime()));
    }
    public void aceptar11(View v){
        if (validar11()) {
            Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LlenadoPlanIngresos.this, MenuPrinc.class);
            startActivity(intent);
        }
    }
    public boolean validar11(){
        boolean retorno = true;
        String concepto = txtConceptoIngPlan.getText().toString().trim();
        String cantidad = txtCantidadIngPlan.getText().toString().trim();
        String comentario = txtComentarioIngPlan.getText().toString().trim();
        String fecha1 = txtFechaIngPlan.getText().toString().trim();

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
        if (retorno) {
            try {
                int cantida = Integer.parseInt(cantidad);
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fecha = formato.parse(fecha1);
                plan_ingresos(concepto, cantida, comentario, fecha);

            } catch (NumberFormatException e) {
                txtCantidadIngPlan.setError("Introduce un número válido");
                retorno = false;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return retorno;
    }
    private void plan_ingresos(String concepto, int cantida, String comentario, Date fecha) {

        Map<String, Object> mapiiis = new HashMap<>();
        mapiiis.put("concepto", concepto);
        mapiiis.put("cantidad", cantida);
        mapiiis.put ("comentario", comentario);
        mapiiis.put("fecha", fecha);
        // mapii.put("fecha", new Timestamp(fecha));
        basededatos.collection("plan_ingresos").add(mapiiis)
          .addOnFailureListener(e -> {
          Toast.makeText(LlenadoPlanIngresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}
