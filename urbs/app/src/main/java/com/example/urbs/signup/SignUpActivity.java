package com.example.urbs.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.urbs.R;
import com.example.urbs.data.model.User;
import com.example.urbs.service.ApiManager;

public class SignUpActivity extends AppCompatActivity {

    private ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiManager = new ApiManager();

        // Criando um objeto User com dados de exemplo
        User user = new User("John Doe",
                "johndoe1@example.com",
                "1990-01-01",
                "41993456780",
                "password123",
                "34589677024");

        // Fazendo a chamada de API para registrar o usuário
        apiManager.registerUser(user, new ApiManager.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Usuário registrado com sucesso
                Log.d("MainActivity", "Usuário registrado com sucesso");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("MainActivity", "Falha ao registrar o usuário: " + t.getMessage());
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Ação quando a seta de retorno é pressionada
            onBackPressed(); // Isso pode ser usado para fechar a SegundaActivity e voltar para a PrimeiraActivity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}