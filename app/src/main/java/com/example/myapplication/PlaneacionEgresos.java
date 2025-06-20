package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaneacionEgresos extends AppCompatActivity {

    private TransaccionAdapter transaccionAdapter;
    private final List<Transaccion> planEgresosItemList = new ArrayList<>();
    private FirebaseFirestore db;
    private Button btnAceptar;
    private PieChart pieChart;

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

        pieChart = findViewById(R.id.graficaPlanEgresos);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(16f);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(true);

        btnAceptar = findViewById(R.id.btnAceptarPlan);
        btnAceptar.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = findViewById(R.id.recyclerPlanEgresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        cargarPlanEgresos("plan_egresos", transacciones -> {
            transaccionAdapter = new TransaccionAdapter(transacciones, this, "plan_egresos");
            recyclerView.setAdapter(transaccionAdapter);
            actualizarPieChart(transacciones);
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


        ImageButton btnAddPlanEg = findViewById(R.id.btnAgregarI);
        btnAddPlanEg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPlanEg = new Intent(PlaneacionEgresos.this, LlenadoPlanEgresos.class);
                startActivity(addPlanEg);
            }
        });
    }

    public interface OnDatosCargadosListener {
        void onCarga(List<Transaccion> transacciones);
    }


    private void actualizarPieChart(List<Transaccion> transacciones){
        Map<String, Float> totalPorCategoria = new HashMap<>();

        for(Transaccion t : transacciones){
            String categoria = t.getCategoria();
            float cantidad = (float) t.getCantidad();

            totalPorCategoria.put(categoria, totalPorCategoria.getOrDefault(categoria, 0f) + cantidad);
        }

        List<PieEntry> entries = new ArrayList<>();
        for(Map.Entry<String, Float> entry : totalPorCategoria.entrySet()){
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(0f);
        pieData.setValueTextColor(Color.TRANSPARENT);

        pieChart.setData(pieData);
        pieChart.invalidate(); // Redibuja
    }

    private void cargarPlanEgresos(String collection, PlaneacionEgresos.OnDatosCargadosListener listener){
        db.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
           // planEgresosItemList.clear();
            List<Transaccion> lista = new ArrayList<>();

            for(DocumentSnapshot doc : queryDocumentSnapshots){
                try {
                    Transaccion planEg = doc.toObject(Transaccion.class);
                    if(planEg != null){
                        planEg.setId(doc.getId());
                        lista.add(planEg);
                       // transaccionAdapter.notifyItemInserted(planEgresosItemList.size() - 1);
                    }
                } catch (Exception e){
                    Log.e("FirestoreDebug", "Error al convertir el documento: ", e);
                }
            }
            listener.onCarga(lista);
        }).addOnFailureListener(e -> Log.e("FirestoreDebug", "Error al obtener egresos", e));
    }
}