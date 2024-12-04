package iset.dsi.tp7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeacherAdapter teacherAdapter;
    private List<Teacher> teacherList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.mRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // Initialize the data list and adapter
        teacherList = new ArrayList<>();
        populateTeacherList();

        // Initialize adapter with the teacher list
        teacherAdapter = new TeacherAdapter(teacherList);
        recyclerView.setAdapter(teacherAdapter);  // Set adapter to RecyclerView


        // Set adapter in MainActivity for access
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setTeacherAdapter(teacherAdapter, teacherList);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView headerList = view.findViewById(R.id.header_list);
        headerList.setOnClickListener(this::onShowPopupMenu);
    }

    // Method to populate teacher list with multiple entries
    private void populateTeacherList() {
        for (int i = 1; i <= 20; i++) {
            teacherList.add(new Teacher(i, "Teacher " + i, "teacher" + i + "@example.com"));
        }

        // Unique sample entries
        teacherList.add(new Teacher(21, "rahma", "rahma@gmail.com"));
        teacherList.add(new Teacher(22, "bacha", "bacha@gmail.com"));
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            int position = item.getOrder();  // Assuming you use a mechanism to set the correct position
            teacherAdapter.removeItem(position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void onShowPopupMenu(View view) {
        // Create a new PopupMenu
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.popup_menu_teacher); // Inflate the new popup menu

        // Set up click handling for each item in the popup menu
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.popup_show_hide_recycler) {
                // Toggle RecyclerView visibility
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                return true;
            } else if (item.getItemId() == R.id.popup_change_title_background) {
                // Change background color of the title bar (assuming headerList is the title view)
                View headerList = view.findViewById(R.id.header_list); // Adjust this if needed
                headerList.setBackgroundColor(getResources().getColor(R.color.new_background_color));
                return true;
            }
            return false;
        });

        popupMenu.show(); // Display the popup menu
    }

    private boolean onPopupMenuClick(MenuItem item) {
        if (item.getItemId() == R.id.popup_show_hide_recycler) {
            // Code to show/hide RecyclerView
            Toast.makeText(requireContext(), "Show/Hide RecyclerView", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.popup_change_title_background) {

            Toast.makeText(requireContext(), "Changer Titre Background", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
