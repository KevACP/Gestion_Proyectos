package ec.edu.utn.example.proyectospersonales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ec.edu.utn.example.proyectospersonales.R;
import ec.edu.utn.example.proyectospersonales.adapters.ActividadAdapter;
import ec.edu.utn.example.proyectospersonales.database.DatabaseHelper;
import ec.edu.utn.example.proyectospersonales.model.Actividad;
import java.util.ArrayList;
import java.util.List;

public class ActivityListActivity extends AppCompatActivity implements ActividadAdapter.OnActividadClickListener {
    private RecyclerView rvActividades;
    private ActividadAdapter adapter;
    private List<Actividad> actividades;
    private DatabaseHelper dbHelper;
    private int proyectoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);

        rvActividades = findViewById(R.id.rv_actividades);
        Button btnAddActividad = findViewById(R.id.btn_add_actividad);
        dbHelper = new DatabaseHelper(this);
        actividades = new ArrayList<>();

        // Obtener proyectoId del Intent
        proyectoId = getIntent().getIntExtra("proyectoId", -1);

        // Configurar RecyclerView
        adapter = new ActividadAdapter(actividades, this);
        rvActividades.setLayoutManager(new LinearLayoutManager(this));
        rvActividades.setAdapter(adapter);

        // Cargar actividades
        loadActividades();

        // Botón para agregar actividad
        btnAddActividad.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditActivityActivity.class);
            intent.putExtra("proyectoId", proyectoId);
            startActivityForResult(intent, 1);
        });
    }

    private void loadActividades() {
        actividades = dbHelper.getActividadesByProyecto(proyectoId);
        adapter.updateActividades(actividades);
    }

    @Override
    public void onEditClick(Actividad actividad) {
        Intent intent = new Intent(this, AddEditActivityActivity.class);
        intent.putExtra("actividadId", actividad.getIdActividad());
        intent.putExtra("proyectoId", actividad.getIdProyecto());
        intent.putExtra("nombre", actividad.getNombre());
        intent.putExtra("descripcion", actividad.getDescripcion());
        intent.putExtra("fechaInicio", actividad.getFechaInicio());
        intent.putExtra("fechaFin", actividad.getFechaFin());
        intent.putExtra("estado", actividad.getEstado());
        startActivityForResult(intent, 2);
    }

    @Override
    public void onDeleteClick(Actividad actividad) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar la actividad '" + actividad.getNombre() + "'?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    if (dbHelper.deleteActividad(actividad.getIdActividad())) {
                        loadActividades();
                        setResult(RESULT_OK); // Notificar cambios
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadActividades();
            setResult(RESULT_OK); // Notificar a ProjectListActivity
        }
    }
}