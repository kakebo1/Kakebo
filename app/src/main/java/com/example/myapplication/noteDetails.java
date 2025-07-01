package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class noteDetails extends AppCompatActivity {

    private TextView mtitleofnotedetail, mcontentofnotedetail;
    FloatingActionButton mgotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        mtitleofnotedetail = findViewById(R.id.titleofnotedetail);
        mcontentofnotedetail = findViewById(R.id.contentofnotedetail);
        mgotoeditnote = findViewById(R.id.gotoeditnote);
        Toolbar toolbar = findViewById(R.id.toolbarofnotedetail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        mgotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), editNoteActivity.class);
                intent.putExtra("Title", data.getStringExtra("Title"));
                intent.putExtra("Content", data.getStringExtra("Content"));
                intent.putExtra("noteId", data.getStringExtra("noteId"));
                view.getContext().startActivity(intent);
            }
        });
        mcontentofnotedetail.setText(data.getStringExtra("Content"));
        mtitleofnotedetail.setText(data.getStringExtra("Title"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(noteDetails.this, notasActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}