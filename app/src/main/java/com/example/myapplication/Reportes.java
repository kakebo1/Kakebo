package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Reportes extends AppCompatActivity {

    Button btnVer, btnDescargar, btnCompartir;
    Spinner reporte;
    private final String[] categoriasIngresos = {"Salario", "Beca", "Préstamo", "Aguinaldo", "Utilidades"};
    private final String[] categoriasEgresos = {"Supervivencia", "Cultural", "Ocio", "Otros"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_reportes);

        reporte = findViewById(R.id.reporte);
        btnVer = findViewById(R.id.btnVer);
        btnDescargar = findViewById(R.id.btnDescargar);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnVer.setEnabled(false);

        btnVer.setOnClickListener(v -> verPDF());
        btnDescargar.setOnClickListener(v -> descargarPDF());
        btnCompartir.setOnClickListener(v -> compartirPDF());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        generarPDFConFirebase();

        Spinner menuLateral=findViewById(R.id.menuLateral);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.menu,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        menuLateral.setAdapter(adapter);

        menuLateral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i).equals("Inicio")) {
                    Intent inicio = new Intent(Reportes.this, MenuPrinc.class);
                    startActivity(inicio);
                }

                if (adapterView.getItemAtPosition(i).equals("Ingresos")) {
                    Intent ingresos = new Intent(Reportes.this, Ingresos.class);
                    startActivity(ingresos);
                }

                if (adapterView.getItemAtPosition(i).equals("Egresos")) {
                    Intent egresos = new Intent(Reportes.this, Egresos.class);
                    startActivity(egresos);
                }

                if (adapterView.getItemAtPosition(i).equals("Deudas")) {
                    Intent deudas = new Intent(Reportes.this, Deudas.class);
                    startActivity(deudas);
                }

                if (adapterView.getItemAtPosition(i).equals("Planeaciones")) {
                    Intent deudas = new Intent(Reportes.this, Planeaciones.class);
                    startActivity(deudas);
                }

                if (adapterView.getItemAtPosition(i).equals("Notas")) {
                    Intent notas = new Intent(Reportes.this, notasActivity.class);
                    startActivity(notas);
                }
                if (adapterView.getItemAtPosition(i).equals("Checkbox")) {
                    Intent checkbox = new Intent(Reportes.this, checkbox.class);
                    startActivity(checkbox);
                }

                if (adapterView.getItemAtPosition(i).equals("Categorias")) {
                    Intent categorias = new Intent(Reportes.this, Categorias.class);
                    startActivity(categorias);
                }

                if (adapterView.getItemAtPosition(i).equals("Reportes")) {
                    Intent reportes = new Intent(Reportes.this, Reportes.class);
                    startActivity(reportes);
                }
                if (adapterView.getItemAtPosition(i).equals("Meta financiera")) {
                    Intent metafin = new Intent(Reportes.this, MetaFin.class);
                    startActivity(metafin);
                }

                if (adapterView.getItemAtPosition(i).equals("Ayuda")) {
                    Intent ayuda = new Intent(Reportes.this, Ayuda.class);
                    startActivity(ayuda);
                }

                if (adapterView.getItemAtPosition(i).equals("Cerrar sesión")) {
                    logOut();
                }
            }

            public void logOut(){
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null ){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Reportes.this, InicioSesion.class));
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner reporte=findViewById(R.id.reporte);
        ArrayAdapter<CharSequence> a=ArrayAdapter.createFromResource(this,R.array.reporte,android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_item);
        reporte.setAdapter(a);
        reporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

        private void generarPDFConFirebase() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Long> planIngresos = new HashMap<>();
            Map<String, Long> ingresos = new HashMap<>();
            Map<String, Long> planEgresos = new HashMap<>();
            Map<String, Long> egresos = new HashMap<>();
            Map<String, String> kakebo = new HashMap<>();

            Toast.makeText(this, "Generando PDF...", Toast.LENGTH_SHORT).show();

            db.collection("plan_ingresos").get().addOnSuccessListener(planIngresosDocs -> {
                for (QueryDocumentSnapshot doc : planIngresosDocs) {
                    String rawCategoria = doc.getString("categoria");
                    Long cantidad = doc.getLong("cantidad");
                    if (rawCategoria != null && cantidad != null) {
                        for (String cat : categoriasIngresos) {
                            if (cat.equalsIgnoreCase(rawCategoria.trim())) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    planIngresos.put(cat, planIngresos.getOrDefault(cat, 0L) + cantidad);
                                }
                                break;
                            }

                        }
                    }
                }

                db.collection("ingresos").get().addOnSuccessListener(ingresosDocs -> {
                    for (QueryDocumentSnapshot doc : ingresosDocs) {
                        String rawCategoria = doc.getString("categoria");
                        Long cantidad = doc.getLong("cantidad");
                        if (rawCategoria != null && cantidad != null) {
                            for (String cat : categoriasIngresos) {
                                if (cat.equalsIgnoreCase(rawCategoria.trim())) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        ingresos.put(cat, ingresos.getOrDefault(cat, 0L) + cantidad);
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    db.collection("plan_egresos").get().addOnSuccessListener(planEgresosDocs -> {
                        for (QueryDocumentSnapshot doc : planEgresosDocs) {
                            String rawCategoria = doc.getString("categoria");
                            Long cantidad = doc.getLong("cantidad");
                            if (rawCategoria != null && cantidad != null) {
                                for (String cat : categoriasEgresos) {
                                    if (cat.equalsIgnoreCase(rawCategoria.trim())) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            planEgresos.put(cat, planEgresos.getOrDefault(cat, 0L) + cantidad);
                                        }
                                        break;
                                    }

                                }
                            }
                        }

                        db.collection("egresos").get().addOnSuccessListener(egresosDocs -> {
                            for (QueryDocumentSnapshot doc : egresosDocs) {
                                String rawCategoria = doc.getString("categoria");
                                Long cantidad = doc.getLong("cantidad");
                                if (rawCategoria != null && cantidad != null) {
                                    for (String cat : categoriasEgresos) {
                                        if (cat.equalsIgnoreCase(rawCategoria.trim())) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                egresos.put(cat, egresos.getOrDefault(cat, 0L) + cantidad);
                                            }
                                            break;
                                        }
                                    }
                                }
                            }

                            generarPDF(planIngresos, ingresos, planEgresos, egresos, kakebo);
                        });
                    });
                });
            });
        }

        private void generarPDF(Map<String, Long> planIngresos, Map<String, Long> ingresos,
                Map<String, Long> planEgresos, Map<String, Long> egresos,
                Map<String, String> kakebo) {

            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 1120, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            int x = 40;
            int y = 50;
            int rowHeight = 25;

            paint.setTextSize(16);
            paint.setFakeBoldText(true);
            canvas.drawText("Reporte de movimientos financieros", x, y, paint);

            y += 30;
            paint.setTextSize(12);
            paint.setFakeBoldText(false);
            canvas.drawText("Periodo del 01 al 15 de junio de 2025", x, y, paint);

            // Tabla Ingresos
            //  y += 40;
            y += 40;
            x += 60;
            paint.setFakeBoldText(true);
            canvas.drawText("Ingresos", x, y, paint);

            //y += 20;
            y += 30;
            canvas.drawText("Categoría", x, y, paint);
            canvas.drawText("Esperado", x + 150, y, paint);
            canvas.drawText("Real", x + 280, y, paint);

            y += 10;
            canvas.drawLine(x, y, x + 400, y, paint);

            paint.setFakeBoldText(false);
            y += rowHeight;

            long totalEsperadoIngresos = 0;
            long totalRealIngresos = 0;

            for (String categoria : categoriasIngresos) {
                long esperado = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    esperado = planIngresos.getOrDefault(categoria, 0L);
                }
                long real = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    real = ingresos.getOrDefault(categoria, 0L);
                }
                canvas.drawText(categoria, x, y, paint);
                canvas.drawText(String.valueOf(esperado), x + 150, y, paint);
                canvas.drawText(String.valueOf(real), x + 280, y, paint);
                totalEsperadoIngresos += esperado;
                totalRealIngresos += real;
                y += rowHeight;
            }

            paint.setFakeBoldText(true);
            canvas.drawText("Total", x, y, paint);
            canvas.drawText(String.valueOf(totalEsperadoIngresos), x + 150, y, paint);
            canvas.drawText(String.valueOf(totalRealIngresos), x + 280, y, paint);

            // Tabla Egresos
            y += 40;
            paint.setFakeBoldText(true);
            canvas.drawText("Egresos", x, y, paint);

            // y += 20;
            y += 30;
            canvas.drawText("Categoría", x, y, paint);
            canvas.drawText("Esperado", x + 150, y, paint);
            canvas.drawText("Real", x + 280, y, paint);

            y += 10;
            canvas.drawLine(x, y, x + 400, y, paint);

            paint.setFakeBoldText(false);
            y += rowHeight;

            long totalEsperadoEgresos = 0;
            long totalRealEgresos = 0;

            for (String categoria : categoriasEgresos) {
                long esperado = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    esperado = planEgresos.getOrDefault(categoria, 0L);
                }
                long real = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    real = egresos.getOrDefault(categoria, 0L);
                }
                canvas.drawText(categoria, x, y, paint);
                canvas.drawText("$  "+ String.valueOf(esperado),  x + 150, y, paint);
                canvas.drawText("$  " +String.valueOf(real), x + 280, y, paint);
                totalEsperadoEgresos += esperado;
                totalRealEgresos += real;
                y += rowHeight;
            }

            paint.setFakeBoldText(true);
            canvas.drawText("Total", x, y, paint);
            canvas.drawText("$  " +String.valueOf(totalEsperadoEgresos), x + 150, y, paint);
            canvas.drawText("$  " +String.valueOf(totalRealEgresos), x + 280, y, paint);

            // ============================
            // GRAFICAS DE BARRAS
            // ============================
            y += 30;
            paint.setFakeBoldText(true);
            paint.setTextSize(14);
            canvas.drawText("Gráfica de Ingresos", x, y, paint);
            paint.setTextSize(12);

            //Mensajes de retroalimnetación sobre el desempeño
            y+=30; //Espacio antes de la gráfica
            paint.setFakeBoldText(true);
            paint.setTextSize(14);

            if(totalRealIngresos > totalEsperadoIngresos){
                paint.setColor(Color.BLACK);
                canvas.drawText("Tus ingresos reales han superado a los esperados. ¡Excelente!", x, y, paint);
                y += 25;
            }
            if(totalRealIngresos < totalEsperadoIngresos){
                paint.setColor(Color.RED);
                canvas.drawText("Tus ingresos reales han sido menores a los esperados. Triste situación.", x, y, paint);
                y += 25;
            }

            int barWidth = 30;
            int space = 20;
            int maxBarHeight = 100;

            long maxIngresos = Math.max(totalEsperadoIngresos, totalRealIngresos);

            int startX = x;
            int baseY = y + maxBarHeight + 20;

            for (String categoria : categoriasIngresos) {
                long esperado = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    esperado = planIngresos.getOrDefault(categoria, 0L);
                }
                long real = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    real = ingresos.getOrDefault(categoria, 0L);
                }

                int esperadoHeight = (int) ((esperado * maxBarHeight) / (float) maxIngresos);
                int realHeight = (int) ((real * maxBarHeight) / (float) maxIngresos);

                paint.setColor(0xFF2196F3); // Azul
                canvas.drawRect(startX, baseY - esperadoHeight, startX + barWidth, baseY, paint);

                paint.setColor(0xFF4CAF50); // Verde
                canvas.drawRect(startX + barWidth + 5, baseY - realHeight, startX + 2 * barWidth + 5, baseY, paint);

                paint.setColor(0xFF000000);
                canvas.drawText(categoria, startX, baseY + 15, paint);

                startX += 2 * barWidth + space;
            }

            // Gráfica Egresos
            y = baseY + 50;
            paint.setFakeBoldText(true);
            paint.setTextSize(14);
            canvas.drawText("Gráfica de Egresos", x, y, paint);
            paint.setTextSize(12);

            //Mensajes de retroalimnetación sobre el desempeño
            y+=20; //Espacio antes de la gráfica
            paint.setFakeBoldText(true);
            paint.setTextSize(14);

            if(totalRealEgresos > totalEsperadoEgresos){
                paint.setColor(Color.RED);
                canvas.drawText("Tus egresos reales han superado a los esperados.", x, y, paint);
                y += 20; // espacio entre líneas
                canvas.drawText("¡Cuidado! Te alejas de tu meta financiera.", x, y, paint);
                y += 25; // espacio final

            }
            if(totalRealEgresos < totalEsperadoEgresos){
                paint.setColor(Color.BLACK);
                canvas.drawText("Tus egresos reales han sido menores a los esperados. ¡Excelente! Sigue así.", x, y, paint);
                y += 25;
            }
            paint.setColor(Color.BLACK); //Restaurar color para lo siguiente.

            y += 20;
            maxBarHeight = 100;
            long maxEgresos = Math.max(totalEsperadoEgresos, totalRealEgresos);
            startX = x;
            baseY = y + maxBarHeight + 20;

            for (String categoria : categoriasEgresos) {
                long esperado = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    esperado = planEgresos.getOrDefault(categoria, 0L);
                }
                long real = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    real = egresos.getOrDefault(categoria, 0L);
                }

                int esperadoHeight = (int) ((esperado * maxBarHeight) / (float) maxEgresos);
                int realHeight = (int) ((real * maxBarHeight) / (float) maxEgresos);

                paint.setColor(0xFF2196F3); // Azul
                canvas.drawRect(startX, baseY - esperadoHeight, startX + barWidth, baseY, paint);

                paint.setColor(0xFF4CAF50); // Verde
                canvas.drawRect(startX + barWidth + 5, baseY - realHeight, startX + 2 * barWidth + 5, baseY, paint);

                paint.setColor(0xFF000000);
                canvas.drawText(categoria, startX, baseY + 15, paint);

                startX += 2 * barWidth + space;
            }
            y = baseY + 40;

            double fondoAhorro = totalEsperadoIngresos * 0.05;
            double fondoEmergencia = totalEsperadoIngresos * 0.05;
            DecimalFormat formato = new DecimalFormat("#.##");

            y += 50;
            paint.setFakeBoldText(true);
            paint.setColor(Color.BLUE);
            paint.setTextSize(14);
            canvas.drawText("Fondo de ahorro ideal (5%): $" + formato.format(fondoAhorro), x, y, paint);
            y += 20;
            canvas.drawText("Fondo de emergencia (5%): $" + formato.format(fondoEmergencia), x, y, paint);

            pdfDocument.finishPage(page);

            try {
                File file = new File(getExternalFilesDir(null), "reporte_financiero.pdf");
                FileOutputStream outputStream = new FileOutputStream(file);
                pdfDocument.writeTo(outputStream);
                outputStream.close();
                Toast.makeText(this, "PDF generado exitosamente.", Toast.LENGTH_SHORT).show();
                btnVer.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al guardar PDF.", Toast.LENGTH_SHORT).show();
            }

            pdfDocument.close();
        }


        private void verPDF() {
            File file = new File(getExternalFilesDir(null), "reporte_financiero.pdf");
            if (file.exists()) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } else {
                Toast.makeText(this, "PDF no encontrado.", Toast.LENGTH_SHORT).show();
            }
        }

        private void descargarPDF() {
            File sourceFile = new File(getExternalFilesDir(null), "reporte_financiero.pdf");
            if (!sourceFile.exists()) {
                Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            String fileName = "reporte_financiero.pdf";

            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    if (uri != null) {
                        OutputStream out = getContentResolver().openOutputStream(uri);
                        FileInputStream in = new FileInputStream(sourceFile);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            assert out != null;
                            out.write(buffer, 0, length);
                        }

                        in.close();
                        assert out != null;
                        out.close();
                        Toast.makeText(this, "PDF guardado en Descargas.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    File destFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                    FileInputStream in = new FileInputStream(sourceFile);
                    FileOutputStream out = new FileOutputStream(destFile);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }

                    in.close();
                    out.close();
                    Toast.makeText(this, "PDF guardado en Descargas.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al guardar el PDF.", Toast.LENGTH_SHORT).show();
            }
        }

        private void compartirPDF() {
            File file = new File(getExternalFilesDir(null), "reporte_financiero.pdf");
            if (!file.exists()) {
                Toast.makeText(this, "PDF no encontrado.", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Compartir PDF"));
        }
    }






