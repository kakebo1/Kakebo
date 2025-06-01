package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
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

public class PlaneacionEgresos extends AppCompatActivity {

    private TransaccionAdapter transaccionAdapter;
    private final List<Transaccion> planEgresosItemList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_planeacion_egresos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerPlanEgresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transaccionAdapter = new TransaccionAdapter(planEgresosItemList);
        recyclerView.setAdapter(transaccionAdapter);

        db = FirebaseFirestore.getInstance();
        cargarPlanEgresos();

        ImageButton btnAddPlanEg = findViewById(R.id.btnAgregarI);
        btnAddPlanEg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPlanEg = new Intent(PlaneacionEgresos.this, LlenadoPlanEgresos.class);
                startActivity(addPlanEg);
            }
        });
    }

    private void cargarPlanEgresos(){
        db.collection("plan_egresos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            planEgresosItemList.clear();
            for(DocumentSnapshot doc : queryDocumentSnapshots){
                Transaccion planEg = doc.toObject(Transaccion.class);
                if(planEg != null){
                    planEgresosItemList.add(planEg);
                    transaccionAdapter.notifyItemInserted(planEgresosItemList.size());
                }
            }
        });
    }
}