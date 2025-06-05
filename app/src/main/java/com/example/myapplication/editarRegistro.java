package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.ui.itemDetails;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class editarRegistro extends AppCompatActivity {

        private EditText editFecha, editConcepto, editCantidad, editComentario;
        private Spinner spinnerCategoria, spinnerSubcategoria, editPlanPagos;
        private Button btnGuardar, btnCancelar;
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
            String tipoTransaccion = getIntent().getStringExtra("tipoTransaccion");
            assert tipoTransaccion != null;


            editFecha = findViewById(R.id.editFecha);
            editConcepto = findViewById(R.id.editConcepto);
            editCantidad = findViewById(R.id.editCantidad);
            editComentario = findViewById(R.id.editComentario);
            editPlanPagos = findViewById(R.id.spinnerPlanPagos);
            spinnerCategoria = findViewById(R.id.spinnerCategoria);
            spinnerSubcategoria = findViewById(R.id.spinnerSubcategoria);
            btnGuardar = findViewById(R.id.btnGuardarCambios);
            btnCancelar = findViewById(R.id.btnCancelar);

            setupCategorias(tipoTransaccion);
            setupDatePicker();
            //cargarDatos(transaccionId, coleccion);

            assert coleccion != null;
            if (!coleccion.equals("pago_deuda") && !coleccion.equals("plan_deuda")) {
                editPlanPagos.setVisibility(View.GONE);
            }
            if (coleccion.equals("ingresos")) {
                spinnerSubcategoria.setVisibility(View.GONE);
            }


            // Cargar datos desde Firestore
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

            btnCancelar.setOnClickListener(v -> {
                finish();
            });

            ArrayAdapter<CharSequence> adapterPlanPagos = ArrayAdapter.createFromResource(this, R.array.plazospagodeuda, android.R.layout.simple_spinner_item);
            adapterPlanPagos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editPlanPagos.setAdapter(adapterPlanPagos);
            Log.d("editarRegistro", "ID: " + transaccionId + " | Colección: " + coleccion);

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

    private void setupCategorias(String tipoTransaccion) {
        if (tipoTransaccion.equals("ingresos") || tipoTransaccion.equals("plan_ingresos")) {
            // Categorías fijas para ingresos
            String[] categoriasIngresos = {"Salario", "Préstamo", "Utilidades", "Aguinaldo"};
            ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasIngresos);
            adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(adapterCategoria);

            // Oculta el spinner de subcategoría
            spinnerSubcategoria.setVisibility(View.GONE);

        } else {
            // Categorías y subcategorías para egresos
            subcategoriasPorCategoria.put("Supervivencia", new String[]{"Alimentación", "Servicios del Hogar", "Transporte"});
            subcategoriasPorCategoria.put("Ocio", new String[]{"Entretenimiento", "Viajes", "Restaurantes"});
            subcategoriasPorCategoria.put("Cultural", new String[]{"Libros", "Cursos", "Museos"});
            subcategoriasPorCategoria.put("Otro", new String[]{"Donaciones", "Otros gastos"});

            ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(subcategoriasPorCategoria.keySet()));
            adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(adapterCategoria);

            spinnerSubcategoria.setVisibility(View.VISIBLE);

            spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                    actualizarSubcategorias(categoriaSeleccionada);
                }

                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
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

        if (spinnerSubcategoria.getVisibility() == View.VISIBLE && t.getSubcategoria() != null) {
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

        // Solo manejar plan_pagos si no es nulo (aplica para deudas)
        if (t.getPlan_pagos() != null) {
            String[] opcionesPlanPagos = getResources().getStringArray(R.array.plazospagodeuda);
            for (int i = 0; i < opcionesPlanPagos.length; i++) {
                if (opcionesPlanPagos[i].equals(t.getPlan_pagos())) {
                    editPlanPagos.setSelection(i);
                    break;
                }
            }
        } else {
            // Ocultar el spinner si no aplica
            editPlanPagos.setVisibility(View.GONE);
            findViewById(R.id.spinnerPlanPagos).setVisibility(View.GONE);
        }

        // Selección de categoría y subcategoría
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
        String fecha = editFecha.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String concepto = editConcepto.getText().toString().trim();
        String cantidadStr = editCantidad.getText().toString().trim();
        String comentario = editComentario.getText().toString().trim();
        String subcategoria = "";

        if (spinnerSubcategoria.getVisibility() == View.VISIBLE && spinnerSubcategoria.getSelectedItem() != null) {
            subcategoria = spinnerSubcategoria.getSelectedItem().toString();
        }

        if (fecha.isEmpty() || concepto.isEmpty() || cantidadStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("fecha", fecha);
        data.put("categoria", categoria);
        data.put("subcategoria", subcategoria);
        data.put("concepto", concepto);
        data.put("cantidad", cantidad);
        data.put("comentario", comentario);

        if (!coleccion.equals("ingresos") && !coleccion.equals("plan_ingresos")) {
            data.put("subcategoria", subcategoria);
        }

        // Solo agregar el campo plan_pagos si aplica
        if (coleccion.equals("pago_deuda") || coleccion.equals("plan_deuda")) {
            String planPagos = editPlanPagos.getSelectedItem().toString();
            data.put("plan_pagos", planPagos);
        }

        db.collection(coleccion).document(transaccionId)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Transacción actualizada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreUpdate", "Error al actualizar", e);
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                });
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
}
