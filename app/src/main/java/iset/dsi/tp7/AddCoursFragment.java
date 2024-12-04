package iset.dsi.tp7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AddCoursFragment extends Fragment {

    private EditText editNomCours, editNbHeures;
    private RadioButton radioCours, radioAtelier;
    private Spinner spinnerTeacher;
    private Button btnAddCours;
    private Button btnDeleteCours;

    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_cours, container, false);
        dbHelper = new DatabaseHelper(getContext());
        editNomCours = view.findViewById(R.id.et_nom_cours);
        editNbHeures = view.findViewById(R.id.et_nombre_heures);
        radioCours = view.findViewById(R.id.radio_cours);
        radioAtelier = view.findViewById(R.id.radio_atelier);
        spinnerTeacher = view.findViewById(R.id.spinner_teacher);
        btnAddCours = view.findViewById(R.id.btn_add_cours);
        Spinner spinnerTeacher = view.findViewById(R.id.spinner_teacher);

// Fetch all teachers from the database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Teacher> teachers = dbHelper.getAllTeachers();

// Create a list of teacher names to display in the spinner
        List<String> teacherNames = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teacherNames.add(teacher.getName());
        }

// Set the adapter for the spinner
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, teacherNames);
        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeacher.setAdapter(teacherAdapter);
        final DatabaseHelper finalDbHelper = dbHelper;

// Optional: Retrieve the selected teacher ID when a course is added
        spinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected teacher's ID from the list
                Teacher selectedTeacher = teachers.get(position);
                int selectedTeacherId = selectedTeacher.getId(); // Use this ID for inserting into the course
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        dbHelper = new DatabaseHelper(getContext());

        btnAddCours.setOnClickListener(v -> {
            String name = editNomCours.getText().toString().trim();
            float nbHeures = Float.parseFloat(editNbHeures.getText().toString());
            String type = radioCours.isChecked() ? "Cours" : "Atelier";

            // Get the selected teacher ID
            //////int enseignantId = teachers.get(spinnerTeacher.getSelectedItemPosition()).getId();
            int enseignantId = spinnerTeacher.getSelectedItemPosition() + 1; // Récupérer l'ID du professeur via Spinner




            // Insert the course into the database
            long result = finalDbHelper.insertCours(name, nbHeures, type, enseignantId);
            if (result != -1) {
                Toast.makeText(getContext(), "Cours ajouté avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Échec de l'ajout", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


    private int getSelectedTeacherId() {
        // Simulate getting teacher ID based on the spinner selection
        return 1; // Replace with actual logic to get the teacher's ID
    }
}