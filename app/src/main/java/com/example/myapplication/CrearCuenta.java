package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearCuenta extends AppCompatActivity {

    private EditText mSignUpEmail, mSignUpPasswords;
    private FirebaseAuth firebaseAuth;
    private CheckBox checkBoxTyC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_crear_cuenta);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mSignUpEmail = findViewById(R.id.txtCorreoCrear);
        mSignUpPasswords = findViewById(R.id.txtPasswordCrear);
        Button mSignUp = findViewById(R.id.btnRegistrarCuenta);
        Button mGoToLogIn = findViewById(R.id.gotoLogIn);
        TextView txtTerminosyCond = findViewById(R.id.txtTerminosyCond);
        checkBoxTyC = findViewById(R.id.checkBoxTyC);

        firebaseAuth = FirebaseAuth.getInstance();

        // Mostrar términos y condiciones
        txtTerminosyCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CrearCuenta.this)
                        .setTitle("Términos y condiciones")
                        .setMessage(getString(R.string.TerminosyCond))
                        .setPositiveButton("Aceptar", null)
                        .show();
            }
        });

        mGoToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearCuenta.this, InicioSesion.class);
                startActivity(intent);
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mSignUpEmail.getText().toString().trim();
                String password = mSignUpPasswords.getText().toString().trim();

                if (!checkBoxTyC.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
                } else if (mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    Toast.makeText(getApplicationContext(), "Correo electrónico inválido", Toast.LENGTH_SHORT).show();
                } else if (!mail.endsWith("@gmail.com")) {
                    Toast.makeText(getApplicationContext(), "Solo se permiten correos de Gmail", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 7) {
                    Toast.makeText(getApplicationContext(), "La contraseña debe ser mayor a 7 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(mail, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                        sendEmailVerification();
                                    } else {
                                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "";
                                        if (errorMsg.contains("email address is already in use")) {
                                            Toast.makeText(getApplicationContext(), "Este correo ya ha sido registrado", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "El correo de verificación ha sido enviado, verifíquelo e ingrese de nuevo", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    Intent intent = new Intent(CrearCuenta.this, InicioSesion.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Error al enviar correo de verificación", Toast.LENGTH_SHORT).show();
        }
    }
}