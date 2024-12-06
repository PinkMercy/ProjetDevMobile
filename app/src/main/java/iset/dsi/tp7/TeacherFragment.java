package iset.dsi.tp7;

import android.annotation.SuppressLint;
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

public class TeacherFragment extends Fragment {

    private TeacherAdapter mAdapter; // Static access for MainActivity
    private List<Teacher> teacherList;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        dbHelper = new DatabaseHelper(getContext());
        teacherList = getTeachersFromDatabase(); // Fetch teachers dynamically
        mAdapter = new TeacherAdapter(teacherList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    // Fetch teachers from the database
    private List<Teacher> getTeachersFromDatabase() {
        List<Teacher> teachers = new ArrayList<>();

        // Get all teachers from the database
        Cursor cursor = (Cursor) dbHelper.getAllTeachers(); // Use your method to get teachers

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_EMAIL));
                teachers.add(new Teacher(id, name, email));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return teachers;
    }

}
