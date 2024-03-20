package com.example.urbs.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.urbs.R;
import com.example.urbs.data.model.User;
import com.example.urbs.service.ApiManager;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;

import android.widget.Toast;
import com.google.i18n.phonenumbers.NumberParseException;

public class SignUpActivity extends AppCompatActivity {

    private ApiManager apiManager;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText birthdayEditText;

    private EditText phoneEditText;
    private PhoneNumberUtil phoneNumberUtil;

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
        birthdayEditText.setFocusable(false);
        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        phoneEditText = findViewById(R.id.phone);
        phoneEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneNumberUtil = PhoneNumberUtil.getInstance();

        cpfEditText = findViewById(R.id.cpf);
        passwordEditText = findViewById(R.id.password);
        passwordConfirmEditText = findViewById(R.id.confirmpassword);

        signButton = findViewById(R.id.signup);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onValidateEmail(v)) {
                    return;
                }
                if (!onValidatePhoneNumber(v)) {
                    return;
                }
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();

                User userFormat = new User(name,
                        email,
                        "1990-01-01",
                        "41993456780",
                        "password123",
                        "34589677024");

                User user = new User("John Doe",
                        "johndoe1@example.com",
                        "1990-01-01",
                        "41993456780",
                        "password123",
                        "34589677024");

//                apiManager.registerUser(user, new ApiManager.ApiCallback<Void>() {
//                    @Override
//                    public void onSuccess(Void result) {
//                        // Usuário registrado com sucesso
//                        Log.d("MainActivity", "Usuário registrado com sucesso");
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        Log.e("MainActivity", "Falha ao registrar o usuário: " + t.getMessage());
//                    }
//                });
            }
        });
    }

    public boolean onValidateEmail(View view) {
        String email = emailEditText.getText().toString().trim();

        if (isValidEmail(email)) {
            Toast.makeText(this, "Email válido: " + email, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public Boolean onValidatePhoneNumber(View view) {
        String phoneNumber = phoneEditText.getText().toString();

        try {
            phoneNumber = phoneNumberUtil.format(phoneNumberUtil.parse(phoneNumber, "BR"), PhoneNumberUtil.PhoneNumberFormat.E164);

            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumber, "BR"));
            if (isValid) {
                Toast.makeText(this, "Número de telefone válido: " + phoneNumber, Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "Número de telefone inválido", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao validar o número de telefone", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        birthdayEditText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
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