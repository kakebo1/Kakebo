package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class historialMetasFinancieras extends AppCompatActivity {

    private MetaFinancieraAdapter metaFinancieraAdapter;
    private final List<MetaFinancieraModel> metaFinancieraList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_metas_financieras);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerMetaFinanciera);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        metaFinancieraAdapter = new MetaFinancieraAdapter(metaFinancieraList, this, "metaFinanciera");
        recyclerView.setAdapter(metaFinancieraAdapter);
        cargarMetaFinanciera("metaFinanciera");

        Button regresarDeHistorialMetasF = findViewById(R.id.btnRegresarDeHistorialMF);
        regresarDeHistorialMetasF.setOnClickListener(v -> {
            Intent aux = new Intent(historialMetasFinancieras.this, MetaFin.class);
            startActivity(aux);
            finish();
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK) {
            // Se editó una meta, regcargamos el historial
            cargarMetaFinanciera("metaFinanciera");
        }
    }

    private void cargarMetaFinanciera(String collection) {
        db.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("FirestoreDebug", "Documentos recibidos: " + queryDocumentSnapshots.size());
            metaFinancieraList.clear(); //Limpiar para evitar duplicados
            metaFinancieraAdapter.notifyDataSetChanged();

            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                try {
                    MetaFinancieraModel metaF = doc.toObject(MetaFinancieraModel.class);
                    if (metaF != null) {
                        metaF.setId(doc.getId());
                        metaFinancieraList.add(metaF);
                        metaFinancieraAdapter.notifyItemInserted(metaFinancieraList.size() - 1); //Notificar por cada nuevo item
                        Log.d("FirestoreDebug", "Documento bruto: " + doc.getData());
                    }
                } catch (Exception e){
                    Log.e("FirestoreDebug", "Error al convertir documento: ", e);
                }
            }
        }).addOnFailureListener(e -> Log.e("FirestoreDebug", "Error al obtener egresos", e));
    }
}
