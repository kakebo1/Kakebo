package com.example.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Planeaciones extends AppCompatActivity {

    Button btngotoplanDeudas;
    Button btngotoPlanIngresos;
    Button btngotoPlanEgresos;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_planeaciones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btngotoplanDeudas = findViewById(R.id.gotoPlanDeudas);
        btngotoPlanEgresos = findViewById(R.id.gotoPlanEg);
        btngotoPlanIngresos = findViewById(R.id.gotoPlanIng);
        btnRegresar = findViewById(R.id.PlaneacionesRegresar);

        btngotoPlanIngresos.setOnClickListener(v -> {
            Intent aux = new Intent(Planeaciones.this, PlaneacionIngresos.class);
            startActivity(aux);
        });
        btngotoPlanEgresos.setOnClickListener(v -> {
            Intent aux = new Intent(Planeaciones.this, PlaneacionEgresos.class);
            startActivity(aux);
        });
        btngotoplanDeudas.setOnClickListener(v -> {
            Intent aux = new Intent(Planeaciones.this, PlaneacionDeudas.class);
            startActivity(aux);
        });
        btnRegresar.setOnClickListener(v -> {
            Intent aux = new Intent(Planeaciones.this, MenuPrinc.class);
            startActivity(aux);
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
                    Intent inicio = new Intent(Planeaciones.this, MenuPrinc.class);
                    startActivity(inicio);
                }

                if (adapterView.getItemAtPosition(i).equals("Ingresos")) {
                    Intent ingresos = new Intent(Planeaciones.this, Ingresos.class);
                    startActivity(ingresos);
                }

                if (adapterView.getItemAtPosition(i).equals("Egresos")) {
                    Intent egresos = new Intent(Planeaciones.this, Egresos.class);
                    startActivity(egresos);
                }
                if (adapterView.getItemAtPosition(i).equals("Deudas")) {
                    Intent deudas = new Intent(Planeaciones.this, Deudas.class);
                    startActivity(deudas);
                }
                if (adapterView.getItemAtPosition(i).equals("Planeaciones")) {
                    Intent deudas = new Intent(Planeaciones.this, Planeaciones.class);
                    startActivity(deudas);
                }
                if (adapterView.getItemAtPosition(i).equals("Notas")) {
                    Intent notas = new Intent(Planeaciones.this, notasActivity.class);
                    startActivity(notas);
                }
                if (adapterView.getItemAtPosition(i).equals("Checkbox")) {
                    Intent checkbox = new Intent(Planeaciones.this, checkbox.class);
                    startActivity(checkbox);
                }
                if (adapterView.getItemAtPosition(i).equals("Categorias")) {
                    Intent categorias = new Intent(Planeaciones.this, Categorias.class);
                    startActivity(categorias);
                }

                if (adapterView.getItemAtPosition(i).equals("Reportes")) {
                    Intent reportes = new Intent(Planeaciones.this, Reportes.class);
                    startActivity(reportes);
                }
                if (adapterView.getItemAtPosition(i).equals("Meta financiera")) {
                    Intent metafin = new Intent(Planeaciones.this, MetaFin.class);
                    startActivity(metafin);
                }
                if (adapterView.getItemAtPosition(i).equals("Ayuda")) {
                    Intent ayuda = new Intent(Planeaciones.this, Ayuda.class);
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
                    startActivity(new Intent(Planeaciones.this, InicioSesion.class));
                    finish();
                }

            }
        });




    }
}