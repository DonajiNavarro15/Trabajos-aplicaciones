package com.example.formulario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText userName, userDate, userPhoneNumber, userEmail, userPassword, passwordConfirm;
    RadioButton userSexFem, userSexMasc;
    Button cleanFields, saveInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeElementsOfView();
        validateFormFields();

    }
    private void initializeElementsOfView(){
        userName = findViewById(R.id.user_name);
        userDate = findViewById(R.id.user_dateBorn);
        userSexFem = findViewById(R.id.sex_fem);
        userSexMasc = findViewById(R.id.sex_masc);
        userPhoneNumber = findViewById(R.id.user_phoneNumber);
        userEmail = findViewById(R.id.user_email);
        userPassword =  findViewById(R.id.user_password);
        passwordConfirm = findViewById(R.id.user_confirmPassword);
        cleanFields = findViewById(R.id.btnclean_fields);
        saveInformation = findViewById(R.id.btnsave_information);
        validatePhoneNumber();
        userDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePickerDialog();
            }
        });
        discardInformation();
    }
    private void validateFormFields(){
        saveInformation.setOnClickListener(v -> {
            if(isAnyFieldEmpty(
                    userName,
                    userDate,
                    userEmail,
                    userPassword,
                    passwordConfirm) ||
                    (!genderSelected())
            ){
                showMessage("Please complete the fields marked as required");
                return;
            }
            if(!validateEmail(userEmail.getText().toString())){
                showMessage("Please enter a valid email address");
                return;
            }
            if(!validatePasswordMatching()){
               showMessage("The password entered does not match, please verify it");
               return;
            }
            else{
                showMessage("information saved successfully");
                clearFields();
            }
        });
    }
    private boolean isAnyFieldEmpty (EditText... editTexts){
        for(EditText editText : editTexts){
            if(TextUtils.isEmpty(editText.getText().toString())) {
                return true;
            }
        }
        return false;
    }
    private void showMessage(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean genderSelected(){
        boolean isFemaleChecked = userSexFem.isChecked();
        boolean isMaleChecked = userSexMasc.isChecked();
        return isFemaleChecked != isMaleChecked;
    }
    private boolean validateEmail(String email){
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }
    private boolean validatePasswordMatching(){
        String password = userPassword.getText().toString();
        String confirmPassword = passwordConfirm.getText().toString();
        return password.equals(confirmPassword);
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String selectedDate = dayOfMonth + "/" + month + "/" + year;
                        userDate.setText(selectedDate);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void validatePhoneNumber(){
        int MAX_PHONE_NUMBER_LENGTH = 10;
        userPhoneNumber.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(MAX_PHONE_NUMBER_LENGTH),
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        int keep = MAX_PHONE_NUMBER_LENGTH - (dest.length() - (dend - dstart));
                        if (keep <= 0) {
                            return "";
                        } else {
                            StringBuilder sb = new StringBuilder();
                            for (int i = start; i < end && sb.length() < keep; i++) {
                                char c = source.charAt(i);
                                if (Character.isDigit(c)) {
                                    sb.append(c);
                                }
                            }
                            return sb.toString();
                        }
                    }
                }
        });
    }
    private void clearFields() {
        userName.setText("");
        userDate.setText("");
        userSexFem.setChecked(false);
        userSexMasc.setChecked(false);
        userPhoneNumber.setText("");
        userEmail.setText("");
        userPassword.setText("");
        passwordConfirm.setText("");
    }
    private void discardInformation(){
        cleanFields.setOnClickListener(v -> {
            clearFields();
        });
    }
}