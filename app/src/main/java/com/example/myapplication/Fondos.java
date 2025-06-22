package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Fondos extends AppCompatActivity {

    private TextView textViewTotalIngresos, resultadoAhorro, resultadoFondoEmergencia;
    private EditText porcentajeAhorro, porcentajeFondoEmergencia;
    private FirebaseFirestore db;
    private double totalPlanIngresos = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fondos); // Asegúrate que el nombre coincide con tu layout

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Referencias UI
        textViewTotalIngresos = findViewById(R.id.textViewTotalIngresos);
        resultadoAhorro = findViewById(R.id.resultadoAhorro);
        resultadoFondoEmergencia = findViewById(R.id.resultadoFondoEmergencia);
        porcentajeAhorro = findViewById(R.id.porcentajeAhorro);
        porcentajeFondoEmergencia = findViewById(R.id.porcentajeFondoEmergencia);
        Button btnCalcularAhorro = findViewById(R.id.button4);
        Button btnCalcularEmergencia = findViewById(R.id.button5);
        Button btnRegresar = findViewById(R.id.button3);

        // Calcular la suma total de plan_ingresos
        db.collection("plan_ingresos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            double suma = 0.0;
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Double cantidad = doc.getDouble("cantidad");
                if (cantidad != null) {
                    suma += cantidad;
                }
            }
            totalPlanIngresos = suma;
            textViewTotalIngresos.setText(String.format("%.2f", totalPlanIngresos));
        });

        // Botón calcular ahorro
        btnCalcularAhorro.setOnClickListener(v -> {
            String porcentajeTexto = porcentajeAhorro.getText().toString().trim();
            if (!porcentajeTexto.isEmpty()) {
                try {
                    double porcentaje = Double.parseDouble(porcentajeTexto);
                    double resultado = (totalPlanIngresos * porcentaje) / 100;
                    resultadoAhorro.setText(String.format("%.2f", resultado));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Ingrese un porcentaje válido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingrese un porcentaje para ahorro", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón calcular fondo de emergencia
        btnCalcularEmergencia.setOnClickListener(v -> {
            String porcentajeTexto = porcentajeFondoEmergencia.getText().toString().trim();
            if (!porcentajeTexto.isEmpty()) {
                try {
                    double porcentaje = Double.parseDouble(porcentajeTexto);
                    double resultado = (totalPlanIngresos * porcentaje) / 100;
                    resultadoFondoEmergencia.setText(String.format("%.2f", resultado));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Ingrese un porcentaje válido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingrese un porcentaje para emergencia", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón regresar
        btnRegresar.setOnClickListener(v -> {
            startActivity(new Intent(Fondos.this, PlaneacionIngresos.class));
            finish();
        });
    }
}