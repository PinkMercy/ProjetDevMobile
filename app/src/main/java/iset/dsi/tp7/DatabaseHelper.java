package iset.dsi.tp7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDataBase.db";
    private static final int DATABASE_VERSION = 3;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteImage;

    private static final String TABLE_NAME = "user_details";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " VARCHAR(50) NOT NULL, " + EMAIL + " TEXT NOT NULL, " + USERNAME + " TEXT NOT NULL, " + PASSWORD + " INTEGER NOT NULL); ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String TABLE_TEACHER = "Teacher";
    private static final String TABLE_COURS = "Cours";
    private static final String TABLE_USER = "user_details";  // Table for user authentication

    // Teacher table columns
    public static final String COLUMN_TEACHER_ID = "_id";
    public static final String COLUMN_TEACHER_NAME = "name";
    public static final String COLUMN_TEACHER_EMAIL = "email";

    // Cours table columns
    public static final String COLUMN_COURS_ID = "_id";
    public static final String COLUMN_COURS_NAME = "name";
    public static final String COLUMN_COURS_NBHEURE = "nbheure";
    public static final String COLUMN_COURS_TYPE = "type";
    public static final String COLUMN_COURS_ENSEIG_ID = "enseig_id";

    // User table columns (for authentication)
    private static final String COLUMN_USER_ID = "Id";
    private static final String COLUMN_USER_NAME = "Name";
    private static final String COLUMN_USER_EMAIL = "Email";
    private static final String COLUMN_USER_USERNAME = "Username";
    private static final String COLUMN_USER_PASSWORD = "Password";

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " VARCHAR(50) NOT NULL, "
            + COLUMN_USER_EMAIL + " TEXT NOT NULL, "
            + COLUMN_USER_USERNAME + " TEXT NOT NULL, "
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_TEACHER = "CREATE TABLE " + TABLE_TEACHER + " ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL, "
            + "email TEXT);";

    private static final String CREATE_TABLE_COURS = "CREATE TABLE " + TABLE_COURS + " ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL, "
            + "nbheure REAL NOT NULL, "
            + "type TEXT NOT NULL, "
            + "enseig_id INTEGER NOT NULL, "
            + "FOREIGN KEY (enseig_id) REFERENCES Teacher(_id));";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TEACHER);
        db.execSQL(CREATE_TABLE_COURS);
    }
    public boolean findUsers(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Query the database for users with the provided username and password
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME + " = ? AND " + PASSWORD + " = ?", new String[]{username, password});

        boolean result = false;

        if (cursor.getCount() > 0) {
            result = true;  // If there is a matching user
        }

        cursor.close();  // Close the cursor
        db.close();  // Close the database
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURS);
        onCreate(db);
    }

    public long insertData(UserData userData) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, userData.getName());
        contentValues.put(EMAIL, userData.getEmail());
        contentValues.put(USERNAME, userData.getUsername());
        contentValues.put(PASSWORD, userData.getPassword());
        return sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    public long insertCours(String name, float nbHeures, String type, int enseignantId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("nbheure", nbHeures);
        values.put("type", type);
        values.put("enseig_id", enseignantId);
        //return db.insert("Cours", null, values);
        long result = db.insert(TABLE_COURS, null, values);
        Log.d("DatabaseHelper", "Insert result: " + result);
        return result;

    }

    public long insertTeacher(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        return db.insert("Teacher", null, values);
    }


    // Insert a new user
    public long insertUser(String name, String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_PASSWORD, password);

        return db.insert(TABLE_USER, null, values);  // Insert into user_details table
    }

    // Check if the user exists (for login)
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE "
                        + COLUMN_USER_USERNAME + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{username, password});

        boolean result = false;
        if (cursor.getCount() > 0) {
            result = true;  // User found, credentials are valid
        }
        cursor.close();
        return result;
    }

    // Add Teacher
    public void addTeacher(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_NAME, teacher.getName());
        values.put(COLUMN_TEACHER_EMAIL, teacher.getEmail());
        db.insert(TABLE_TEACHER, null, values);
    }


//    // Get all Teachers
//    public List<Teacher> getAllTeachers() {
//        List<Teacher> teacherList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEACHER, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                Teacher teacher = new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
//                teacherList.add(teacher);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return teacherList;
//    }

    // Add Cours
    public long addCours(Cours cours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURS_NAME, cours.getName());
        values.put(COLUMN_COURS_NBHEURE, cours.getNbHeures());
        values.put(COLUMN_COURS_TYPE, cours.getType());
        values.put(COLUMN_COURS_ENSEIG_ID, cours.getTeacherId());
        return db.insert(TABLE_COURS, null, values);
    }

    // Get Cours List
    public List<Cours> getCoursList() {
        List<Cours> coursList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURS, null);

        if (cursor.moveToFirst()) {
            do {
                Cours cours = new Cours(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getString(3), cursor.getInt(4));
                coursList.add(cours);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return coursList;
    }
    public void storeData(ModelClass modelClass){
        SQLiteDatabase database = this.getWritableDatabase();
        Bitmap bitmapImage = modelClass.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byteImage = byteArrayOutputStream.toByteArray();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", modelClass.getName());
        contentValues.put("email", modelClass.getEmail());
        //contentValues.put("image", byteImage);
        long checkQuery = database.insert("user_details", null, contentValues);
        if (checkQuery != -1){
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            database.close();
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor getUser(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from user_details", null);
        return cursor;
    }
    // In DatabaseHelper.java

    public List<Teacher> getAllTeachers() {
        List<Teacher> teacherList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Teacher", null);  // Query to fetch all teachers

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_EMAIL));
                teacherList.add(new Teacher(id, name, email));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teacherList;
    }

}
