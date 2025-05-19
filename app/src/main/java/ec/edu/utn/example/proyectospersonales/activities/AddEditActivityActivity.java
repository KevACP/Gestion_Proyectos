package ec.edu.utn.example.proyectospersonales.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.utn.example.proyectospersonales.R;
import ec.edu.utn.example.proyectospersonales.database.DatabaseHelper;
import ec.edu.utn.example.proyectospersonales.model.Actividad;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEditActivityActivity extends AppCompatActivity {
    private EditText etActividadName, etActividadDescription, etStartDate, etEndDate;
    private Spinner spinnerEstado;
    private DatabaseHelper dbHelper;
    private int proyectoId, actividadId = -1;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private List<String> estados = Arrays.asList("Planificado", "En ejecución", "Realizado");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_actividad);

        etActividadName = findViewById(R.id.et_actividad_name);
        etActividadDescription = findViewById(R.id.et_actividad_description);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        spinnerEstado = findViewById(R.id.spinner_estado);
        Button btnSave = findViewById(R.id.btn_save);
        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Configurar Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        // Obtener datos del Intent
        Intent intent = getIntent();
        proyectoId = intent.getIntExtra("proyectoId", -1);
        if (intent.hasExtra("actividadId")) {
            actividadId = intent.getIntExtra("actividadId", -1);
            etActividadName.setText(intent.getStringExtra("nombre"));
            etActividadDescription.setText(intent.getStringExtra("descripcion"));
            etStartDate.setText(intent.getStringExtra("fechaInicio"));
            etEndDate.setText(intent.getStringExtra("fechaFin"));
            String estado = intent.getStringExtra("estado");
            spinnerEstado.setSelection(estados.indexOf(estado));
        } else {
            spinnerEstado.setSelection(0); // Planificado por defecto
        }

        // Configurar DatePicker
        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));

        btnSave.setOnClickListener(v -> {
            String nombre = etActividadName.getText().toString().trim();
            String descripcion = etActividadDescription.getText().toString().trim();
            String fechaInicio = etStartDate.getText().toString().trim();
            String fechaFin = etEndDate.getText().toString().trim();
            String estado = spinnerEstado.getSelectedItem().toString();

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

            Actividad actividad;
            if (actividadId == -1) {
                // Nueva actividad
                actividad = new Actividad(proyectoId, nombre, descripcion, fechaInicio, fechaFin, estado);
                if (dbHelper.addActividad(actividad)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error al agregar actividad", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Editar actividad
                actividad = new Actividad(actividadId, proyectoId, nombre, descripcion, fechaInicio, fechaFin, estado);
                if (dbHelper.updateActividad(actividad)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error al actualizar actividad", Toast.LENGTH_SHORT).show();
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