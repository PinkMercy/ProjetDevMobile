package iset.dsi.tp7;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import iset.dsi.tp7.databinding.ActivityMainBinding;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding bind;
    private boolean enseig = false;
    private boolean about = false;
    private boolean cours = false;
    private boolean Name = false;
    private TeacherAdapter teacherAdapter;
    private List<Teacher> teacherList;

    private DrawerLayout drawerLayout;
    FloatingActionButton fab;
    private MediaPlayer mediaPlayer;





    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Course Notifications";
            String description = "Notifications for new courses";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("course_channel", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBackgroundMusic();


        createNotificationChannel();



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        fab = findViewById(R.id.fab);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
//                R.string.open_nav, R.string.close_nav);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        View header = navigationView.getHeaderView(0);
        ImageView navImage = (ImageView) header.findViewById(R.id.navImage);
        TextView navName = (TextView) header.findViewById(R.id.navName);
        TextView navEmail = (TextView) header.findViewById(R.id.navEmail);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getUser();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No Profile Details", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                navName.setText(""+cursor.getString(0));
                navEmail.setText(""+cursor.getString(1));
                byte[] imageByte = cursor.getBlob(2);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
                navImage.setImageBitmap(bitmap);
            }
        }


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
    // Play background music
    private void playBackgroundMusic() {
        // Create MediaPlayer instance and set music source
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music); // Make sure the music file is in res/raw folder
        mediaPlayer.setLooping(true); // Loop the music
        mediaPlayer.start(); // Start the music
    }

    // Stop music when the activity is paused or destroyed
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause(); // Pause the music when the activity is paused
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release the MediaPlayer resources when the activity is destroyed
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_about, menu); // Default menu
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
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
            Name= false;
            invalidateOptionsMenu();
            selectedFragment = new HomeFragment();
        } else if (item.getItemId() == R.id.nav_cours) {
            bind.toolbar.setTitle("Gérer Cours");
            enseig = false;
            cours = true;
            about = false;
            Name= false;
            invalidateOptionsMenu();
            selectedFragment = new AddCoursFragment(); // Fragment to add courses
        }else if (item.getItemId() == R.id.nav_home) {
            bind.toolbar.setTitle("homee");
            enseig = false;
            cours = true;
            about = false;
            Name= false;
            invalidateOptionsMenu();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            selectedFragment = new AddCoursFragment(); // Fragment to add courses
        } else if (item.getItemId() == R.id.navName) {
            //bind.toolbar.setTitle("Gérer Cours");
            enseig = false;
            cours = false;
            about = false;
            Name= true;
            invalidateOptionsMenu();
            selectedFragment = new AddCoursFragment(); // Fragment to add courses
        } else if (item.getItemId() == R.id.nav_liste_cours) {
            bind.toolbar.setTitle("Liste des Cours");
            enseig = false;
            cours = true;
            about = false;
            Name= false;
            invalidateOptionsMenu();
            selectedFragment = new ListeCoursFragment(); // Fragment to list courses
        } else if (item.getItemId() == R.id.nav_about) {
            bind.toolbar.setTitle("About");
            enseig = false;
            cours = false;
            about = true;
            Name= false;
            invalidateOptionsMenu();
            selectedFragment = new AboutFragment();
        }else if (item.getItemId() == R.id.nav_settings) {
            bind.toolbar.setTitle("Settings");
            enseig = false;
            cours = false;
            about = true;
            Name= false;
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
