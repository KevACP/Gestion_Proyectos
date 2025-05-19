package ec.edu.utn.example.proyectospersonales.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ec.edu.utn.example.proyectospersonales.R;
import ec.edu.utn.example.proyectospersonales.database.DatabaseHelper;
import ec.edu.utn.example.proyectospersonales.model.Proyecto;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private List<Proyecto> proyectos;
    private OnProjectClickListener listener;
    private DatabaseHelper dbHelper;

    public interface OnProjectClickListener {
        void onViewActividadesClick(Proyecto proyecto);
        void onEditClick(Proyecto proyecto);
        void onDeleteClick(Proyecto proyecto);
    }

    public ProjectAdapter(List<Proyecto> proyectos, OnProjectClickListener listener, DatabaseHelper dbHelper) {
        this.proyectos = proyectos;
        this.listener = listener;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Proyecto proyecto = proyectos.get(position);
        holder.tvProjectName.setText(proyecto.getNombre());
        holder.tvProjectDescription.setText(proyecto.getDescripcion() != null ? proyecto.getDescripcion() : "Sin descripción");
        holder.tvProjectDates.setText("Inicio: " + proyecto.getFechaInicio() + " | Fin: " +
                (proyecto.getFechaFin() != null ? proyecto.getFechaFin() : "No definido"));

        // Calcular y mostrar progreso
        int progreso = dbHelper.getProgresoProyecto(proyecto.getIdProyecto());
        holder.tvProjectProgress.setText("Progreso: " + progreso + "%");
        holder.progressBar.setProgress(progreso);

        holder.btnViewActividades.setOnClickListener(v -> listener.onViewActividadesClick(proyecto));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(proyecto));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(proyecto));
    }

    @Override
    public int getItemCount() {
        return proyectos.size();
    }

    public void updateProyectos(List<Proyecto> newProyectos) {
        this.proyectos = newProyectos;
        notifyDataSetChanged();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName, tvProjectDescription, tvProjectDates, tvProjectProgress;
        ProgressBar progressBar;
        Button btnViewActividades, btnEdit, btnDelete;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectDescription = itemView.findViewById(R.id.tv_project_description);
            tvProjectDates = itemView.findViewById(R.id.tv_project_dates);
            tvProjectProgress = itemView.findViewById(R.id.tv_project_progress);
            progressBar = itemView.findViewById(R.id.progress_bar);
            btnViewActividades = itemView.findViewById(R.id.btn_view_actividades);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}