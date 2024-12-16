package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Ayuda extends AppCompatActivity {
    //DECLARAR BOTONES
Button btnAvisoPriv, btnGuiaDeUso, btnTyC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_ayuda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /** MENU LATERAL **/
        Spinner menuLateral=findViewById(R.id.menuLateral);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.menu,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        menuLateral.setAdapter(adapter);
        /** FIN MENU LATERAL **/
        menuLateral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            /** METODO PARA  SELECCIONAR ALGÚN ELEMENTO DEL MENÚ **/
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE INICIO **/
                if (adapterView.getItemAtPosition(i).equals ("Inicio")){
                    Intent inicio = new Intent(Ayuda.this, MenuPrinc.class);
                    startActivity(inicio);
                }  /** FIN CÓDIGO BOTÓN INICIO MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INGRESOS SE PASA A LA PANTALLA DE INGRESOS **/
                if (adapterView.getItemAtPosition(i).equals ("Ingresos")){
                    Intent ingresos = new Intent(Ayuda.this, Ingresos.class);
                    startActivity(ingresos);
                }  /** FIN CÓDIGO BOTÓN INGRESOS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE EGRESOS **/
                if (adapterView.getItemAtPosition(i).equals ("Egresos")){
                    Intent egresos = new Intent(Ayuda.this, Egresos.class);
                    startActivity(egresos);
                }  /** FIN CÓDIGO BOTÓN EGRESOS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE NOTAS **/
                if (adapterView.getItemAtPosition(i).equals ("Notas")){
                    Intent notas = new Intent(Ayuda.this, Notas.class);
                    startActivity(notas);
                }  /** FIN CÓDIGO BOTÓN NOTAS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE PLANEACIÓN DE DEUDAS **/
                if (adapterView.getItemAtPosition(i).equals ("Planeación de deudas")){
                    Intent deudas = new Intent(Ayuda.this, PlaneacionDeudas.class);
                    startActivity(deudas);
                }  /** FIN CÓDIGO BOTÓN PLANEACION DE DEUDAS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE CATEGORIAS **/
                if (adapterView.getItemAtPosition(i).equals ("Categorias")){
                    Intent categorias = new Intent(Ayuda.this, Categorias.class);
                    startActivity(categorias);
                }  /** FIN CÓDIGO BOTÓN CATEGORIAS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE REPORTES **/
                if (adapterView.getItemAtPosition(i).equals ("Reportes")){
                    Intent reportes = new Intent(Ayuda.this, Reportes.class);
                    startActivity(reportes);
                }  /** FIN CÓDIGO BOTÓN REPORTES MENÚ   **/
                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE META FINANCIERA **/
                if (adapterView.getItemAtPosition(i).equals ("Meta financiera")){
                    Intent metafin = new Intent(Ayuda.this, MetaFin.class);
                    startActivity(metafin);
                }  /** FIN CÓDIGO BOTÓN META FINANCIERA MENÚ   **/
                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE AYUDA **/
                if (adapterView.getItemAtPosition(i).equals ("Ayuda")){
                    Intent ayuda = new Intent(Ayuda.this, Ayuda.class);
                    startActivity(ayuda);
                }  /** FIN CÓDIGO BOTÓN INICIO MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO SE CAMBIA EL COLOR DE LAS PANTALLAS **/
                // if (adapterView.getItemAtPosition(i).equals ("Inicio")){
                //   Intent inicio = new Intent(Ingresos.this, MenuPrinc.class);
                // startActivity(inicio);
                // }  /** FIN CÓDIGO BOTÓN INICIO MENÚ   **/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //BOTON AVISO PRIVACIDAD
        btnAvisoPriv=findViewById(R.id.btnAvisoPriv);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        btnAvisoPriv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Aviso de privacidad");
                builder.setMessage(getString(R.string.AvisodePriv));
                builder.setCancelable(false);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                           //     finish();

                            }

                        });
               builder.show();
            }

        }); // FIIIIN

        //BOTON TYC
        btnTyC=findViewById(R.id.btnTyC);
        AlertDialog.Builder builder2;
        builder2 = new AlertDialog.Builder(this);
        btnTyC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Términos y Condiciones");
                builder.setMessage(getString(R.string.TerminosyCond));
                builder.setCancelable(false);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //     finish();

                    }

                });
                builder.show();
            }

        }); // FIIIIN
        };


}