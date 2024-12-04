package iset.dsi.tp7;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

        DatabaseHelper myDatabaseHelper;
        private TextView signUpPageId;
        private TextInputEditText usernameEditTextId,passwordEditTextId;
        private Button loginButtonId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            signUpPageId = findViewById(R.id.signUpPageId);
            usernameEditTextId = findViewById(R.id.usernameEditTextId);
            passwordEditTextId = findViewById(R.id.passwordEditTextId);
            loginButtonId = findViewById(R.id.loginButtonId);

            myDatabaseHelper = new DatabaseHelper(this);
            SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

            loginButtonId.setOnClickListener(this);
            signUpPageId.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (v.getId()==R.id.signUpPageId) {
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
            }else {

                String username = usernameEditTextId.getText().toString().trim();
                String password = passwordEditTextId.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "filed is empty!", Toast.LENGTH_SHORT).show();

                }else {
                    boolean result = myDatabaseHelper.findUsers(username,password);
                    if (result) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else Toast.makeText(this, "user and password didn't match", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }
