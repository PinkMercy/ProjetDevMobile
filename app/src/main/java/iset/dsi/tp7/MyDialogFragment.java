package iset.dsi.tp7;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.widget.Toast;


public class MyDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_add_dialog, null);

        // Set the custom view in the dialog
        builder.setView(dialogView);

        // Retrieve the EditText fields from the dialog layout
        final EditText nameInput = dialogView.findViewById(R.id.edit_text);
        final EditText emailInput = dialogView.findViewById(R.id.email);

        // Build the dialog with title, message, and buttons
        builder.setTitle("Ajouter Nouveau Enseignant")
                .setMessage("Donner nom et email de l'enseignant")
                .setPositiveButton("Valider", (dialog, which) -> {
                    // Retrieve entered name and email
                    String teacherName = nameInput.getText().toString().trim();
                    String teacherEmail = emailInput.getText().toString().trim();

                    // Check if fields are not empty
                    if (!teacherName.isEmpty() && !teacherEmail.isEmpty()) {
                        // Add new teacher to MainActivity's list
                        if (getActivity() instanceof MainActivity) {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            int id = mainActivity.getNextTeacherId(); // Define a method to get the next ID
                            mainActivity.addTeacherToList(new Teacher(id, teacherName, teacherEmail));


                        }
                    } else {
                        // Display a message if fields are empty
                        Toast.makeText(requireContext(), "Les champs ne peuvent pas être vides", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}
