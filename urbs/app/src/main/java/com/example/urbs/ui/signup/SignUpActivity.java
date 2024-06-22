package com.example.urbs.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SignUpActivity extends AppCompatActivity {

    private ApiManager apiManager;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText birthdayEditText;

    private EditText phoneEditText;
    private PhoneNumberUtil phoneNumberUtil;

    private EditText cpfEditText;
    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");

    private EditText passwordEditText;
    private EditText passwordConfirmEditText;

    private Button signButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("URBS - Cadastro");

        apiManager = ApiManager.getInstance(this);

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
        cpfEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        cpfEditText.addTextChangedListener(new CPFFormatter());

        passwordEditText = findViewById(R.id.password);
        passwordConfirmEditText = findViewById(R.id.confirmpassword);

        signButton = findViewById(R.id.signup);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String birthday = converterData(birthdayEditText.getText().toString());
                String cpf = cpfEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = passwordConfirmEditText.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Nome inválido", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!onValidateEmail(v)) {
                    return;
                }
                if (!onValidatePhoneNumber(v)) {
                    return;
                }
                if (birthday.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Data de nascimento inválida", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!isValidCPF(cpf)) {
//                    Toast.makeText(SignUpActivity.this, "CPF inválido", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Senha inválida", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirm)) {
                    Toast.makeText(SignUpActivity.this, "Senha de confirmação inválida", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(name,
                        email,
                        birthday,
                        phone,
                        password,
                        cpf);

                apiManager.registerUser(user, new ApiManager.ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(SignUpActivity.this, "Cadatro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static String converterData(String dataBrasileira) {
        // Formato da data brasileira
        SimpleDateFormat formatoBrasileiro = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Parse da data brasileira para objeto Date
            Date data = formatoBrasileiro.parse(dataBrasileira);

            // Formato da data americana
            SimpleDateFormat formatoAmericano = new SimpleDateFormat("yyyy-MM-dd");

            // Convertendo a data para o formato americano e retornando como string
            return formatoAmericano.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    private boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (caso de CPFs inválidos)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int[] numbers = new int[11];
        for (int i = 0; i < 11; i++) {
            numbers[i] = Integer.parseInt(cpf.substring(i, i + 1));
        }

        // Calcula o primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += numbers[i] * (10 - i);
        }
        int firstVerifier = 11 - (sum % 11);
        if (firstVerifier >= 10) {
            firstVerifier = 0;
        }

        if (numbers[9] != firstVerifier) {
            return false;
        }

        // Calcula o segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += numbers[i] * (11 - i);
        }
        int secondVerifier = 11 - (sum % 11);
        if (secondVerifier >= 10) {
            secondVerifier = 0;
        }

        return numbers[10] == secondVerifier;
    }

    private class CPFFormatter implements TextWatcher {
        private boolean isUpdating = false;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isUpdating) {
                isUpdating = false;
                return;
            }

            String str = s.toString().replaceAll("[^0-9]", "");

            if (str.length() > 3 && str.length() <= 6) {
                str = str.substring(0, 3) + "." + str.substring(3);
            } else if (str.length() > 6 && str.length() <= 9) {
                str = str.substring(0, 3) + "." + str.substring(3, 6) + "." + str.substring(6);
            } else if (str.length() > 9) {
                str = str.substring(0, 3) + "." + str.substring(3, 6) + "." + str.substring(6, 9) + "-" + str.substring(9);
            }

            isUpdating = true;
            cpfEditText.setText(str);
            cpfEditText.setSelection(str.length());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public boolean onValidateEmail(View view) {
        String email = emailEditText.getText().toString().trim();

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


    public Boolean onValidatePhoneNumber(View view) {
        String phoneNumber = phoneEditText.getText().toString();

        try {
            phoneNumber = phoneNumberUtil.format(phoneNumberUtil.parse(phoneNumber, "BR"), PhoneNumberUtil.PhoneNumberFormat.E164);

            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumber, "BR"));
            if (isValid) {
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