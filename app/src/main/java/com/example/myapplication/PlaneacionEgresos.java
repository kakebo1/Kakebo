package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class PlaneacionEgresos extends AppCompatActivity {

    private TransaccionAdapter transaccionAdapter;
    private final List<Transaccion> planEgresosItemList = new ArrayList<>();
    private FirebaseFirestore db;
    private Button btnAceptar;

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

        btnAceptar = findViewById(R.id.btnAceptarPlan);
        btnAceptar.setVisibility(View.INVISIBLE);

            //Menú Lateral
            Spinner menuLateral = findViewById(R.id.menuLateral);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.menu, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            menuLateral.setAdapter(adapter);
            menuLateral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getItemAtPosition(i).equals("Inicio")) {
                        Intent inicio = new Intent(PlaneacionEgresos.this, MenuPrinc.class);
                        startActivity(inicio);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Ingresos")) {
                        Intent ingresos = new Intent(PlaneacionEgresos.this, Ingresos.class);
                        startActivity(ingresos);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Egresos")) {
                        Intent egresos = new Intent(PlaneacionEgresos.this, Egresos.class);
                        startActivity(egresos);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Deudas")) {
                        Intent deudas = new Intent(PlaneacionEgresos.this, Deudas.class);
                        startActivity(deudas);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Planeaciones")) {
                        Intent deudas = new Intent(PlaneacionEgresos.this, Planeaciones.class);
                        startActivity(deudas);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Notas")) {
                        Intent notas = new Intent(PlaneacionEgresos.this, notasActivity.class);
                        startActivity(notas);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Checkbox")) {
                        Intent checkbox = new Intent(PlaneacionEgresos.this, checkbox.class);
                        startActivity(checkbox);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Categorias")) {
                        Intent categorias = new Intent(PlaneacionEgresos.this, Categorias.class);
                        startActivity(categorias);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Reportes")) {
                        Intent reportes = new Intent(PlaneacionEgresos.this, Reportes.class);
                        startActivity(reportes);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Meta financiera")) {
                        Intent metafin = new Intent(PlaneacionEgresos.this, MetaFin.class);
                        startActivity(metafin);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Ayuda")) {
                        Intent ayuda = new Intent(PlaneacionEgresos.this, Ayuda.class);
                        startActivity(ayuda);
                    }

                    if (adapterView.getItemAtPosition(i).equals ("Cerrar sesión")){
                        logOut();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //No hacer nada
                }

                public void logOut(){
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(firebaseUser != null ){
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(PlaneacionEgresos.this, InicioSesion.class));
                        finish();
                    }

                }
            });

        RecyclerView recyclerView = findViewById(R.id.recyclerPlanEgresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transaccionAdapter = new TransaccionAdapter(planEgresosItemList, this, "plan_egresos");
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
                    planEg.setId(doc.getId());
                    planEgresosItemList.add(planEg);
                    transaccionAdapter.notifyItemInserted(planEgresosItemList.size() - 1);
                }
            }
        }).addOnFailureListener(e -> Log.e("FirestoreDebug", "Error al obtener egresos", e));
    }
}