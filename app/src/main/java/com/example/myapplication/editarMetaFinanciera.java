package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editarMetaFinanciera extends AppCompatActivity {

    private EditText editCantidadObj, editMotivo;
    private Spinner spinnerPlazo;
    private Button btnGuardar, btnCancelar;
    private FirebaseFirestore db;
    private String idMetaFin;
    private String coleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_meta_financiera);

        db = FirebaseFirestore.getInstance();

        editCantidadObj = findViewById(R.id.editCantidadObj);
        editMotivo = findViewById(R.id.editMotivoAhorro);
        spinnerPlazo = findViewById(R.id.SpinnerEditPlazo);
        btnGuardar = findViewById(R.id.btnGuardarCambios);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Obtener datos del intent
        idMetaFin = getIntent().getStringExtra("id");
        coleccion = getIntent().getStringExtra("coleccion");

        // Configurar el spinner de plazo
        ArrayAdapter<CharSequence> adapterPlazo = ArrayAdapter.createFromResource(
                this,
                R.array.plazo, android.R.layout.simple_spinner_item
        );
        adapterPlazo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlazo.setAdapter(adapterPlazo);

        // Cargar datos desde Firestore
        db.collection(coleccion).document(idMetaFin).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                editCantidadObj.setText(doc.getString("cantidad_objetivo"));
                editMotivo.setText(doc.getString("motivo_ahorro"));

                String plazo = doc.getString("plazo");
                if (plazo != null) {
                    int index = adapterPlazo.getPosition(plazo);
                    if (index >= 0) spinnerPlazo.setSelection(index);
                }
            }
        });

        btnGuardar.setOnClickListener(v -> actualizarMetaFinanciera());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void actualizarMetaFinanciera() {
        String cantidad = editCantidadObj.getText().toString().trim();
        String motivo = editMotivo.getText().toString().trim();
        String plazo = spinnerPlazo.getSelectedItem().toString();

        if (cantidad.isEmpty() || motivo.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("cantidad_objetivo", cantidad);
        data.put("motivo_ahorro", motivo);
        data.put("plazo", plazo);

        db.collection(coleccion).document(idMetaFin)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Meta actualizada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreUpdate", "Error", e);
                });
    }
}
