package iset.dsi.tp7;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CoursAdapter extends RecyclerView.Adapter<CoursAdapter.CoursViewHolder> {

    private List<Cours> coursList;

    public CoursAdapter(List<Cours> coursList) {
        this.coursList = coursList;
    }

    public static class CoursViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomCours, tvTypeCours;
        EditText etNombreHeure;
        Spinner spinnerTeacher;
        Button btnEdit, btnDelete;

        public CoursViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomCours = itemView.findViewById(R.id.tv_nom_cours);
            tvTypeCours = itemView.findViewById(R.id.tv_type_cours);
            etNombreHeure = itemView.findViewById(R.id.et_nombre_heure);
            spinnerTeacher = itemView.findViewById(R.id.spinner_teacher);
            btnEdit = itemView.findViewById(R.id.btn_edit_cours);
            btnDelete = itemView.findViewById(R.id.btn_delete_cours);
        }
    }

    @NonNull
    @Override
    public CoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cours, parent, false);
        return new CoursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursViewHolder holder, int position) {
        Cours cours = coursList.get(position);

        holder.tvNomCours.setText(cours.getName());
        holder.tvTypeCours.setText(cours.getType());
        holder.etNombreHeure.setText(String.valueOf(cours.getNbHeures()));

        holder.btnEdit.setOnClickListener(v -> {
            // Logic for editing the course
            String newNombreHeure = holder.etNombreHeure.getText().toString().trim();
            if (!newNombreHeure.isEmpty()) {
                try {
                    cours.setNbHeures(Float.parseFloat(newNombreHeure));
                    Toast.makeText(holder.itemView.getContext(), "Cours modifié", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(holder.itemView.getContext(), "Nombre d'heures invalide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Logic for deleting the course
            coursList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, coursList.size());
            Toast.makeText(holder.itemView.getContext(), "Cours supprimé", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return coursList.size();
    }
}
