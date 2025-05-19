package ec.edu.utn.example.proyectospersonales.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ec.edu.utn.example.proyectospersonales.R;
import ec.edu.utn.example.proyectospersonales.adapters.ProjectAdapter;
import ec.edu.utn.example.proyectospersonales.database.DatabaseHelper;
import ec.edu.utn.example.proyectospersonales.model.Proyecto;
import java.util.ArrayList;
import java.util.List;

public class ProjectListActivity extends AppCompatActivity implements ProjectAdapter.OnProjectClickListener {
    private RecyclerView rvProjects;
    private ProjectAdapter adapter;
    private List<Proyecto> proyectos;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        rvProjects = findViewById(R.id.rv_projects);
        Button btnAddProject = findViewById(R.id.btn_add_project);
        dbHelper = new DatabaseHelper(this);
        proyectos = new ArrayList<>();

        // Obtener userId de SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        // Configurar RecyclerView
        adapter = new ProjectAdapter(proyectos, this, dbHelper);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));
        rvProjects.setAdapter(adapter);

        // Cargar proyectos
        loadProyectos();

        // Botón para agregar proyecto
        btnAddProject.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditProjectActivity.class);
            intent.putExtra("userId", userId);
            startActivityForResult(intent, 1);
        });
    }

    private void loadProyectos() {
        proyectos = dbHelper.getProyectosByUser(userId);
        adapter.updateProyectos(proyectos);
    }

    @Override
    public void onViewActividadesClick(Proyecto proyecto) {
        Intent intent = new Intent(this, ActivityListActivity.class);
        intent.putExtra("proyectoId", proyecto.getIdProyecto());
        startActivityForResult(intent, 3);
    }

    @Override
    public void onEditClick(Proyecto proyecto) {
        Intent intent = new Intent(this, AddEditProjectActivity.class);
        intent.putExtra("proyectoId", proyecto.getIdProyecto());
        intent.putExtra("userId", userId);
        intent.putExtra("nombre", proyecto.getNombre());
        intent.putExtra("descripcion", proyecto.getDescripcion());
        intent.putExtra("fechaInicio", proyecto.getFechaInicio());
        intent.putExtra("fechaFin", proyecto.getFechaFin());
        startActivityForResult(intent, 2);
    }

    @Override
    public void onDeleteClick(Proyecto proyecto) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar el proyecto '" + proyecto.getNombre() + "'? Esto también eliminará todas sus actividades.")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    if (dbHelper.deleteProyecto(proyecto.getIdProyecto())) {
                        loadProyectos();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadProyectos();
        }
    }
}