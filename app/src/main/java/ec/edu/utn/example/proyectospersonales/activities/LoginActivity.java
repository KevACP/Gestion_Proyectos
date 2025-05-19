package ec.edu.utn.example.proyectospersonales.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.utn.example.proyectospersonales.R;
import ec.edu.utn.example.proyectospersonales.database.DatabaseHelper;
import ec.edu.utn.example.proyectospersonales.model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_register);
        TextView tvForgotPassword = findViewById(R.id.tv_forgot_password);
        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuario = dbHelper.validateUser(username, password);
            if (usuario != null) {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                prefs.edit()
                        .putString("username", usuario.getNombreUsuario())
                        .putInt("userId", usuario.getIdUsuario())
                        .apply();
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ProjectListActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
    }
}