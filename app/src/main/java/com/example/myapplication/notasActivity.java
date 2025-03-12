package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static androidx.core.content.ContextCompat.startActivity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class notasActivity extends AppCompatActivity {

    FloatingActionButton mcrearnotas;
    private FirebaseAuth firebaseAuth;

    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<fbmodel, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        mcrearnotas = findViewById(R.id.crearNota);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Todas las notas");

        mcrearnotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notasActivity.this, createNote.class));
            }
        });

        Query query = firebaseFirestore.collection("notas")
                .document(firebaseUser.getUid())
                .collection("MisNotas")
                .orderBy("Title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<fbmodel> allusernotes = new FirestoreRecyclerOptions.Builder<fbmodel>().setQuery(query,fbmodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<fbmodel, NoteViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull fbmodel fbbmodel) {

                ImageView popupbutton = noteViewHolder.itemView.findViewById(R.id.menupopbutton);
                int colorcode = getRandomColor();
                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colorcode,null));
                noteViewHolder.notetitle.setText(fbbmodel.getTitle());
                noteViewHolder.notecontent.setText(fbbmodel.getContent());
                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //we have to open note detail activity
                        Intent intent = new Intent(view.getContext(), noteDetails.class);
                        intent.putExtra("Title", fbbmodel.getTitle());
                        intent.putExtra("Content",fbbmodel.getContent());
                        intent.putExtra("noteId", docId);
                        view.getContext().startActivity(intent);
                    }
                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Editar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                                Intent intent = new Intent(view.getContext(), editNoteActivity.class);
                                intent.putExtra("Title", fbbmodel.getTitle());
                                intent.putExtra("Content",fbbmodel.getContent());
                                intent.putExtra("noteId", docId);
                                view.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Eliminar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                                // Toast.makeText(view.getContext(), "This note is deleted", Toast.LENGTH_SHORT).show();
                                DocumentReference documentReference = firebaseFirestore.collection("notas").document(firebaseUser.getUid()).collection("MisNotas").document("noteId");
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(), "La nota ha sido borrada", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(), "No se ha podido borrar la nota", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerview = findViewById(R.id.recyclerView);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteAdapter);


        // MENU LATERAL
        Spinner menuLateral=findViewById(R.id.menuLateral);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.menu,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuLateral.setAdapter(adapter);

        menuLateral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override

            public void onItemSelected (AdapterView<?> adapterView, View view,int i, long l){

                if (adapterView.getItemAtPosition(i).equals("Inicio")) {
                    Intent inicio = new Intent(notasActivity.this, MenuPrinc.class);
                    startActivity(inicio);
                }

                if (adapterView.getItemAtPosition(i).equals("Ingresos")) {
                    Intent ingresos = new Intent(notasActivity.this, Ingresos.class);
                    startActivity(ingresos);
                }

                if (adapterView.getItemAtPosition(i).equals("Egresos")) {
                    Intent egresos = new Intent(notasActivity.this, Egresos.class);
                    startActivity(egresos);
                }

                if (adapterView.getItemAtPosition(i).equals("Notas")) {
                    Intent notas = new Intent(notasActivity.this, Notas.class);
                    startActivity(notas);
                }

                if (adapterView.getItemAtPosition(i).equals("Planeación de deudas")) {
                    Intent deudas = new Intent(notasActivity.this, PlaneacionDeudas.class);
                    startActivity(deudas);
                }

                if (adapterView.getItemAtPosition(i).equals("Categorias")) {
                    Intent categorias = new Intent(notasActivity.this, Categorias.class);
                    startActivity(categorias);
                }

                if (adapterView.getItemAtPosition(i).equals("Reportes")) {
                    Intent reportes = new Intent(notasActivity.this, Reportes.class);
                    startActivity(reportes);
                }
                if (adapterView.getItemAtPosition(i).equals("Meta financiera")) {
                    Intent metafin = new Intent(notasActivity.this, MetaFin.class);
                    startActivity(metafin);
                }
                if (adapterView.getItemAtPosition(i).equals("Ayuda")) {
                    Intent ayuda = new Intent(notasActivity.this, Ayuda.class);
                    startActivity(ayuda);
                }

                //SI SE SELECCIONA EL TEXTO SE CAMBIA EL COLOR DE LAS PANTALLAS
                // if (adapterView.getItemAtPosition(i).equals ("Inicio")){
                //   Intent inicio = new Intent(Ingresos.this, MenuPrinc.class);
                // startActivity(inicio);
                // }  /** FIN CÓDIGO BOTÓN INICIO MENÚ   **/
            }
        });

    }

    public class NoteViewHolder extends  RecyclerView.ViewHolder{
        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemview){
            super(itemview);
            notetitle = itemview.findViewById(R.id.noteTitle);
            notecontent = itemview.findViewById(R.id.notecontent);
            mnote = itemview.findViewById(R.id.note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(notasActivity.this, InicioSesion.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }

    private int getRandomColor(){
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.gray);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.lightgreen);
        colorcode.add(R.color.green);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.color4);
        colorcode.add(R.color.color5);

        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);
    }

}

