package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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

public class PlaneacionIngresos extends AppCompatActivity {

    Button btnAceptar;
    private TransaccionAdapter transaccionAdapter;
    private final List<Transaccion> planIngresosItemList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_plan_ingresos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerPlanIngresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transaccionAdapter = new TransaccionAdapter(planIngresosItemList);
        recyclerView.setAdapter(transaccionAdapter);

        db = FirebaseFirestore.getInstance();
        cargarPlanIngresos();

        ImageButton btnAddPlanIng = findViewById(R.id.btnAgregarPlanIng);
        btnAddPlanIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPlanIng = new Intent(PlaneacionIngresos.this, LlenadoPlanIngresos.class);
                startActivity(addPlanIng);
            }
        });

        btnAceptar=findViewById(R.id.btnAceptar);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("NO SE HA AGREGADO NINGÚN REGISTRO");
                builder.setMessage("Se debe de agregar por lo menos un registro planeado");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //     finish();
                    }
                });
                builder.show();
            }
        });
    }

    private void cargarPlanIngresos(){
        db.collection("plan_ingresos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            planIngresosItemList.clear();
            for(DocumentSnapshot doc : queryDocumentSnapshots){
                Transaccion planIng = doc.toObject(Transaccion.class);
                if(planIng != null){
                    planIngresosItemList.add(planIng);
                    transaccionAdapter.notifyItemInserted(planIngresosItemList.size());
                }
            }
        });
    }
}