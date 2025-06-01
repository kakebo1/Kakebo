package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Notas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner menuLateral=findViewById(R.id.menuLateral);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.menu,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        menuLateral.setAdapter(adapter);
        menuLateral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i).equals ("Inicio")){
                    Intent inicio = new Intent(Notas.this, MenuPrinc.class);
                    startActivity(inicio);
                }
                if (adapterView.getItemAtPosition(i).equals ("Ingresos")){
                    Intent ingresos = new Intent(Notas.this, Ingresos.class);
                    startActivity(ingresos);
                }
                if (adapterView.getItemAtPosition(i).equals ("Egresos")){
                    Intent egresos = new Intent(Notas.this, Egresos.class);
                    startActivity(egresos);
                }
                if (adapterView.getItemAtPosition(i).equals ("Notas")){
                    Intent notas = new Intent(Notas.this, Notas.class);
                    startActivity(notas);
                }

                if (adapterView.getItemAtPosition(i).equals ("Deudas")){
                    Intent deudas = new Intent(Notas.this, Deudas.class);
                    startActivity(deudas);
                }
                if (adapterView.getItemAtPosition(i).equals ("Categorias")){
                    Intent categorias = new Intent(Notas.this, Categorias.class);
                    startActivity(categorias);
                }
                if (adapterView.getItemAtPosition(i).equals ("Reportes")){
                    Intent reportes = new Intent(Notas.this, Reportes.class);
                    startActivity(reportes);
                }
                if (adapterView.getItemAtPosition(i).equals ("Meta financiera")){
                    Intent metafin = new Intent(Notas.this, MetaFin.class);
                    startActivity(metafin);
                }
                if (adapterView.getItemAtPosition(i).equals ("Ayuda")){
                    Intent ayuda = new Intent(Notas.this, Ayuda.class);
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
                startActivity(new Intent(Notas.this, InicioSesion.class));
            }
        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}