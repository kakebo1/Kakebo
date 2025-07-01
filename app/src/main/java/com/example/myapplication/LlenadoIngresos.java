package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
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

    Button btnAceptarIng;
    ImageButton btnAgregarImgIng, btnCancelarIng;
    EditText txtConceptoIng, txtCantidadIng, txtComentarioIng, txtFechaIng;
    TextView txtQuincena;
    private FirebaseFirestore basededatos;
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


        txtConceptoIng = findViewById(R.id.txtConceptoIng);
        txtCantidadIng = findViewById(R.id.txtCantidadIng);
        txtComentarioIng = findViewById(R.id.txtComentarioIng);
        txtFechaIng = findViewById(R.id.txtFechaIng);
        btnCancelarIng = findViewById(R.id.btnCancelarIng);
        txtQuincena = findViewById(R.id.txtQuincena);

        Spinner categoria = findViewById(R.id.categoriaIng);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.ingresos, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categoria.setAdapter(ad);


        // Abrir el DatePicker al hacer clic
        txtFechaIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioIng();
            }
        });
        btnCancelarIng.setOnClickListener(v -> {
            Toast.makeText(LlenadoIngresos.this, "Registro cancelado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void mostrarCalendarioIng() {
        int year1 = calendarioIng.get(Calendar.YEAR);
        int month1 = calendarioIng.get(Calendar.MONTH);
        int day = calendarioIng.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(
                LlenadoIngresos.this,
                (view, year, month, dayOfMonth) -> {
                    calendarioIng.set(Calendar.YEAR, year);
                    calendarioIng.set(Calendar.MONTH, month);
                    calendarioIng.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    actualizarFechaEnEditText();

                    if(dayOfMonth < 15){
                        txtQuincena.setText("Quincena 1");
                    } else{
                        txtQuincena.setText("Quincena 2");
                    }
                },
                year1, month1, day
        );
        datePickerDialog.show();
    }

    private void actualizarFechaEnEditText() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtFechaIng.setText(formato.format(calendarioIng.getTime()));
    }

    public void aceptar1(View v){
        if (validar1()) {
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
        String categoriaSel = ((Spinner) findViewById(R.id.categoriaIng)).getSelectedItem().toString();

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
        if (categoriaSel.isEmpty()){
            Toast.makeText(LlenadoIngresos.this, "Seleccione una categoria", Toast.LENGTH_SHORT).show();
        }
        if (retorno) {
            try {
                int cantida = Integer.parseInt(cantidad);
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fecha = formato.parse(fecha1);
                ingresos(concepto, cantida, fecha, comentario, categoriaSel);

            } catch (NumberFormatException e) {
                txtCantidadIng.setError("Introduce un número válido");
                retorno = false;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return retorno;
    }
    private void ingresos(String concepto, int cantida, Date fecha, String comentario, String categoria) {

        Map<String, Object> mapii = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mapii.put("concepto", concepto);
        mapii.put("cantidad", cantida);
        mapii.put("fecha", format.format(fecha));
        mapii.put ("comentario", comentario);
        mapii.put("categoria", categoria);

        basededatos.collection("ingresos").add(mapii)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(LlenadoIngresos.this, "Ingreso guardado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoIngresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}
