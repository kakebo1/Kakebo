package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class RecuperarContra extends AppCompatActivity {

        private EditText mforgotPassword;
        FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recuperar_contra);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mforgotPassword = findViewById(R.id.correoRecuperacion);

        Button mpasswordRecoverButton = findViewById(R.id.btnRecuperarContra);
        TextView mgobacktologin = findViewById(R.id.goBackToLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        mgobacktologin.setOnClickListener(v -> {
            startActivity(new Intent(this, InicioSesion.class));
        });

        mpasswordRecoverButton.setOnClickListener(v -> {
            String mail = mforgotPassword.getText().toString().trim();

            if (mail.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Se ha enviado un código a tu correo", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, InicioSesion.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Correo incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}