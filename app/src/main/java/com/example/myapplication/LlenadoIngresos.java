package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class LlenadoIngresos extends AppCompatActivity {
    //DECLARAR COMPONENTES
Button btnAceptarIng;
ImageButton btnAgregarImgIng, btnCancelarIng;
EditText txtConceptoIng, txtCantidadIng, txtComentarioIng, txtFechaIng;
private FirebaseFirestore basededatos;
//
final Calendar calendarioIng = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_llenado_ingresos);
        basededatos = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //INICIALIZAR COMPONENTES
        btnAgregarImgIng = findViewById(R.id.btnAgregarImgIng);
        txtConceptoIng = findViewById(R.id.txtConceptoIng);
        txtCantidadIng = findViewById(R.id.txtCantidadIng);
        txtComentarioIng = findViewById(R.id.txtComentarioIng);
        txtFechaIng = findViewById(R.id.txtFechaIng);
        btnCancelarIng = findViewById(R.id.btnCancelarIng);

        // Abrir el DatePicker al hacer clic
        txtFechaIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioIng();
            }
        });
        btnCancelarIng.setOnClickListener(v -> {
            Toast.makeText(LlenadoIngresos.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
    private void mostrarCalendarioIng() {
        int año = calendarioIng.get(Calendar.YEAR);
        int mes = calendarioIng.get(Calendar.MONTH);
        int día = calendarioIng.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                LlenadoIngresos.this,
                (view, year, month, dayOfMonth) -> {
                    calendarioIng.set(Calendar.YEAR, year);
                    calendarioIng.set(Calendar.MONTH, month);
                    calendarioIng.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    actualizarFechaEnEditText();
                },
                año, mes, día
        );
        datePickerDialog.show();
    }

    private void actualizarFechaEnEditText() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtFechaIng.setText(formato.format(calendarioIng.getTime()));
    }

public void aceptar1(View v){
    if (validar1()) {
        Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LlenadoIngresos.this, MenuPrinc.class);
        startActivity(intent);
    }
}
public boolean validar1(){
    boolean retorno = true;
    String concepto = txtConceptoIng.getText().toString().trim();
    String cantidad = txtCantidadIng.getText().toString().trim();
    String comentario = txtComentarioIng.getText().toString().trim();
    String fecha1 = txtFechaIng.getText().toString().trim();

    if (concepto.isEmpty()) {
        txtConceptoIng.setError("Este campo NO puede quedar vacío");
        retorno = false;
    }
    if (cantidad.isEmpty()){
        txtCantidadIng.setError("Este campo NO puede quedar vacío");
        retorno = false;
    }
    if (fecha1.isEmpty()){
        txtFechaIng.setError("Ingrese una fecha");
        retorno = false;
    }
    if (retorno) {
        try {
            int cantida = Integer.parseInt(cantidad);
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fecha = formato.parse(fecha1);
            ingresos(concepto, cantida, comentario, fecha);

        } catch (NumberFormatException e) {
            txtCantidadIng.setError("Introduce un número válido");
            retorno = false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    return retorno;
}
    private void ingresos(String concepto, int cantida, String comentario, Date fecha) {

        Map<String, Object> mapii = new HashMap<>();
        mapii.put("concepto", concepto);
        mapii.put("cantidad", cantida);
        mapii.put ("comentario", comentario);
        mapii.put("fecha", fecha);
       // mapii.put("fecha", new Timestamp(fecha));
        basededatos.collection("ingresos").add(mapii)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(LlenadoIngresos.this, "Pago guardado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoIngresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}



