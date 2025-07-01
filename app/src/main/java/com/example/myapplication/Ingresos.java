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
import android.widget.Spinner;
import android.widget.Toast;

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

import androidx.appcompat.widget.SearchView;

public class Ingresos extends AppCompatActivity {

    private TransaccionAdapter transaccionAdapter;
    private final List<Transaccion> ingresoItemList = new ArrayList<>();
    private FirebaseFirestore db;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_ingresos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        pieChart = findViewById(R.id.graficaIngresos);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(16f);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerIngresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transaccionAdapter = new TransaccionAdapter(ingresoItemList, this, "ingresos");
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

        Button btnReales = findViewById(R.id.btnIngReales);
        Button btnPlaneados = findViewById(R.id.btnIngPlaneados);

        db = FirebaseFirestore.getInstance();

        btnReales.setOnClickListener(v -> {
            cargarIngresos("ingresos", transacciones ->{
                transaccionAdapter = new TransaccionAdapter(transacciones, this, "ingresos");
                recyclerView.setAdapter(transaccionAdapter);
                actualizarEstiloBotones(btnReales, btnPlaneados);
                actualizarPieChart(transacciones);
            });
        });

        btnPlaneados.setOnClickListener(v -> {
            cargarIngresos("plan_ingresos", transacciones -> {
                transaccionAdapter = new TransaccionAdapter(transacciones, this, "plan_ingresos");
                recyclerView.setAdapter(transaccionAdapter);
                actualizarEstiloBotones(btnPlaneados, btnReales);
                actualizarPieChart(transacciones);
            });
        });

        ImageButton btnAgregarIng = findViewById(R.id.btnAgregar);
        btnAgregarIng.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent addIngReal = new Intent(Ingresos.this, LlenadoIngresos.class);
                startActivity(addIngReal);
            }
        });


        //Menú Lateral
        Spinner menuLateral=findViewById(R.id.menuLateral);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.menu,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        menuLateral.setAdapter(adapter);
        menuLateral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getItemAtPosition(i).equals ("Inicio")){
                Intent inicio = new Intent(Ingresos.this, MenuPrinc.class);
            startActivity(inicio);
            }

            if (adapterView.getItemAtPosition(i).equals ("Ingresos")){
                Intent ingresos = new Intent(Ingresos.this, Ingresos.class);
                startActivity(ingresos);
            }

            if (adapterView.getItemAtPosition(i).equals ("Egresos")){
                Intent egresos = new Intent(Ingresos.this, Egresos.class);
                startActivity(egresos);
            }

                if (adapterView.getItemAtPosition(i).equals ("Deudas")){
                    Intent deudas = new Intent(Ingresos.this, Deudas.class);
                    startActivity(deudas);
                }

                if (adapterView.getItemAtPosition(i).equals ("Planeaciones")){
                    Intent deudas = new Intent(Ingresos.this, Planeaciones.class);
                    startActivity(deudas);
                }

                if (adapterView.getItemAtPosition(i).equals ("Notas")){
                Intent notas = new Intent(Ingresos.this, notasActivity.class);
                startActivity(notas);
            }
            if (adapterView.getItemAtPosition(i).equals("Checkbox")) {
                    Intent checkbox = new Intent(Ingresos.this, checkbox.class);
                    startActivity(checkbox);
                }

            if (adapterView.getItemAtPosition(i).equals ("Categorias")){
                Intent categorias = new Intent(Ingresos.this, Categorias.class);
                startActivity(categorias);
            }

            if (adapterView.getItemAtPosition(i).equals ("Reportes")){
                Intent reportes = new Intent(Ingresos.this, Reportes.class);
                startActivity(reportes);
            }
            if (adapterView.getItemAtPosition(i).equals ("Meta financiera")){
                Intent metafin = new Intent(Ingresos.this, MetaFin.class);
                startActivity(metafin);
            }
            if (adapterView.getItemAtPosition(i).equals ("Ayuda")){
                Intent ayuda = new Intent(Ingresos.this, Ayuda.class);
                startActivity(ayuda);
            }

            if (adapterView.getItemAtPosition(i).equals ("Cerrar sesión")){
                logOut();
            }
        }

        public void logOut(){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if(firebaseUser != null ){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Ingresos.this, InicioSesion.class));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

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

    private void cargarIngresos(String collection, Ingresos.OnDatosCargadosListener listener){
        db.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("FirestoreDebug", "Datos recibidos: " + queryDocumentSnapshots.size());
          //  ingresoItemList.clear(); // Limpiar lista antes de cargar nuevos datos
            //transaccionAdapter.notifyDataSetChanged();
            List<Transaccion> lista = new ArrayList<>();

            for (DocumentSnapshot doc : queryDocumentSnapshots){
                try {
                    Transaccion ingreso = doc.toObject(Transaccion.class);
                    if (ingreso != null) {
                        ingreso.setId(doc.getId());
                        lista.add(ingreso);
                        //transaccionAdapter.notifyItemInserted(ingresoItemList.size() - 1);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error al obtener la información", Toast.LENGTH_SHORT).show();
                }
            }
            listener.onCarga(lista);
        }).addOnFailureListener(e ->
                Log.e("FirestoreDebug", "Error al obtener la información", e)
        );
    }

}