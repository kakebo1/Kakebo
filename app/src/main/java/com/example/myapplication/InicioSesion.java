package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;


public class InicioSesion extends AppCompatActivity {

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
        if (isGranted){
            Log.d("Permiso", "Permiso de notificaciones concedido");
        } else{
            Log.d("Permiso", "Permiso de notificaciones denegado");
            Toast.makeText(this, "No recibirás notificaciones", Toast.LENGTH_SHORT).show();
        }
    });

    private void askNotificationPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                Log.d("Permiso", "Notificaciones activadas");
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(this, "La app necesita permiso para mostrar notificaciones", Toast.LENGTH_SHORT).show();
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else{
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.w("MainActivity", "Fetching Registration Token Failed", task.getException());
                return;
            }

            String token = task.getResult();
            Log.d("MainActivity", "Token: " + token);
            //Toast.makeText(InicioSesion.this, "Token: " + token, Toast.LENGTH_LONG).show();
        });
    }

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

        askNotificationPermission();
        getToken();


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