package com.example.myapplication;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.OnBackPressedCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class createNote extends AppCompatActivity {

        EditText mcreatetitleofnote, mcreatecontentofnote;
        FloatingActionButton msavenote;
        FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
        FirebaseFirestore db;
        ProgressBar mprogressbarofcreatenote;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_note);

            msavenote = findViewById(R.id.savenote);
            mcreatecontentofnote = findViewById(R.id.createcontentofnote);
            mcreatetitleofnote = findViewById(R.id.createtitleofnote);
            mprogressbarofcreatenote = findViewById(R.id.progressbarofcreatenote);

            Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

            firebaseAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser != null) {
                String userId = firebaseUser.getUid();
                Log.d("FIREBASE_UID", "UID del usuario: " + userId);
            } else {
                Log.d("FIREBASE_UID", "firebaseUser es null");
            }

            msavenote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = mcreatetitleofnote.getText().toString();
                    String content = mcreatecontentofnote.getText().toString();

                    if (title.isEmpty() || content.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Ambos campos son requeridos", Toast.LENGTH_SHORT).show();
                    } else{
                        mprogressbarofcreatenote.setVisibility(View.VISIBLE);
                        DocumentReference documentReference = db.collection("notas").document(firebaseUser.getUid()).collection("MisNotas").document();
                        Map<String, Object> note = new HashMap<>();
                        note.put("Title", title);
                        note.put("Content", content);

                        documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Nota creada exitosamente", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(createNote.this, notasActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error al crear la nota", Toast.LENGTH_SHORT).show();
                                mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            });

            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    startActivity(new Intent(createNote.this, notasActivity.class));
                    finish();
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                getOnBackPressedDispatcher().onBackPressed();
            }
            return super.onOptionsItemSelected(item);
        }
    }

