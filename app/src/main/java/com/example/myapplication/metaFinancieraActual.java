package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import static android.R.layout.*;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class metaFinancieraActual extends AppCompatActivity {
    TextView tvMetaCant, tvRazon, tvPlazo;
    private FirebaseFirestore basededatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_financiera_actual);

        tvMetaCant = findViewById(R.id.tvMetaCant);
        tvRazon = findViewById(R.id.tvRazon);
        tvPlazo = findViewById(R.id.tvPlazo);

        basededatos = FirebaseFirestore.getInstance();

        cargarUltimaMetaFinanciera();

        Button btnRegresarMetaFActual = findViewById(R.id.btnRegresarDeMFActual);
        btnRegresarMetaFActual.setOnClickListener(v -> {
            finish();
        });
    }

    private void cargarUltimaMetaFinanciera() {
        basededatos.collection("metaFinanciera")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        String cantidad = doc.getString("cantidad_objetivo");
                        String motivo = doc.getString("motivo_ahorro");
                        String plazo = doc.getString("plazo");

                        tvMetaCant.setText(cantidad != null ? cantidad : "Sin dato");
                        tvRazon.setText(motivo != null ? motivo : "Sin motivo");
                        tvPlazo.setText(plazo != null ? plazo : "Sin plazo");
                    } else {
                        Toast.makeText(this, "No hay metas guardadas aún.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MetaFinDetalle", "Error Firestore: ", e);
                    Toast.makeText(this, "Error al obtener datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}