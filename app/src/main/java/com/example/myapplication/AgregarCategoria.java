package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AgregarCategoria extends AppCompatActivity {
    ImageButton btnAlcoholIco, btnTransporteIco, btnBosqueIco, btnBotiquinIco, btnCasaIco, btnCineIco, btnComidaIco, btnFamiliaIco,
    btnFiestaIco, btnGymIco, btnVideojuegosIco, btnLibroIco, btnMaquillajeIco, btnRopaIco, btnTarjetaIco, btnTelefonoIco;
    private int selectedColor = Color.BLACK; // Color inicial
    private ImageButton selectedIcon = null; // Para rastrear el icono seleccionado

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.act_agregar_categoria);

            RecyclerView recyclerColores = findViewById(R.id.elegirColores);
            recyclerColores.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            // Lista de colores predefinidos
            ColorAdapter adapter = getColorAdapter();

            recyclerColores.setAdapter(adapter);

            // Asignar evento de selección a cada botón de icono
            setIconClickListener(R.id.btnAlcoholIco);
            setIconClickListener(R.id.btnTransporteIco);
            setIconClickListener(R.id.btnBosqueIco);
            setIconClickListener(R.id.btnBotiquinIco);
            setIconClickListener(R.id.btnCasaIco);
            setIconClickListener(R.id.btnCineIco);
            setIconClickListener(R.id.btnComidaIco);
            setIconClickListener(R.id.btnFamiliaIco);
            setIconClickListener(R.id.btnFiestaIco);
            setIconClickListener(R.id.btnGymIco);
            setIconClickListener(R.id.btnVideojuegosIco);
            setIconClickListener(R.id.btnLibroIco);
            setIconClickListener(R.id.btnMaquillajeIco);
            setIconClickListener(R.id.btnRopaIco);
            setIconClickListener(R.id.btnTarjetaIco);
            setIconClickListener(R.id.btnTelefonoIco);
        }

    private @NonNull ColorAdapter getColorAdapter() {
        int[] colores = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};

        return new ColorAdapter(colores, new ColorAdapter.OnColorClickListener() {
            @Override
            public void onColorSelected(int color) {
                selectedColor = color;
                if (selectedIcon != null) {
                    selectedIcon.setColorFilter(selectedColor);
                }
            }
        });
    }

    private void setIconClickListener(int iconId) {
            ImageButton iconButton = findViewById(iconId);
            iconButton.setOnClickListener(v -> {
                selectedIcon = (ImageButton) v;
                selectedIcon.setColorFilter(selectedColor); // Aplicar el color actual
            });
        }
    }



