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

    private TextView txtFecha, txtCategoria, txtSubcategoria, txtConcepto, txtCantidad, txtComentario, txtPlanPagos;
    private Button btnEditar, btnEliminar;
    private String id, coleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        txtFecha = findViewById(R.id.txtFecha);
        txtCategoria = findViewById(R.id.txtCategoria);
        txtSubcategoria = findViewById(R.id.txtSubcategoria);
        txtConcepto = findViewById(R.id.txtConcepto);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtComentario = findViewById(R.id.txtComentario);
        txtPlanPagos = findViewById(R.id.txtPlanPagos);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Obtener datos del intent
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        coleccion = intent.getStringExtra("coleccion");

        txtFecha.setText(intent.getStringExtra("fecha"));
        txtCategoria.setText(intent.getStringExtra("categoria"));
        txtSubcategoria.setText(intent.getStringExtra("subcategoria"));
        txtConcepto.setText(intent.getStringExtra("concepto"));
        txtCantidad.setText(String.valueOf(intent.getDoubleExtra("cantidad", 0)));
        txtComentario.setText(intent.getStringExtra("comentario"));
        txtPlanPagos.setText(intent.getStringExtra("plan_pagos"));

        btnEditar.setOnClickListener(v -> {
            Intent editarIntent = new Intent(this, editarRegistro.class);
            editarIntent.putExtras(intent); // Reutiliza los datos del item
            editarIntent.putExtra("coleccion", coleccion);
            editarIntent.putExtra("tipoTransaccion", coleccion);

            // Define el tipo de transacción según la colección
            if (coleccion.equals("ingresos") || coleccion.equals("plan_ingresos")) {
                editarIntent.putExtra("tipoTransaccion", "ingresos");
            } else if (coleccion.equals("egresos")) {
                editarIntent.putExtra("tipoTransaccion", "egresos");
            } else if (coleccion.equals("deudas")) {
                editarIntent.putExtra("tipoTransaccion", "deudas"); // Por si luego personalizas también
            }

            startActivity(editarIntent);
            finish();
        });


        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar registro")
                .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                .setPositiveButton("Sí", (dialog, which) -> eliminarRegistro())
                .setNegativeButton("No", null)
                .show();
    }

    private void eliminarRegistro() {
        FirebaseFirestore.getInstance().collection(coleccion)
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                );
    }
}
