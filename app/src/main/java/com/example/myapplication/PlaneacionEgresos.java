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