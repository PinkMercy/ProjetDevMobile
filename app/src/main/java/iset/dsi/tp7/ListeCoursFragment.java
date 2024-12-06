package iset.dsi.tp7;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ListeCoursFragment extends Fragment {

    private RecyclerView recyclerView;
    private CoursAdapter coursAdapter;
    private List<Cours> coursList;
    private List<Cours> filteredCoursList;  // A list to store filtered courses
    private DatabaseHelper dbHelper;
    private SearchView searchView;  // Declare the SearchView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste_cours, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_cours);
        SearchView searchView = (SearchView) view.findViewById(R.id.search_view);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());

        // Get all courses from the database
        coursList = getCoursListFromDatabase();
        filteredCoursList = new ArrayList<>(coursList); // Initially set filtered list to be the same as full list

        // Set up the adapter
        coursAdapter = new CoursAdapter(filteredCoursList);
        recyclerView.setAdapter(coursAdapter);

        // Set up the search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // You can implement some action when the search is submitted if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCourses(newText);  // Filter courses based on the query text
                return false;
            }
        });

        return view;
    }

    private void filterCourses(String query) {
        // Clear the filtered list
        filteredCoursList.clear();

        // If query is not empty, filter the courses
        if (query.isEmpty()) {
            filteredCoursList.addAll(coursList);  // If no query, show all courses
        } else {
            // Filter courses by name (you can add more filters here like type, etc.)
            for (Cours cours : coursList) {
                if (cours.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredCoursList.add(cours);
                }
            }
        }

        // Notify the adapter that the data has changed
        coursAdapter.notifyDataSetChanged();
    }

    private List<Cours> getCoursListFromDatabase() {
        List<Cours> list = new ArrayList<>();

        // Query the database
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM Cours", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new Cours(
                        cursor.getInt(0),      // _id
                        cursor.getString(1),   // name
                        cursor.getFloat(2),    // nbheure
                        cursor.getString(3),   // type
                        cursor.getInt(4)       // enseig_id
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }
}
