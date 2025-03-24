package com.example.myapplication;

import static android.R.layout.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MetaFin extends AppCompatActivity {
    //DECLARAR EDIT TEXTS Y BOTÓN
    EditText txtMetaCant, txtRazon;
    Button btnAceptar;
    // SE DECLARÓ LA BASE DE DATOS
    private FirebaseFirestore basededatos;
    //VARIABLE DE LA CUAL SE VA A SACAR EL PLAZO DE META
    String plazoMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_meta_fin);
        basededatos = FirebaseFirestore.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /** SPINNER PLAZO **/
        // SE TOMAN LOS DATOS DEL ARRAY (DENTRO DEL ARCHIVO ARRAYS)

        Spinner plazo = findViewById(R.id.plazoMeta);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.plazo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plazo.setAdapter(adapter);

// Establecer la primera opción por defecto
        plazo.setSelection(0, false);

        plazo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Evitar que la opción "Seleccione una opción" sea válida
                if (position == 0) {
                    plazoMe = ""; // No se guarda ningún valor
                } else {
                    plazoMe = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada aquí
            }
        });

        // FIN SPINNER PLAZO

        /** MENU LATERAL **/
        Spinner menuLateral = findViewById(R.id.menuLateral);
        ArrayAdapter<CharSequence> adapteri = ArrayAdapter.createFromResource(this, R.array.menu, simple_spinner_item);
        adapter.setDropDownViewResource(simple_spinner_item);
        menuLateral.setAdapter(adapteri);
        /** FIN MENU LATERAL **/
        menuLateral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            /** METODO PARA  SELECCIONAR ALGÚN ELEMENTO DEL MENÚ **/
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE INICIO **/
                if (adapterView.getItemAtPosition(i).equals("Inicio")) {
                    Intent inicio = new Intent(MetaFin.this, MenuPrinc.class);
                    startActivity(inicio);
                }  /** FIN CÓDIGO BOTÓN INICIO MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INGRESOS SE PASA A LA PANTALLA DE INGRESOS **/
                if (adapterView.getItemAtPosition(i).equals("Ingresos")) {
                    Intent ingresos = new Intent(MetaFin.this, Ingresos.class);
                    startActivity(ingresos);
                }  /** FIN CÓDIGO BOTÓN INGRESOS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE EGRESOS **/
                if (adapterView.getItemAtPosition(i).equals("Egresos")) {
                    Intent egresos = new Intent(MetaFin.this, Egresos.class);
                    startActivity(egresos);
                }  /** FIN CÓDIGO BOTÓN EGRESOS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE NOTAS **/
                if (adapterView.getItemAtPosition(i).equals("Notas")) {
                    Intent notas = new Intent(MetaFin.this, Notas.class);
                    startActivity(notas);
                }  /** FIN CÓDIGO BOTÓN NOTAS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE PLANEACIÓN DE DEUDAS **/
                if (adapterView.getItemAtPosition(i).equals("Planeación de deudas")) {
                    Intent deudas = new Intent(MetaFin.this, PlaneacionDeudas.class);
                    startActivity(deudas);
                }  /** FIN CÓDIGO BOTÓN PLANEACION DE DEUDAS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE CATEGORIAS **/
                if (adapterView.getItemAtPosition(i).equals("Categorias")) {
                    Intent categorias = new Intent(MetaFin.this, Categorias.class);
                    startActivity(categorias);
                }  /** FIN CÓDIGO BOTÓN CATEGORIAS MENÚ   **/

                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE REPORTES **/
                if (adapterView.getItemAtPosition(i).equals("Reportes")) {
                    Intent reportes = new Intent(MetaFin.this, Reportes.class);
                    startActivity(reportes);
                }  /** FIN CÓDIGO BOTÓN REPORTES MENÚ   **/
                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE META FINANCIERA **/
                if (adapterView.getItemAtPosition(i).equals("Meta financiera")) {
                    Intent metafin = new Intent(MetaFin.this, MetaFin.class);
                    startActivity(metafin);
                }  /** FIN CÓDIGO BOTÓN META FINANCIERA MENÚ   **/
                /** SI SE SELECCIONA EL TEXTO INICIO SE PASA A LA PANTALLA DE AYUDA **/
                if (adapterView.getItemAtPosition(i).equals("Ayuda")) {
                    Intent ayuda = new Intent(MetaFin.this, Ayuda.class);
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
        //INICIALIZAR COMPONENTES
        txtMetaCant= (EditText)findViewById(R.id.txtMetaCant);
        txtRazon = (EditText)findViewById(R.id.txtRazon);
        btnAceptar =findViewById(R.id.btnAceptarPlan);
    }
    //     ESTE SE VA A PONER EN EL EVENTO ON CLICK DEL BOTÓN DE ACEPTAR ****
    public void aceptar(View v) {
        if (validar()) {
            int cantidad_objetivo = Integer.parseInt(txtMetaCant.getText().toString().trim());
            String motivo_ahorro = txtRazon.getText().toString().trim();
            meta_financiera(motivo_ahorro, cantidad_objetivo, plazoMe);
            Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MetaFin.this, MenuPrinc.class));
        }
    }

    //PARA CHECAR QUE LOS EDIT TEXT NO SE ENCUENTREN VACIOS
    public boolean validar() {
        boolean retorno = true;

        String MetaCant = txtMetaCant.getText().toString().trim();
        String Razon = txtRazon.getText().toString().trim();

        if (MetaCant.isEmpty()) {
            txtMetaCant.setError("Este campo NO puede quedar vacío");
            retorno = false;
        } else {
            try {
                int cantidad_objetivo = Integer.parseInt(MetaCant);
                if (cantidad_objetivo <= 0) {
                    txtMetaCant.setError("Ingrese un número válido mayor a 0");
                    retorno = false;
                }
            } catch (NumberFormatException e) {
                txtMetaCant.setError("Ingrese un número válido");
                retorno = false;
            }
        }

        if (Razon.isEmpty()) {
            txtRazon.setError("Este campo NO puede quedar vacío");
            retorno = false;
        }
        if (plazoMe == null || plazoMe.isEmpty()) {
            Toast.makeText(this, "Seleccione un plazo válido", Toast.LENGTH_SHORT).show();
            retorno = false;
        }
        return retorno;
    }

    //
    private void meta_financiera (String motivo_ahorro, int cantidad_objetivo, String plazoM){
        Map <String, Object> map = new HashMap<>();
// SE MANDAN ESTOS CAMPOS AL FIREBASE
        map.put("cantidad_objetivo",cantidad_objetivo ); //ESTE ES STRING
        map.put("motivo_ahorro", motivo_ahorro);
        map.put("plazo",plazoM);
        basededatos.collection("meta_financiera").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    //
}