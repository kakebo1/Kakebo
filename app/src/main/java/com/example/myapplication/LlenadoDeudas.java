package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LlenadoDeudas extends AppCompatActivity {
    //DECLARAR LOS COMPONENTES
Button btnAceptar;
EditText txtConceptoa, txtCantidad, txtFecha;
private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_llenado_deudas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mfirestore = FirebaseFirestore.getInstance();
        btnAceptar = findViewById(R.id.btnAceptar);
        txtConceptoa = findViewById(R.id.txtConceptoa);
        txtCantidad = findViewById(R.id.txtCantidad);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Conceptodeuda = txtConceptoa.getText().toString().trim();
                Integer Cantidad = txtCantidad.getText().length();


                if(Conceptodeuda.isEmpty()){

                }else{
                    postDeuda(Conceptodeuda);

                }

            }
        });
    }

    private void postDeuda(String conceptodeuda) {
        Map<String, Object> map = new HashMap<>();
        map.put("concepto", conceptodeuda);
     //   map.put("cantidad", cantidad);

        mfirestore.collection("deudas").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}