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
//DECLARAR EL EDIT TEXT
    EditText txtMetaCant, txtRazon;
    Button btnAceptar;
    private FirebaseFirestore basededatos;
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
        Spinner plazo = findViewById(R.id.plazoMeta);
        ArrayAdapter<CharSequence>ad = ArrayAdapter.createFromResource(this, R.array.plazo, simple_spinner_item);
        ad.setDropDownViewResource(simple_spinner_item);
        plazo.setAdapter(ad);
        boolean b = true;
        plazo.setSelected(b);
     //   int a;
        plazo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int a, long l) {
      //          public void onItemSelected(int a) {
                    if (plazo.getItemAtPosition(a).equals("Corto (menor a 6 meses)")) {
                      //SI SE SELECCIONA EL PLAZO CORTO EL VALOR DENTRO DE LA BASE DE DATOS SERÁ CORTO
                        plazoMe = "Corto";
                    }
                    if (plazo.getItemAtPosition(a).equals("Mediano (6 meses a 1 año)")) {
                        //SI SE SELECCIONA EL PLAZO MEDIANO EL VALOR DENTRO DE LA BASE DE DATOS SERÁ MEDIANO
                        plazoMe = "Mediano";
                    }
                    if (plazo.getItemAtPosition(a).equals("Largo (mayor a 1 año)")){
                        //SI SE SELECCIONA EL PLAZO  LARGO EL VALOR DENTRO DE LA BASE DE DATOS SERÁ LARGO
                        plazoMe = "Largo";
                    }
                }
           // }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
        // FIN SPINNER PLAZO

        /** MENU LATERAL **/
        Spinner menuLateral = findViewById(R.id.menuLateral);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.menu, simple_spinner_item);
        adapter.setDropDownViewResource(simple_spinner_item);
        menuLateral.setAdapter(adapter);
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
        txtMetaCant= (EditText)findViewById(R.id.txtMetaCant);
        txtRazon = (EditText)findViewById(R.id.txtRazon);
        btnAceptar =findViewById(R.id.btnAceptar);
    }
    //     ESTE SE VA A PONER EN EL EVENTO ON CLICK DEL BOTÓN DE ACEPTAR ****
public void aceptar (View v){

        if (validar()) {
            Toast.makeText(getApplicationContext(),"Datos guardados correctamente", Toast.LENGTH_SHORT).show();

         Intent btnAceptar = new Intent(MetaFin.this, MenuPrinc.class);
                        startActivity(btnAceptar);}
}
// ****
    //PARA CHECAR QUE LOS EDIT TEXT NO SE ENCUENTREN VACIOS
    public boolean validar(){
        boolean retorno =true;
        String MetaCant = txtMetaCant.getText().toString();
        String cantidad_objetivo = txtMetaCant.getText().toString().trim();
        String Razon = txtRazon.getText().toString();
        String motivo_ahorro = txtRazon.getText().toString().trim();
        String plazoM;
        plazoM = plazoMe;
        if(MetaCant.isEmpty()) {
            txtMetaCant.setError("Este campo NO puede quedar vacío");
            retorno = false;
                } else if (Razon.isEmpty()) {
                txtRazon.setError("Este campo NO puede quedar vacío");
                retorno = false;
            } else {

                meta_financiera(motivo_ahorro, cantidad_objetivo, plazoM);


            }
        return retorno;
    }
private void meta_financiera (String motivo_ahorro, String cantidad_objetivo, String plazoM){
Map <String, Object> map = new HashMap<>();
map.put("cantidad_objetivo",cantidad_objetivo ); //ESTE ES STRING
map.put("motivo_ahorro", motivo_ahorro);
map.put("plazo",plazoM);
basededatos.collection("metaFinanciera").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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