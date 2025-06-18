package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class InicioSesion extends AppCompatActivity {

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    Log.d("Permiso", "Permiso de notificaciones concedido");
                } else {
                    Log.d("Permiso", "Permiso de notificaciones denegado");
                    Toast.makeText(this, "No recibirás notificaciones", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permiso", "Notificaciones activadas");
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("MainActivity", "Fetching Registration Token Failed", task.getException());
                return;
            }

            String token = task.getResult();
            Log.d("MainActivity", "Token: " + token);
        });
    }

    private EditText mloginemail, mloginpassword;
    private FirebaseAuth firebaseAuth;
    private ImageButton btnVerContra;
    private ProgressBar mprogressbaroflogin;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_inicio_sesion);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        askNotificationPermission();
        getToken();

        mloginemail = findViewById(R.id.loginemail);
        mloginpassword = findViewById(R.id.loginPassword);
        Button mlog_in = findViewById(R.id.btnContinuar);
        TextView mgotoforgotPassword = findViewById(R.id.gotoforgotPassword);
        Button mCrearCuenta = findViewById(R.id.btnCrearCuenta);
        mprogressbaroflogin = findViewById(R.id.progressbaroflogin);
        btnVerContra = findViewById(R.id.btnVerContra);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            finish();
            startActivity(new Intent(InicioSesion.this, MenuPrinc.class));
        }

        mCrearCuenta.setOnClickListener(view ->
                startActivity(new Intent(InicioSesion.this, CrearCuenta.class))
        );

        mgotoforgotPassword.setOnClickListener(view ->
                startActivity(new Intent(InicioSesion.this, RecuperarContra.class))
        );

        mlog_in.setOnClickListener(view -> {
            String mail = mloginemail.getText().toString().trim();
            String password = mloginpassword.getText().toString().trim();

            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            } else {
                mprogressbaroflogin.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener((Task<AuthResult> task) -> {
                    if (task.isSuccessful()) {
                        checkmailverification();
                    } else {
                        Toast.makeText(getApplicationContext(), "La cuenta no existe", Toast.LENGTH_SHORT).show();
                        mprogressbaroflogin.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        // Mostrar/Ocultar contraseña
        btnVerContra.setOnClickListener(v -> {
            if (isPasswordVisible) {
                mloginpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnVerContra.setImageResource(R.drawable.ojo_cerrado); // ocultar
            } else {
                mloginpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnVerContra.setImageResource(R.drawable.ojoabierto); // mostrar
            }
            mloginpassword.setSelection(mloginpassword.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });
    }

    private void checkmailverification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Sesión Iniciada", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(InicioSesion.this, MenuPrinc.class));
        } else {
            mprogressbaroflogin.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verifica tu cuenta con el link de activación que enviamos a tu correo", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }
}
