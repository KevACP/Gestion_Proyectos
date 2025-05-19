package ec.edu.utn.example.proyectospersonales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.utn.example.proyectospersonales.R;
import ec.edu.utn.example.proyectospersonales.database.DatabaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etIdentifier;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etIdentifier = findViewById(R.id.et_identifier);
        Button btnRecover = findViewById(R.id.btn_recover);
        TextView tvBackToLogin = findViewById(R.id.tv_back_to_login);
        dbHelper = new DatabaseHelper(this);

        btnRecover.setOnClickListener(v -> {
            String identifier = etIdentifier.getText().toString().trim();

            if (identifier.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu usuario o correo", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = dbHelper.getUserEmail(identifier);
            if (email != null) {
                Toast.makeText(this, "Se envió un enlace de recuperación al correo: " + email, Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Usuario o correo no encontrado", Toast.LENGTH_SHORT).show();
            }
        });

        tvBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}