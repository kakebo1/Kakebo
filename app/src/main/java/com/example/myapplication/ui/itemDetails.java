package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.editarRegistro;
import com.google.firebase.firestore.FirebaseFirestore;

public class itemDetails extends AppCompatActivity {
    TextView txtFecha, txtCategoria, txtSubcategoria, txtConcepto, txtCantidad, txtComentario, txtPlanPagos;
    Button btnEditar, btnEliminar;
    String docId;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        txtFecha = findViewById(R.id.txtFecha);
        txtCategoria = findViewById(R.id.txtCategoria);
        txtSubcategoria = findViewById(R.id.txtSubcategoria);
        txtConcepto = findViewById(R.id.txtConcepto);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtComentario = findViewById(R.id.txtComentario);
        txtPlanPagos = findViewById(R.id.txtPlanPagos);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Recibe los datos
        Intent intent = getIntent();
        docId = intent.getStringExtra("docId");
        txtFecha.setText(intent.getStringExtra("fecha"));
        txtCategoria.setText(intent.getStringExtra("categoria"));
        txtSubcategoria.setText(intent.getStringExtra("subcategoria"));
        txtConcepto.setText(intent.getStringExtra("concepto"));
        txtCantidad.setText(String.valueOf(intent.getDoubleExtra("cantidad", 0.0)));
        txtComentario.setText(intent.getStringExtra("comentario"));
        txtPlanPagos.setText(intent.getStringExtra("plan_pagos"));

        // Botón editar
        btnEditar.setOnClickListener(v -> {
            // Puedes enviar los datos a otra Activity para edición o abrir un diálogo
            Intent editIntent = new Intent(itemDetails.this, editarRegistro.class);
            editIntent.putExtra("docId", docId);
            editIntent.putExtra("fecha", txtFecha.getText().toString());
            editIntent.putExtra("categoria", txtCategoria.getText().toString());
            editIntent.putExtra("subcategoria", txtSubcategoria.getText().toString());
            editIntent.putExtra("concepto", txtConcepto.getText().toString());
            editIntent.putExtra("cantidad", Double.parseDouble(txtCantidad.getText().toString()));
            editIntent.putExtra("comentario", txtComentario.getText().toString());
            editIntent.putExtra("plan_pagos", txtPlanPagos.getText().toString());
            startActivity(editIntent);
        });

        // Botón eliminar
        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Registro")
                    .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        db.collection("ingresos").document(docId)
                                .delete()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
                                    finish(); // Cierra la pantalla
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Error al eliminar registro", Toast.LENGTH_SHORT).show()
                                );
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }
}
