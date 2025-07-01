package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class PlaneacionDeudas extends AppCompatActivity {

    private TransaccionAdapter deudaAdapter;
    private final List<Transaccion> planDeudaList = new ArrayList<>();
    private FirebaseFirestore db;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planeacion_deudas);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pieChart = findViewById(R.id.graficaPlanDeudas);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(16f);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerPlanDeudas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        cargarDeudasPlan("plan_deuda", transacciones -> {
            deudaAdapter = new TransaccionAdapter(transacciones, this, "plan_deuda");
            recyclerView.setAdapter(deudaAdapter);
            actualizarPieChart(transacciones);
        });

        ImageButton btnAgregarPagoDeuda = findViewById(R.id.btnAgregarPlanDeu);
        btnAgregarPagoDeuda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent addPagoDeu = new Intent(PlaneacionDeudas.this, LlenadoPlanDeudas.class);
                startActivity(addPagoDeu);
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
                    Intent inicio = new Intent(PlaneacionDeudas.this, MenuPrinc.class);
                    startActivity(inicio);
                }

                if (adapterView.getItemAtPosition(i).equals("Ingresos")) {
                    Intent ingresos = new Intent(PlaneacionDeudas.this, Ingresos.class);
                    startActivity(ingresos);
                }

                if (adapterView.getItemAtPosition(i).equals("Egresos")) {
                    Intent egresos = new Intent(PlaneacionDeudas.this, Egresos.class);
                    startActivity(egresos);
                }
                if (adapterView.getItemAtPosition(i).equals("Deudas")) {
                    Intent deudas = new Intent(PlaneacionDeudas.this, Deudas.class);
                    startActivity(deudas);
                }
                if (adapterView.getItemAtPosition(i).equals("Planeaciones")) {
                    Intent deudas = new Intent(PlaneacionDeudas.this, Planeaciones.class);
                    startActivity(deudas);
                }

                if (adapterView.getItemAtPosition(i).equals("Notas")) {
                    Intent notas = new Intent(PlaneacionDeudas.this, notasActivity.class);
                    startActivity(notas);
                }
                if (adapterView.getItemAtPosition(i).equals("Checkbox")) {
                    Intent checkbox = new Intent(PlaneacionDeudas.this, checkbox.class);
                    startActivity(checkbox);
                }

                if (adapterView.getItemAtPosition(i).equals("Categorias")) {
                    Intent categorias = new Intent(PlaneacionDeudas.this, Categorias.class);
                    startActivity(categorias);
                }

                if (adapterView.getItemAtPosition(i).equals("Reportes")) {
                    Intent reportes = new Intent(PlaneacionDeudas.this, Reportes.class);
                    startActivity(reportes);
                }
                if (adapterView.getItemAtPosition(i).equals("Meta financiera")) {
                    Intent metafin = new Intent(PlaneacionDeudas.this, MetaFin.class);
                    startActivity(metafin);
                }
                if (adapterView.getItemAtPosition(i).equals("Ayuda")) {
                    Intent ayuda = new Intent(PlaneacionDeudas.this, Ayuda.class);
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
                    startActivity(new Intent(PlaneacionDeudas.this, InicioSesion.class));
                    finish();
                }

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


    private void cargarDeudasPlan(String collection, PlaneacionDeudas.OnDatosCargadosListener listener) {
        db.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("FirestoreDebug", "Documentos recibidos: " + queryDocumentSnapshots.size());
          //  planDeudaList.clear(); //Limpiar para evitar duplicados
            //deudaAdapter.notifyDataSetChanged();
            List<Transaccion> lista = new ArrayList<>();

            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                try {
                    Transaccion deudaPlaneada = doc.toObject(Transaccion.class);
                    if (deudaPlaneada != null) {
                        deudaPlaneada.setId(doc.getId());
                        lista.add(deudaPlaneada);
                        //deudaAdapter.notifyItemInserted(planDeudaList.size() - 1); //Notificar por cada nuevo item
                        Log.d("FirestoreDebug", "Documento bruto: " + doc.getData());
                    }
                } catch (Exception e){
                    Log.e("FirestoreDebug", "Error al convertir documento: ", e);
                }
            }
            listener.onCarga(lista);
        }).addOnFailureListener(e -> Log.e("FirestoreDebug", "Error al obtener egresos", e));
    }
    }