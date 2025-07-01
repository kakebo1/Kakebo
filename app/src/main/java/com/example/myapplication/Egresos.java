package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
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

public class Egresos extends AppCompatActivity {

    private TransaccionAdapter transaccionAdapter;
    private final List<Transaccion> egresoList = new ArrayList<>();
    private FirebaseFirestore db;
    private PieChart pieChart;
    private String tipo = "egresos";

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

        pieChart = findViewById(R.id.graficaEgresos);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(16f);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(true);

        // Visualizar registros
        RecyclerView recyclerView = findViewById(R.id.recyclerEgresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transaccionAdapter = new TransaccionAdapter(egresoList, this, "egresos");
        recyclerView.setAdapter(transaccionAdapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                transaccionAdapter.filtrar(newText);
                return true;
            }
        });

        Button btnReales = findViewById(R.id.btnEgReales);
        Button btnPlaneados = findViewById(R.id.btnEgPlaneados);

        db = FirebaseFirestore.getInstance();

        btnReales.setOnClickListener(v -> {
            cargarEgresos("egresos", transacciones -> {
                transaccionAdapter = new TransaccionAdapter(transacciones, this, "egresos");
                recyclerView.setAdapter(transaccionAdapter);
            actualizarEstiloBotones(btnReales, btnPlaneados);
            actualizarPieChart(transacciones);
            });
        });

        btnPlaneados.setOnClickListener(v -> {
            cargarEgresos("plan_egresos", transacciones -> {
                transaccionAdapter = new TransaccionAdapter(transacciones, this, "plan_egresos");
                recyclerView.setAdapter(transaccionAdapter);
                actualizarEstiloBotones(btnPlaneados, btnReales);
                actualizarPieChart(transacciones);
            });
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

                    if (adapterView.getItemAtPosition(i).equals("Deudas")) {
                        Intent deudas = new Intent(Egresos.this, Deudas.class);
                        startActivity(deudas);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Planeaciones")) {
                        Intent deudas = new Intent(Egresos.this, Planeaciones.class);
                        startActivity(deudas);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Notas")) {
                        Intent notas = new Intent(Egresos.this, notasActivity.class);
                        startActivity(notas);
                    }
                    if (adapterView.getItemAtPosition(i).equals("Checkbox")) {
                        Intent checkbox = new Intent(Egresos.this, checkbox.class);
                        startActivity(checkbox);
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

    private void actualizarEstiloBotones(Button selec, Button noSel){
        selec.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#72BB53")));

        selec.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        noSel.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gray));
        noSel.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private void cargarEgresos(String collection, OnDatosCargadosListener listener) {
        db.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("FirestoreDebug", "Documentos recibidos: " + queryDocumentSnapshots.size());
            //egresoList.clear(); //Limpiar para evitar duplicados
            List<Transaccion> lista = new ArrayList<>(); // Lista temporal para el callback

            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                try {
                    EgresoItem egreso = doc.toObject(EgresoItem.class);
                    if (egreso != null) {
                        egreso.setId(doc.getId());
                        lista.add(egreso); //Se agrega a lista temporal, no a egresoList
                        //transaccionAdapter.notifyItemInserted(egresoList.size() - 1); //Notificar por cada nuevo item
                        Log.d("FirestoreDebug", "Documento bruto: " + doc.getData());
                    }
                } catch (Exception e){
                    Log.e("FirestoreDebug", "Error al convertir documento: ", e);
                }
            }
            listener.onCarga(lista); // Envia la lista al callback
        }).addOnFailureListener(e ->
                Log.e("FirestoreDebug", "Error al obtener egresos", e)
        );
    }
}