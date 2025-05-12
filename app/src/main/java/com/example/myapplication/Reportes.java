package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Reportes extends AppCompatActivity {

    Button btnVer, btnDescargar, btnCompartir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_reportes);

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
    }

    private void generarPDFConFirebase() {
        Toast.makeText(this, "Cargando datos...", Toast.LENGTH_SHORT).show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String[]> datosEgresos = new ArrayList<>();
        List<String[]> datosPlanEgresos = new ArrayList<>();



        db.collection("egresos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String[]> datos = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String concepto = doc.getString("concepto");
                        String comentario = doc.getString("comentario");
                        String categoria = doc.getString("categoria");
                        String subcategoria = doc.getString("subcategoria");
                        Long cantidad = doc.getLong("cantidad");
                        String fecha = doc.getString("fecha");

                        String[] linea = {concepto, comentario, categoria, subcategoria, cantidad.toString(), fecha};
                        datos.add(linea);
                    }

                    generarPDFDesdeLista(datos);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al leer Firestore", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error al obtener documentos", e);
                });
    }

    private void generarPDFDesdeLista(List<String[]> datos) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int xStart = 50;
        int yStart = 60; // inicio del título
        int rowHeight = 40;
        int columnWidth = 90; // reducido para evitar sobreposición

        // --- TÍTULO ---
        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        canvas.drawText("Reporte de resultados", xStart, yStart, paint);

        // --- SUBTÍTULO (PERIODO) ---
        yStart += 30;
        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        canvas.drawText("Periodo del XX de XX al XX de 20XX", xStart, yStart, paint);

        // --- ESPACIO antes de tabla ---
        yStart += 40;

        // --- Encabezado tabla ---
        paint.setFakeBoldText(true);
        //String[] headers = {"Categoría Kakebo", "Cantidad", "Fecha"};
        String[] headers = {"Concepto", "Comentario", "Categoría", "Subcategoría", "Cantidad", "Fecha"};
        for (int i = 0; i < headers.length; i++) {
            canvas.drawText(headers[i], xStart + i * columnWidth, yStart, paint);
        }

        yStart += 10;
        canvas.drawLine(xStart, yStart, xStart + columnWidth * headers.length, yStart, paint);

        // --- Datos tabla ---
        paint.setTextSize(10);
        paint.setFakeBoldText(false);
        for (String[] row : datos) {
            yStart += rowHeight;
            if (yStart > 800) break;
            for (int i = 0; i < row.length; i++) {
                canvas.drawText(row[i], xStart + i * columnWidth, yStart, paint);
            }
            canvas.drawLine(xStart, yStart + rowHeight - 10, xStart + columnWidth * headers.length, yStart + rowHeight - 10, paint);
        }

        pdfDocument.finishPage(page);

        try {
            File file = new File(getExternalFilesDir(null), "reporte_egresos.pdf");
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
        File file = new File(getExternalFilesDir(null), "reporte_egresos.pdf");
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
        File sourceFile = new File(getExternalFilesDir(null), "reporte_egresos.pdf");
        if (!sourceFile.exists()) {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = "reporte_egresos_descarga.pdf";

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
                        out.write(buffer, 0, length);
                    }

                    in.close();
                    out.close();
                    Toast.makeText(this, "PDF guardado en Descargas.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se pudo guardar el archivo", Toast.LENGTH_SHORT).show();
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
        File file = new File(getExternalFilesDir(null), "reporte_egresos.pdf");
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
