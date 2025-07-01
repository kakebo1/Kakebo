package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.editarMetaFinanciera;
import com.example.myapplication.editarRegistro;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class metafin_itemDetails extends AppCompatActivity {

    private TextView CantidadObj, Motivo, Plazo;
    private Button btnEditar, btnEliminar;
    private String id, coleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metafin_item_details);

        CantidadObj = findViewById(R.id.txtCantObj);
        Motivo = findViewById(R.id.txtMotivo);
        Plazo = findViewById(R.id.txtPlazo);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Obtener datos del intent
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        coleccion = intent.getStringExtra("coleccion");

        String cantidad = intent.getStringExtra("cantidad_objetivo");
        String motivo = intent.getStringExtra("motivo_ahorro");
        String plazo = intent.getStringExtra("plazo");

        CantidadObj.setText(cantidad != null ? cantidad : "Sin cantidad");
        Motivo.setText(motivo != null ? motivo : "Sin motivo");
        Plazo.setText(plazo != null ? plazo : "Sin plazo");

        btnEditar.setOnClickListener(v -> {
            Intent editarIntent = new Intent(this, editarMetaFinanciera.class);
            editarIntent.putExtras(intent); // Reutiliza los datos del item
            editarIntent.putExtra("coleccion", coleccion);
            editarIntent.putExtra("tipoTransaccion", coleccion);

            startActivity(editarIntent);
            setResult(RESULT_OK);
            finish();
        });

        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar meta financiera")
                .setMessage("¿Estás seguro de que deseas eliminar esta meta financiera?")
                .setPositiveButton("Sí", (dialog, which) -> eliminarRegistro())
                .setNegativeButton("No", null)
                .show();
    }

    private void eliminarRegistro() {
        FirebaseFirestore.getInstance().collection(coleccion)
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Meta Financiera eliminada", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                );
    }
}
