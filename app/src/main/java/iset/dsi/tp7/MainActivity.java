package iset.dsi.tp7;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import iset.dsi.tp7.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding bind;
    private boolean enseig = false;
    private boolean about = false;
    private boolean cours = false;
    private TeacherAdapter teacherAdapter;
    private List<Teacher> teacherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        setContentView(view);

        // Set toolbar title
        setTitle("Gérer Enseignants");

        // Initialize the toolbar
        setSupportActionBar(bind.toolbar);

        // Set up navigation item selection listener
        bind.navView.setNavigationItemSelectedListener(this);

        // Set up the drawer layout with toggle button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, bind.drawerLayout, bind.toolbar, R.string.open_drawer, R.string.close_drawer);
        bind.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load the default fragment on initial load
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AboutFragment())
                    .commit();
            bind.navView.setCheckedItem(R.id.nav_about);
            about = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_about, menu); // Default menu
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear(); // Clear existing menu items
        if (enseig) {
            getMenuInflater().inflate(R.menu.menu_main_enseig, menu); // Show Enseignant menu
        } else if (cours) {
            getMenuInflater().inflate(R.menu.menu_main_cours, menu); // Show Cours menu
        } else if (about) {
            getMenuInflater().inflate(R.menu.menu_main_about, menu); // Show About menu
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_home) {
            bind.toolbar.setTitle("Gérer Enseignants");
            enseig = true;
            cours = false;
            about = false;
            invalidateOptionsMenu();
            selectedFragment = new HomeFragment();
        } else if (item.getItemId() == R.id.nav_cours) {
            bind.toolbar.setTitle("Gérer Cours");
            enseig = false;
            cours = true;
            about = false;
            invalidateOptionsMenu();
            selectedFragment = new AddCoursFragment(); // Fragment to add courses
        } else if (item.getItemId() == R.id.nav_liste_cours) {
            bind.toolbar.setTitle("Liste des Cours");
            enseig = false;
            cours = true;
            about = false;
            invalidateOptionsMenu();
            selectedFragment = new ListeCoursFragment(); // Fragment to list courses
        } else if (item.getItemId() == R.id.nav_about) {
            bind.toolbar.setTitle("About");
            enseig = false;
            cours = false;
            about = true;
            invalidateOptionsMenu();
            selectedFragment = new AboutFragment();
        } else if (item.getItemId() == R.id.nav_logout) {
            Log.i("tag", "Exit application");
            Toast.makeText(this, "Exit", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        bind.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public int getNextTeacherId() {
        if (teacherList == null || teacherList.isEmpty()) {
            return 1; // Start with ID 1 if the list is empty
        }
        // Find the highest ID and increment it
        int maxId = 0;
        for (Teacher teacher : teacherList) {
            if (teacher.getId() > maxId) {
                maxId = teacher.getId();
            }
        }
        return maxId + 1;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // Handle new menu items
        if (itemId == R.id.menu_add) {
            // Navigate to AddCoursFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddCoursFragment())
                    .commit();
            return true;

        } else if (itemId == R.id.menu_search) {
            // Implement search functionality here
            Toast.makeText(this, "Search selected", Toast.LENGTH_SHORT).show();
            return true;

        } else if (itemId == R.id.menu_list_all) {
            // Navigate to ListeCoursFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ListeCoursFragment())
                    .commit();
            return true;

        } else if (itemId == R.id.menu_clear_list) {
            // Clear the list or implement logic to handle this
            Toast.makeText(this, "Clear List selected", Toast.LENGTH_SHORT).show();
            return true;

        } else if (itemId == R.id.a_z) {
            // Existing functionality
            teacherAdapter.sortByName(); // Sort teachers A-Z
            return true;

        } else if (itemId == R.id.z_a) {
            teacherAdapter.reverseByName(); // Sort teachers Z-A
            return true;

        } else if (itemId == R.id.add) {
            showAddingDialog(); // Add new teacher
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void showAddingDialog() {
        MyDialogFragment dialog = new MyDialogFragment();
        dialog.show(getSupportFragmentManager(), "my_dialog");
    }

    @Override
    public void onBackPressed() {
        if (bind.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            bind.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setTeacherAdapter(TeacherAdapter adapter, List<Teacher> list) {
        this.teacherAdapter = adapter;
        this.teacherList = list;
    }

    public void addTeacherToList(Teacher teacher) {
        if (teacherAdapter != null) {
            teacherList.add(teacher);
            teacherAdapter.notifyDataSetChanged();
        }
    }
}
