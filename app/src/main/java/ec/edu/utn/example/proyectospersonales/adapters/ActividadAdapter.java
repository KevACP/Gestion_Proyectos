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
import ec.edu.utn.example.proyectospersonales.model.Actividad;
import java.util.List;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {
    private List<Actividad> actividades;
    private OnActividadClickListener listener;

    public interface OnActividadClickListener {
        void onEditClick(Actividad actividad);
        void onDeleteClick(Actividad actividad);
    }

    public ActividadAdapter(List<Actividad> actividades, OnActividadClickListener listener) {
        this.actividades = actividades;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actividad, parent, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad actividad = actividades.get(position);
        holder.tvActividadName.setText(actividad.getNombre());
        holder.tvActividadDescription.setText(actividad.getDescripcion() != null ? actividad.getDescripcion() : "Sin descripción");
        holder.tvActividadDates.setText("Inicio: " + actividad.getFechaInicio() + " | Fin: " +
                (actividad.getFechaFin() != null ? actividad.getFechaFin() : "No definido"));
        holder.tvActividadEstado.setText("Estado: " + actividad.getEstado());

        // Mostrar progreso
        int progreso = actividad.getProgreso();
        holder.tvActividadProgreso.setText("Progreso: " + progreso + "%");
        holder.actividadProgressBar.setProgress(progreso);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(actividad));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(actividad));
    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }

    public void updateActividades(List<Actividad> newActividades) {
        this.actividades = newActividades;
        notifyDataSetChanged();
    }

    static class ActividadViewHolder extends RecyclerView.ViewHolder {
        TextView tvActividadName, tvActividadDescription, tvActividadDates, tvActividadEstado, tvActividadProgreso;
        ProgressBar actividadProgressBar;
        Button btnEdit, btnDelete;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvActividadName = itemView.findViewById(R.id.tv_actividad_name);
            tvActividadDescription = itemView.findViewById(R.id.tv_actividad_description);
            tvActividadDates = itemView.findViewById(R.id.tv_actividad_dates);
            tvActividadEstado = itemView.findViewById(R.id.tv_actividad_estado);
            tvActividadProgreso = itemView.findViewById(R.id.tv_actividad_progreso);
            actividadProgressBar = itemView.findViewById(R.id.actividad_progress_bar);
            btnEdit = itemView.findViewById(R.id.btn_edit_actividad);
            btnDelete = itemView.findViewById(R.id.btn_delete_actividad);
        }
    }
}