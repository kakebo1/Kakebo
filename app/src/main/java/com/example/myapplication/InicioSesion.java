package com.example.myapplication;

import android.os.Bundle;
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

public class InicioSesion extends AppCompatActivity {

    private EditText mloginemail, mloginpassword;
    private RelativeLayout mlog_in, mCrearCuenta;
    private TextView mgotoforgotPassword;
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
        mlog_in = findViewById(R.id.log_in);
        mgotoforgotPassword = findViewById(R.id.gotoforgotPassword);
        mCrearCuenta = findViewById(R.id.btnCrearCuenta);
    }
}