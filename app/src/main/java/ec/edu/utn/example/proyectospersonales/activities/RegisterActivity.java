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
import ec.edu.utn.example.proyectospersonales.model.Usuario;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etEmail;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        Button btnRegister = findViewById(R.id.btn_register);
        TextView tvLogin = findViewById(R.id.tv_login);
        dbHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuario = new Usuario(username, password, email);
            if (dbHelper.registerUser(usuario)) {
                Toast.makeText(this, "Registro exitoso. Inicia sesión.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_SHORT).show();
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}