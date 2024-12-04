package iset.dsi.tp7;

import android.annotation.SuppressLint;
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

    public static TeacherAdapter mAdapter; // Static access for MainActivity
    private List<Teacher> teacherList;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        teacherList = new ArrayList<>();
        populateTeacherList();

        mAdapter = new TeacherAdapter(teacherList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void populateTeacherList() {
        for (int i = 1; i <= 100; i++) {
            teacherList.add(new Teacher(i, "Teacher " + i, "teacher" + i + "@example.com"));
        }
    }

}

