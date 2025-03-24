package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LlenadoIngresos extends AppCompatActivity {
    //DECLARAR COMPONENTES
Button btnAceptarIng;
ImageButton btnAgregarImgIng;
EditText txtConceptoIng, txtCantidadIng, txtComentarioIng;
    private FirebaseFirestore basededatos;

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
        //INICIALIZAR COMPONENTES
        btnAgregarImgIng = findViewById(R.id.btnAgregarImgIng);
        txtConceptoIng = findViewById(R.id.txtConceptoIng);
        txtCantidadIng = findViewById(R.id.txtCantidadIng);
        txtComentarioIng = findViewById(R.id.txtComentarioIng);

    }
public void aceptar1(View v){
    if (validar1()) {
        Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LlenadoIngresos.this, MenuPrinc.class);
        startActivity(intent);
    }
}
public boolean validar1(){
    boolean retorno = true;
    String concepto = txtConceptoIng.getText().toString().trim();
    String cantidad = txtCantidadIng.getText().toString().trim();
    String comentario = txtComentarioIng.getText().toString().trim();

    if (concepto.isEmpty()) {
        txtConceptoIng.setError("Este campo NO puede quedar vacío");
        retorno = false;
    }
    if (cantidad.isEmpty()){
        txtCantidadIng.setError("Este campo NO puede quedar vacío");
    }
    if (retorno) {
        try {
            int cantida = Integer.parseInt(cantidad);
            ingresos(concepto, cantida, comentario);
        } catch (NumberFormatException e) {
            txtCantidadIng.setError("Introduce un número válido");
            retorno = false;
        }
}
    return retorno;
}
    private void ingresos(String concepto, int cantida, String comentario) {

        Map<String, Object> mapii = new HashMap<>();
        mapii.put("concepto", concepto);
        mapii.put("cantidad", cantida);
        mapii.put ("comentario", comentario);
        basededatos.collection("ingresos").add(mapii)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(LlenadoIngresos.this, "Pago guardado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LlenadoIngresos.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}



