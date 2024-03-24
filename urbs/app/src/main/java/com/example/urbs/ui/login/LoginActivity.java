package com.example.urbs.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.urbs.R;
import com.example.urbs.service.ApiManager;
import com.example.urbs.ui.signup.SignUpActivity;
import com.example.urbs.ui.map.MapsActivity;

public class LoginActivity extends AppCompatActivity {
    private ApiManager apiManager;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiManager = new ApiManager();

        usernameEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button signupButton = findViewById(R.id.signup);

        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());

                String name = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (!onValidateEmail(v)) {
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Senha inválido", Toast.LENGTH_SHORT).show();
                    return;
                }


                apiManager.loginUser(name, password, new ApiManager.ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onValidateEmail(View view) {
        String email = usernameEditText.getText().toString().trim();

        if (isValidEmail(email)) {
            return true;
        } else {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
