package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuPrinc extends AppCompatActivity {

    ImageButton btnAyuda, btnMetaFin, btnIngresos, btnNotas, btnEgresos, btnCategorias, btnReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_menu_princ);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
           Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnMetaFin=(android.widget.ImageButton)findViewById(R.id.btnMetaFin);
        btnMetaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnMetaFin = new Intent(MenuPrinc.this, MetaFin.class);
                startActivity(btnMetaFin);
            }
        });

        btnIngresos=(android.widget.ImageButton)findViewById(R.id.btnIngresos);
        btnIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnIngresos = new Intent(MenuPrinc.this, Ingresos.class);
                startActivity(btnIngresos);
            }
        });

        btnNotas=(android.widget.ImageButton)findViewById(R.id.btnNotas);
        btnNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnNotas = new Intent(MenuPrinc.this, notasActivity.class);
                startActivity(btnNotas);
            }
        });

        btnEgresos=(android.widget.ImageButton)findViewById(R.id.btnEgresos);
        btnEgresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnEgresos = new Intent(MenuPrinc.this, Egresos.class);
                startActivity(btnEgresos);
            }
        });

        btnCategorias=(android.widget.ImageButton)findViewById(R.id.btnCategorias);
        btnCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnCategorias = new Intent(MenuPrinc.this, Planeaciones.class);
                startActivity(btnCategorias);
            }
        });

        btnReportes=(android.widget.ImageButton)findViewById(R.id.btnReportes);
        btnReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnReportes = new Intent(MenuPrinc.this, Reportes.class);
                startActivity(btnReportes);
            }
        });

        btnAyuda =(android.widget.ImageButton)findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent btnAyuda = new Intent(MenuPrinc.this, Ayuda.class);
                startActivity(btnAyuda);
            }
        });

    }



}