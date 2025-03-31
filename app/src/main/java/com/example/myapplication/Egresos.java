package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Egresos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_egresos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
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
                        Intent notas = new Intent(Egresos.this, Notas.class);
                        startActivity(notas);
                    }

                    if (adapterView.getItemAtPosition(i).equals("Planeación de deudas")) {
                        Intent deudas = new Intent(Egresos.this, PlaneacionDeudas.class);
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

                public void logOut(){
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(firebaseUser != null ){
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Egresos.this, InicioSesion.class));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
    }
}