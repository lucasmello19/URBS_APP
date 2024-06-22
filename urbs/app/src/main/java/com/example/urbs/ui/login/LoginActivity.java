package com.example.urbs.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.urbs.R;
import com.example.urbs.service.ApiManager;
import com.example.urbs.ui.Line.LinesActivity;
import com.example.urbs.ui.signup.SignUpActivity;
import com.example.urbs.ui.map.MapsActivity;
import com.example.urbs.utils.AccessTokenManager;

public class LoginActivity extends AppCompatActivity {
    private ApiManager apiManager;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("");

        apiManager = new ApiManager(LoginActivity.this);

        usernameEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button signupButton = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progress);

        progressBar.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Solicita a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Permissão já concedida, faça o que for necessário
            iniciarLocalizacao();
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (!onValidateEmail(v)) {
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Senha inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);

                apiManager.loginUser(LoginActivity.this, name, password, new ApiManager.ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        String accessToken = AccessTokenManager.getInstance(LoginActivity.this).getAccessToken();

                        if (accessToken != null) {
                            Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, LinesActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        loginButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
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

        String accessToken = AccessTokenManager.getInstance(LoginActivity.this).getAccessToken();

        if (accessToken != null) {
            Intent intent = new Intent(LoginActivity.this, LinesActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Se a solicitação de permissão foi cancelada, o array de resultados estará vazio
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, inicia a localização
                iniciarLocalizacao();

                // Envia um broadcast informando que a permissão foi concedida
                Intent intent = new Intent("com.example.urbs.PERMISSION_GRANTED");
                sendBroadcast(intent);
            } else {
                // Permissão negada, explique para o usuário ou desative recursos que dependem dessa permissão
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarLocalizacao() {
        // Inicie a lógica para obter a localização do usuário aqui
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
