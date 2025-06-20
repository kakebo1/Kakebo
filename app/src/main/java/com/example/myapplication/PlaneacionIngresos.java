package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaneacionIngresos extends AppCompatActivity {

    Button btnRegresar;
    private TransaccionAdapter transaccionAdapter;
    private final List<Transaccion> planIngresosItemList = new ArrayList<>();
    private FirebaseFirestore db;
    private PieChart pieChart;

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

        pieChart = findViewById(R.id.graficaPlanIngresos);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(16f);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerPlanIngresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        cargarPlanIngresos("plan_ingresos", transacciones -> {
            transaccionAdapter = new TransaccionAdapter(transacciones, this, "plan_ingresos");
            recyclerView.setAdapter(transaccionAdapter);
            actualizarPieChart(transacciones);
        });


        ImageButton btnAddPlanIng = findViewById(R.id.btnAgregarPlanIng);
        btnAddPlanIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPlanIng = new Intent(PlaneacionIngresos.this, LlenadoPlanIngresos.class);
                startActivity(addPlanIng);
            }
        });

        btnRegresar=findViewById(R.id.btnRegresar);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        btnRegresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
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

    private void cargarPlanIngresos(String collection, PlaneacionIngresos.OnDatosCargadosListener listener){
        db.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
           // planIngresosItemList.clear();
            List<Transaccion> lista = new ArrayList<>();

            for(DocumentSnapshot doc : queryDocumentSnapshots){
               try {
                   Transaccion planIng = doc.toObject(Transaccion.class);
                   if(planIng != null){
                       planIng.setId(doc.getId());
                       lista.add(planIng);
                       transaccionAdapter.notifyItemInserted(planIngresosItemList.size());
                   }
               } catch (Exception e){
                   Log.e("FirestoreDebug", "Error al obtener el documento", e);
               }
            }
            listener.onCarga(lista);
        }).addOnFailureListener(e -> Log.e("FirestoreDebug", "Error al obtener plan de ingresos", e));
    }
}