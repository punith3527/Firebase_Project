package com.example.project4.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project4.MainActivity;
import com.example.project4.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
  private EditText email, password;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_activity);

    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

    email = findViewById(R.id.username_input);
    password = findViewById(R.id.pass);
    TextView forgot = findViewById(R.id.forgot);
    Button login = findViewById(R.id.loginBtn);

    mAuth = FirebaseAuth.getInstance();

    //Login Button
    login.setOnClickListener(v -> {
      String txt_email = email.getText().toString();
      String txt_password = password.getText().toString();

      if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
        Toast.makeText(LoginActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
      } else {
        loginUser(txt_email, txt_password);
      }
    });

    //Forgot Button
    forgot.setOnClickListener(v -> Toast.makeText(LoginActivity.this, "Please Contact Admin", Toast.LENGTH_SHORT).show());

  }

  private void loginUser(String email, String password) {

    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
      }
    }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
  }
}
