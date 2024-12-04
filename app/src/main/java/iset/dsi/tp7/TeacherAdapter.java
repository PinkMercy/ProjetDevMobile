package iset.dsi.tp7;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {
    private List<Teacher> teacherList;
    private int position; // Holds the position of the item for context menu actions

    public TeacherAdapter(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    public class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView nameTextView, emailTextView;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.teacher_name);
            emailTextView = itemView.findViewById(R.id.teacher_email);

            // Set long-click listener to open context menu
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // Save the position of the item that was long-pressed
            position = getAdapterPosition();
            menu.add(this.getAdapterPosition(), R.id.delete, 0, "Delete");
        }
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.nameTextView.setText(teacher.getName());
        holder.emailTextView.setText(teacher.getEmail());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public void removeItem() {
        if (position >= 0 && position < teacherList.size()) {
            teacherList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, teacherList.size());
        }
    }

    public void sortByName(List<Teacher> teachers) {
        Collections.sort(teachers, Comparator.comparing(Teacher::getName));
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < teacherList.size()) {
            teacherList.remove(position);
            notifyItemRemoved(position);
        }
    }
    // Method to sort by name A-Z
    public void sortByName() {
        Collections.sort(teacherList, Comparator.comparing(Teacher::getName));
        notifyDataSetChanged();
    }

    // Method to sort by name Z-A
    public void reverseByName() {
        Collections.sort(teacherList, Comparator.comparing(Teacher::getName).reversed());
        notifyDataSetChanged();
    }
}

