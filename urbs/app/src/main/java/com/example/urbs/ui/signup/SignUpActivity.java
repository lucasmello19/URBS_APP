package com.example.urbs.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.urbs.R;
import com.example.urbs.data.model.User;
import com.example.urbs.service.ApiManager;

public class SignUpActivity extends AppCompatActivity {

    private ApiManager apiManager;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText birthdayEditText;
    private EditText phoneEditText;
    private EditText cpfEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;

    private Button signButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiManager = new ApiManager();

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        birthdayEditText = findViewById(R.id.birthday);
        phoneEditText = findViewById(R.id.phone);
        cpfEditText = findViewById(R.id.cpf);
        passwordEditText = findViewById(R.id.password);
        passwordConfirmEditText = findViewById(R.id.confirmpassword);

        signButton = findViewById(R.id.signup);


        User user = new User("John Doe",
                "johndoe1@example.com",
                "1990-01-01",
                "41993456780",
                "password123",
                "34589677024");

//        // Fazendo a chamada de API para registrar o usuário
//        apiManager.registerUser(user, new ApiManager.ApiCallback<Void>() {
//            @Override
//            public void onSuccess(Void result) {
//                // Usuário registrado com sucesso
//                Log.d("MainActivity", "Usuário registrado com sucesso");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e("MainActivity", "Falha ao registrar o usuário: " + t.getMessage());
//            }
//        });

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}