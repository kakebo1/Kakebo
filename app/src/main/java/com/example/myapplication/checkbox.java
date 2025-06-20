package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.checkerframework.checker.units.qual.A;
import java.util.ArrayList;

public class checkbox extends AppCompatActivity {

    ListView listViewData;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems;
    EditText newItemInput;
    Button addButton, deleteButton, updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);

        listViewData = findViewById(R.id.listView_data);
        newItemInput = findViewById(R.id.newItemInput);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Inicializa lista dinámica y el adaptador
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listItems);
        listViewData.setAdapter(adapter);

        //Añadir un elemento a la lista cuando se hace clic en el botón de agregar
        addButton.setOnClickListener(view -> {
            String newItem = newItemInput.getText().toString().trim();
            if(!newItem.isEmpty()){
                listItems.add(newItem); // Añadir el nuevo elemento
                adapter.notifyDataSetChanged(); //Actualizar el adaptador
                newItemInput.setText(""); // Limpiar el campo de entrada
            } else{
                Toast.makeText(this, "Por favor, ingresa un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        // Eliminar elementos seleccionados al hacer clic en el botón de eliminar
        deleteButton.setOnClickListener(view -> {
            for(int i = listViewData.getCount() - 1; i >= 0; i--){
                if(listViewData.isItemChecked(i)){
                    listItems.remove(i); // Eliminar el elemento de la lista
                }
            }
            adapter.notifyDataSetChanged(); // Actualizar el adaptador
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent aux = new Intent(checkbox.this, notasActivity.class);
            startActivity(aux);
            finish();
        });

    }
}
