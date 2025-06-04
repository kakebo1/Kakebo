package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class Egresos extends AppCompatActivity {

    private EgresosAdapter egresoAdapter;
    private final List<EgresoItem> egresoList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_egresos);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
        });

        //Visualizar registros
        RecyclerView recyclerView = findViewById(R.id.recyclerEgresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        egresoAdapter = new EgresosAdapter(egresoList);
        recyclerView.setAdapter(egresoAdapter);

        db = FirebaseFirestore.getInstance();
        cargarEgresos("egresos");

        Button btnReales = findViewById(R.id.btnEgReales);
        Button btnPlaneados = findViewById(R.id.btnEgPlaneados);
        Button btnDeudas = findViewById(R.id.btnDeudas);

        btnReales.setOnClickListener(v -> {
            cargarEgresos("egresos");
            actualizarEstiloBotones(btnReales, btnPlaneados, btnDeudas);
        });
        btnPlaneados.setOnClickListener(v -> {
            cargarEgresos("plan_egresos");
            actualizarEstiloBotones(btnPlaneados, btnReales, btnDeudas);
        });
        btnDeudas.setOnClickListener(v -> {
            cargarEgresos("plan_deuda");
            actualizarEstiloBotones(btnDeudas, btnPlaneados, btnReales);
        });

        ImageButton btnAgregarEg = findViewById(R.id.btnAgregar);
        btnAgregarEg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent addEgReal = new Intent(Egresos.this, LlenadoEgresos.class);
                startActivity(addEgReal);
                }
            });

            //Menú Lateral
            Spinner menuLateral = findViewById(R.id.menuLateral);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.menu, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            menuLateral.setAdapter(adapter);
            menuLateral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getItemAtPosition(i).equals("Inicio")) {
                        Intent inicio = new Intent(Egresos.this, MenuPrinc.class);
                        startActivity(inicio);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Ingresos")) {
                        Intent ingresos = new Intent(Egresos.this, Ingresos.class);
                        startActivity(ingresos);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Egresos")) {
                        Intent egresos = new Intent(Egresos.this, Egresos.class);
                        startActivity(egresos);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Notas")) {
                        Intent notas = new Intent(Egresos.this, notasActivity.class);
                        startActivity(notas);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Checkbox")) {
                        Intent checkbox = new Intent(Egresos.this, checkbox.class);
                        startActivity(checkbox);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Deudas")) {
                        Intent deudas = new Intent(Egresos.this, Deudas.class);
                        startActivity(deudas);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Categorias")) {
                        Intent categorias = new Intent(Egresos.this, Categorias.class);
                        startActivity(categorias);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Reportes")) {
                        Intent reportes = new Intent(Egresos.this, Reportes.class);
                        startActivity(reportes);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Meta financiera")) {
                        Intent metafin = new Intent(Egresos.this, MetaFin.class);
                        startActivity(metafin);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Ayuda")) {
                        Intent ayuda = new Intent(Egresos.this, Ayuda.class);
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
                        startActivity(new Intent(Egresos.this, InicioSesion.class));
                        finish();
                    }

                }
            });

    }

    private void actualizarEstiloBotones(Button selec, Button noSel, Button noSel2){
        selec.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.teal_200));
        selec.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        noSel.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gray));
        noSel.setTextColor(ContextCompat.getColor(this, R.color.black));
        noSel2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        noSel2.setTextColor(ContextCompat.getColor(this, android.R.color.black));
    }

    private void cargarEgresos(String collection) {
        db.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("FirestoreDebug", "Documentos recibidos: " + queryDocumentSnapshots.size());
            egresoList.clear(); //Limpiar para evitar duplicados
            egresoAdapter.notifyDataSetChanged();

            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                try {
                    EgresoItem egreso = doc.toObject(EgresoItem.class);
                    if (egreso != null) {
                        egresoList.add(egreso);
                        egresoAdapter.notifyItemInserted(egresoList.size() - 1); //Notificar por cada nuevo item
                        Log.d("FirestoreDebug", "Documento bruto: " + doc.getData());
                    }
                } catch (Exception e){
                    Log.e("FirestoreDebug", "Error al convertir documento: ", e);
                }
            }
        }).addOnFailureListener(e -> Log.e("FirestoreDebug", "Error al obtener egresos", e));
    }
}