package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AgregarCategoria extends AppCompatActivity {
ImageButton btnAlcoholIco, btnTransporteIco, btnBosqueIco, btnBotiquinIco, btnCasaIco, btnCineIco, btnComidaIco, btnFamiliaIco,
    btnFiestaIco, btnGymIco, btnVideojuegosIco, btnLibroIco, btnMaquillajeIco, btnRopaIco, btnTarjetaIco, btnTelefonoIco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_agregar_categoria);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}