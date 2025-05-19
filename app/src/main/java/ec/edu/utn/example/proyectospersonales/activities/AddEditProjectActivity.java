package ec.edu.utn.example.proyectospersonales.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.utn.example.proyectospersonales.R;
import ec.edu.utn.example.proyectospersonales.database.DatabaseHelper;
import ec.edu.utn.example.proyectospersonales.model.Proyecto;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditProjectActivity extends AppCompatActivity {
    private EditText etProjectName, etProjectDescription, etStartDate, etEndDate;
    private DatabaseHelper dbHelper;
    private int userId, proyectoId = -1;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_project);

        etProjectName = findViewById(R.id.et_project_name);
        etProjectDescription = findViewById(R.id.et_project_description);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        Button btnSave = findViewById(R.id.btn_save);
        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Obtener datos del Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        if (intent.hasExtra("proyectoId")) {
            proyectoId = intent.getIntExtra("proyectoId", -1);
            etProjectName.setText(intent.getStringExtra("nombre"));
            etProjectDescription.setText(intent.getStringExtra("descripcion"));
            etStartDate.setText(intent.getStringExtra("fechaInicio"));
            etEndDate.setText(intent.getStringExtra("fechaFin"));
        }

        // Configurar DatePicker para fecha de inicio
        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));

        // Configurar DatePicker para fecha de fin
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));

        btnSave.setOnClickListener(v -> {
            String nombre = etProjectName.getText().toString().trim();
            String descripcion = etProjectDescription.getText().toString().trim();
            String fechaInicio = etStartDate.getText().toString().trim();
            String fechaFin = etEndDate.getText().toString().trim();

            // Validaciones
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fechaInicio.isEmpty()) {
                Toast.makeText(this, "La fecha de inicio es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidDateFormat(fechaInicio)) {
                Toast.makeText(this, "Fecha de inicio inválida (use YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!fechaFin.isEmpty() && !isValidDateFormat(fechaFin)) {
                Toast.makeText(this, "Fecha de fin inválida (use YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!fechaFin.isEmpty() && !isEndDateValid(fechaInicio, fechaFin)) {
                Toast.makeText(this, "La fecha de fin no puede ser anterior a la fecha de inicio", Toast.LENGTH_SHORT).show();
                return;
            }

            Proyecto proyecto;
            if (proyectoId == -1) {
                // Nuevo proyecto
                proyecto = new Proyecto(userId, nombre, descripcion, fechaInicio, fechaFin);
                if (dbHelper.addProyecto(proyecto)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error al agregar proyecto", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Editar proyecto
                proyecto = new Proyecto(proyectoId, userId, nombre, descripcion, fechaInicio, fechaFin);
                if (dbHelper.updateProyecto(proyecto)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error al actualizar proyecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog(EditText editText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format(Locale.US, "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editText.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private boolean isValidDateFormat(String date) {
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isEndDateValid(String startDate, String endDate) {
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            return !end.before(start);
        } catch (ParseException e) {
            return false;
        }
    }
}