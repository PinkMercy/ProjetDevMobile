package iset.dsi.tp7;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText nameEditTextId,emailEditTextId,usernameEditTextId,passwordEditTextId;
    private Button signUpButtonId;
    DatabaseHelper myDatabaseHelper;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nameEditTextId = findViewById(R.id.nameEditTextId);
        emailEditTextId = findViewById(R.id.emailEditTextId);
        usernameEditTextId = findViewById(R.id.usernameEditTextId);
        passwordEditTextId = findViewById(R.id.passwordEditTextId);
        signUpButtonId = findViewById(R.id.signUpButtonId);

        myDatabaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        userData = new UserData();
        signUpButtonId.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String name = nameEditTextId.getText().toString().trim();
        String email = emailEditTextId.getText().toString().trim();
        String username = usernameEditTextId.getText().toString().trim();
        String password = passwordEditTextId.getText().toString().trim();
        nameEditTextId.setText("");
        emailEditTextId.setText("");
        usernameEditTextId.setText("");
        passwordEditTextId.setText("");

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please insert your information! then try", Toast.LENGTH_SHORT).show();
        } else {

            userData.setName(name);
            userData.setEmail(email);
            userData.setUsername(username);
            userData.setPassword(password);
            long rowid = myDatabaseHelper.insertData(userData);
            if (rowid>0) Toast.makeText(this, " Sign-up Successful", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "Sign-up is Unsuccessful!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },500);
        }

    }
}
