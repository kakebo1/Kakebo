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

    public class forgotPassword extends AppCompatActivity {

        private EditText mforgotPassword;

        FirebaseAuth firebaseAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.act_recuperar_contra);

            Objects.requireNonNull(getSupportActionBar()).hide();

            mforgotPassword = findViewById(R.id.correoRecuperacion);
            Button mpasswordRecoverButton = findViewById(R.id.btnRecuperarContra);
            TextView mgobacktologin = findViewById(R.id.goBackToLogin);

            firebaseAuth = FirebaseAuth.getInstance();

            mgobacktologin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.example.myapplication.RecuperarContra.this, InicioSesion.class);
                    startActivity(intent);
                }
            });

            mpasswordRecoverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mail = mforgotPassword.getText().toString().trim();
                    if(mail.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //we have to send password recover email
                        firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Un código ha sido enviado a tu correo, con él puedes recuperar tu contraseña", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(com.example.myapplication.RecuperarContra.this, InicioSesion.class));
                                } else{
                                    Toast.makeText(getApplicationContext(), "Correo incorrecto", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}