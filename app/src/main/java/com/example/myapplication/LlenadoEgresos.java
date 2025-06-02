package com.example.myapplication;

import static android.R.layout.simple_spinner_item;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LlenadoEgresos extends AppCompatActivity {
    // DECLARAR VARIABLES
    Button btnAceptarEg;
    ImageButton btnAgregarImg, btnCancelarEg;
    EditText txtConceptoEg, txtCantidadEg, txtFechaEg, txtComentarioEg;
    Switch switchDeuda;
    RadioButton fijo, variable;
    private FirebaseFirestore basededatos;
    final Calendar calendarioEg = Calendar.getInstance();

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imagenUri;

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
        txtConceptoEg = findViewById(R.id.txtConceptoEg);
        txtCantidadEg = findViewById(R.id.txtCantidadEg);
        txtFechaEg = findViewById(R.id.txtFechaEg);
        txtComentarioEg = findViewById(R.id.txtComentarioEg);
        btnAceptarEg = findViewById(R.id.btnAceptarEg);
        btnCancelarEg = findViewById(R.id.btnCancelarEg);
        btnAgregarImg = findViewById(R.id.btnAgregarImgEg);
        switchDeuda = findViewById(R.id.switchDeuda);

        txtFechaEg.setOnClickListener(v -> mostrarCalendarioEg());
        btnAgregarImg.setOnClickListener(v -> abrirGaleria());

        // SPINNER CATEGORÍA
        Spinner categoria = findViewById(R.id.categoriaspn);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.kakebo, simple_spinner_item);
        ad.setDropDownViewResource(simple_spinner_item);
        categoria.setAdapter(ad);

        // SPINNER KAKEBO
        Spinner kakebo = findViewById(R.id.kakebospn);
        ArrayAdapter<CharSequence> adc = ArrayAdapter.createFromResource(this, R.array.kakebo, simple_spinner_item);
        adc.setDropDownViewResource(simple_spinner_item);
        kakebo.setAdapter(adc);

        btnAceptarEg.setOnClickListener(this::aceptar2);

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
            // Ya no se hace nada aquí. Se espera confirmación en egreso().
        }
    }

    public boolean validar2() {
        boolean retorno = true;

        String concepto = txtConceptoEg.getText().toString().trim();
        String cantidadStr = txtCantidadEg.getText().toString().trim();
        String fechaStr = txtFechaEg.getText().toString().trim();
        String comentario = txtComentarioEg.getText().toString().trim();

        if (concepto.isEmpty()) {
            txtConceptoEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }

        if (cantidadStr.isEmpty()) {
            txtCantidadEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }

        if (fechaStr.isEmpty()) {
            txtFechaEg.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }

        if (retorno) {
            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                txtCantidadEg.setError("Introduce un número válido");
                return false;
            }

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fecha;
            try {
                fecha = formato.parse(fechaStr);
            } catch (ParseException e) {
                txtFechaEg.setError("Formato de fecha inválido");
                return false;
            }

            egreso(concepto, cantidad, fecha, comentario);
        }

        return retorno;
    }

    private void egreso(String concepto, int cantidad, Date fecha, String comentario) {
        Map<String, Object> mapiii = new HashMap<>();
        mapiii.put("concepto", concepto);
        mapiii.put("cantidad", cantidad);
        mapiii.put("fecha", fecha);
        mapiii.put("comentario", comentario);

        String tabla = switchDeuda.isChecked() ? "pago_deuda" : "egresos";

        if (imagenUri != null) {
            String nombreArchivo = "imagenes/" + System.currentTimeMillis() + ".jpg";
            FirebaseStorage.getInstance().getReference(nombreArchivo).putFile(imagenUri)
                    .addOnSuccessListener(taskSnapshot ->
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                mapiii.put("imagen", uri.toString());

                                basededatos.collection(tabla).add(mapiii)
                                        .addOnSuccessListener(docRef -> {
                                            Toast.makeText(LlenadoEgresos.this, "Egreso guardado con imagen", Toast.LENGTH_SHORT).show();
                                            irAMenuPrincipal();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(LlenadoEgresos.this, "Error al guardar egreso", Toast.LENGTH_SHORT).show());
                            }))
                    .addOnFailureListener(e ->
                            Toast.makeText(LlenadoEgresos.this, "Error al subir imagen", Toast.LENGTH_SHORT).show());
        } else {
            basededatos.collection(tabla).add(mapiii)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(LlenadoEgresos.this, "Egreso guardado sin imagen", Toast.LENGTH_SHORT).show();
                        irAMenuPrincipal();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(LlenadoEgresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show());
        }
    }

    private void irAMenuPrincipal() {
        Intent intent = new Intent(LlenadoEgresos.this, MenuPrinc.class);
        startActivity(intent);
        finish();
    }

    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData();
            Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
        }
    }
}
