package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearCuenta extends AppCompatActivity {

    private EditText mSignUpEmail, mSignUpPasswords;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_crear_cuenta);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        mSignUpEmail = findViewById(R.id.txtCorreoCrear);
        mSignUpPasswords = findViewById(R.id.txtPasswordCrear);
        Button mSignUp = findViewById(R.id.btnRegistrarCuenta);
        Button mGoToLogIn = findViewById(R.id.gotoLogIn);

        firebaseAuth = FirebaseAuth.getInstance();

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

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                } else if(password.length() < 7){
                    Toast.makeText(getApplicationContext(), "La contraseña debe ser mayor a 7 caracteres", Toast.LENGTH_SHORT).show();
                } else{
                    // registered the user to firebase
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            } else{
                                Toast.makeText(getApplicationContext(), "Error al crear usuario", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "El correo de verificacion ha sido enviado, verifiquelo e ingrese de nuevo", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();

                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Error al enviar correo de verificacion", Toast.LENGTH_SHORT).show();
        }
    }
}