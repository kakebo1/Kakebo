package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class editarRegistro extends AppCompatActivity {
/*
        private EditText editFecha, editConcepto, editCantidad, editComentario, editPlanPagos;
        private Spinner spinnerCategoria, spinnerSubcategoria;
        private Button btnGuardar, btnEliminar;
        private FirebaseFirestore db;
        private String transaccionId;

        private final Map<String, String[]> subcategoriasPorCategoria = new HashMap<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_registro);
            db = FirebaseFirestore.getInstance();

            // Obtener ID del documento y tipo de colección desde el intent
            transaccionId = getIntent().getStringExtra("id");
            String coleccion = getIntent().getStringExtra("coleccion");

            editFecha = findViewById(R.id.editFecha);
            editConcepto = findViewById(R.id.editConcepto);
            editCantidad = findViewById(R.id.editCantidad);
            editComentario = findViewById(R.id.editComentario);
            editPlanPagos = findViewById(R.id.editPlanPagos);
            spinnerCategoria = findViewById(R.id.spinnerCategoria);
            spinnerSubcategoria = findViewById(R.id.spinnerSubcategoria);
            btnGuardar = findViewById(R.id.btnGuardarCambios);
            btnEliminar = findViewById(R.id.btnEliminar);

            setupDatePicker();
            setupCategorias();

            // Cargar datos desde Firestore
            assert coleccion != null;
            db.collection(coleccion).document(transaccionId).get().addOnSuccessListener(doc -> {
                if (doc.exists()) {
                    Transaccion transaccion = doc.toObject(Transaccion.class);
                    if (transaccion != null) {
                        cargarDatos(transaccion);
                    }
                }
            });

            btnGuardar.setOnClickListener(v -> {
                actualizarTransaccion(coleccion);
            });

            btnEliminar.setOnClickListener(v -> {
                eliminarTransaccion(coleccion);
            });
        }

        private void setupDatePicker() {
            editFecha.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    String fecha = dayOfMonth + "/" + (month + 1) + "/" + year;
                    editFecha.setText(fecha);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            });
        }

        private void setupCategorias() {
            // Define categorías y subcategorías aquí
            subcategoriasPorCategoria.put("Supervivencia", new String[]{"Alimentación", "Servicios del Hogar", "Transporte"});
            subcategoriasPorCategoria.put("Ocio", new String[]{"Entretenimiento", "Viajes", "Restaurantes"});
            subcategoriasPorCategoria.put("Cultural", new String[]{"Libros", "Cursos", "Museos"});
            subcategoriasPorCategoria.put("Otro", new String[]{"Donaciones", "Otros gastos"});

            ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(subcategoriasPorCategoria.keySet()));
            adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(adapterCategoria);

            spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                    actualizarSubcategorias(categoriaSeleccionada);
                }

                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        private void actualizarSubcategorias(String categoria) {
            String[] subcats = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                subcats = subcategoriasPorCategoria.getOrDefault(categoria, new String[]{});
            }
            assert subcats != null;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subcats);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubcategoria.setAdapter(adapter);
        }

        private void cargarDatos(Transaccion t) {
            editFecha.setText(t.getFecha());
            editConcepto.setText(t.getConcepto());
            editCantidad.setText(String.valueOf(t.getCantidad()));
            editComentario.setText(t.getComentario());
            editPlanPagos.setText(t.getPlan_pagos());

            if (t.getCategoria() != null) {
                int index = new ArrayList<>(subcategoriasPorCategoria.keySet()).indexOf(t.getCategoria());
                if (index >= 0) {
                    spinnerCategoria.setSelection(index);
                    actualizarSubcategorias(t.getCategoria());

                    if (t.getSubcategoria() != null) {
                        String[] subcats = subcategoriasPorCategoria.get(t.getCategoria());
                        if (subcats != null) {
                            for (int i = 0; i < subcats.length; i++) {
                                if (subcats[i].equals(t.getSubcategoria())) {
                                    spinnerSubcategoria.setSelection(i);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        private void actualizarTransaccion(String coleccion) {
            String fecha = editFecha.getText().toString();
            String categoria = spinnerCategoria.getSelectedItem().toString();
            String subcategoria = spinnerSubcategoria.getSelectedItem().toString();
            String concepto = editConcepto.getText().toString();
            double cantidad = Double.parseDouble(editCantidad.getText().toString());
            String comentario = editComentario.getText().toString();
            String planPagos = editPlanPagos.getText().toString();

            Map<String, Object> data = new HashMap<>();
            data.put("fecha", fecha);
            data.put("categoria", categoria);
            data.put("subcategoria", subcategoria);
            data.put("concepto", concepto);
            data.put("cantidad", cantidad);
            data.put("comentario", comentario);
            data.put("plan_pagos", planPagos);

            db.collection(coleccion).document(transaccionId)
                    .update(data)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Transacción actualizada", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show());
        }

        private void eliminarTransaccion(String coleccion) {
            db.collection(coleccion).document(transaccionId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Transacción eliminada", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show());
        }
   */
}
