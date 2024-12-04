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

import java.util.ArrayList;
import java.util.List;

public class ListeCoursFragment extends Fragment {

    private RecyclerView recyclerView;
    private CoursAdapter coursAdapter;
    private List<Cours> coursList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste_cours, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_cours);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());
        coursList = getCoursListFromDatabaseOrList();
        coursAdapter = new CoursAdapter(coursList);
        recyclerView.setAdapter(coursAdapter);

        return view;
    }

    private List<Cours> getCoursListFromDatabaseOrList() {
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
