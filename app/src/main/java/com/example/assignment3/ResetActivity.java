package com.example.assignment3;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.databinding.ResetPasswordBinding;
import com.example.assignment3.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Almost same as signup activity

public class ResetActivity extends AppCompatActivity {
    private ResetPasswordBinding binding;
    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ResetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);
        // return to login screen
        binding.leaveButton.setOnClickListener(v -> startActivity(new Intent(ResetActivity.this, LoginActivity.class)));

        binding.resetButton.setOnClickListener(v -> {
            // Get Text
            String email_txt = binding.emailEditText.getText().toString();
            String password_txt_1 = binding.passwordEditText.getText().toString();
            String password_txt_2 = binding.confirmPasswordEditText.getText().toString();
            // validation
            if (TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt_1) || TextUtils.isEmpty(password_txt_2)) {
                String msg = "Please enter your information";
                toastMsg(msg);
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email_txt).matches()) {
                String msg = "Please enter an valid email";
                toastMsg(msg);
            } else if (!isValidPassword(password_txt_1)) {
                String msg = "Invalid password format";
                toastMsg(msg);
            } else if (!password_txt_2.equals(password_txt_1)) {
                String msg = "Please confirm your password";
                toastMsg(msg);
            } else
                resetPassword(email_txt, password_txt_1);
        });
    }

    private void resetPassword(String email_txt, String password_txt_1) {
        userViewModel.findByEmail(email_txt).observe(this, user -> {
            String msg;
            if (user != null) {
                msg = "Reset success";
                user.setPassword(password_txt_1);
                userViewModel.updateUser(user);
                firebaseUser.updatePassword(password_txt_1);
                auth.updateCurrentUser(firebaseUser);
                startActivity(new Intent(ResetActivity.this, LoginActivity.class));
            } else {
                msg = "No such user, please check your email again!";
                binding.emailEditText.setText("");
                binding.passwordEditText.setText("");
                binding.confirmPasswordEditText.setText("");
            }
            toastMsg(msg);
        });
    }

    public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isValidPassword(final String password) {
        //validation
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[@#$%^&+=!?])" +  //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
