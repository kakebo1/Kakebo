package com.example.myapplication;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;


public class InicioSesion extends AppCompatActivity {

    private EditText mloginemail, mloginpassword;
    private FirebaseAuth firebaseAuth;

    ProgressBar mprogressbaroflogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_inicio_sesion);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        mloginemail = findViewById(R.id.loginemail);
        mloginpassword = findViewById(R.id.loginPassword);
        Button mlog_in = findViewById(R.id.btnContinuar);
        TextView mgotoforgotPassword = findViewById(R.id.gotoforgotPassword);
        Button mCrearCuenta = findViewById(R.id.btnCrearCuenta);
        mprogressbaroflogin = findViewById(R.id.progressbaroflogin);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(InicioSesion.this, MenuPrinc.class));
        }
/*        else{  Toast.makeText(getApplicationContext(), "Sesión Cerrada", Toast.LENGTH_SHORT).show();
        } */

        mCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InicioSesion.this, CrearCuenta.class));
            }
        });

        mgotoforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InicioSesion.this, RecuperarContra.class));
            }
        });

        mlog_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mloginemail.getText().toString().trim();
                String password = mloginpassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                }else{
                    mprogressbaroflogin.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkmailverification();
                            } else{
                                Toast.makeText(getApplicationContext(), "La cuenta no existe", Toast.LENGTH_SHORT).show();
                                mprogressbaroflogin.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkmailverification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        if(firebaseUser.isEmailVerified()){
            Toast.makeText(getApplicationContext(), "Sesión Iniciada", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(InicioSesion.this, MenuPrinc.class));
        } else{
            mprogressbaroflogin.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verifica tu cuenta con el link de activacion que enviamos a tu correo", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}