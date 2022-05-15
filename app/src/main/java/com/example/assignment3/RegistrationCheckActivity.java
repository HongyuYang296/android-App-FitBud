package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.databinding.RegistrationCheckBinding;
import com.example.assignment3.entity.User;
import com.example.assignment3.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

//Version 1.0.3: data entry --- Lichen
public class RegistrationCheckActivity extends AppCompatActivity {
    private RegistrationCheckBinding binding;
    private FirebaseAuth auth;
    //DatabaseReference myRef;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegistrationCheckBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        // Display user object
        Bundle bundle = getIntent().getExtras();
        User user = bundle.getParcelable("userInfo");
        binding.emailTextView.setText(user.getEmail());
        binding.passwordTextView.setText(user.getPassword());
        binding.roleTextView.setText(user.getRole());
        binding.nameTextView.setText(user.getName());
        binding.addressTextView.setText(user.getAddress());
        // Use database in Singapore... so URL is needed
        //FirebaseDatabase database = FirebaseDatabase.getInstance("https://as3-5046-default-rtdb.asia-southeast1.firebasedatabase.app/");
        //myRef = database.getReference("User");
        // Room database
        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);
        // return to login screen
        binding.redoButton.setOnClickListener(v -> startActivity(new Intent(RegistrationCheckActivity.this, LoginActivity.class)));
        binding.createButton.setOnClickListener(v -> registerUser(user));

    }

    private void registerUser(User user) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String msg = "Registration Successful";
                toastMsg(msg);
                // update to database
                userViewModel.insertUser(user);
                // update to firebase --- use push() to auto generate the child nodes
                //myRef.push().setValue(user);

                Intent intent = new Intent(RegistrationCheckActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                String msg = "Registration Unsuccessful, Please try another email";
                toastMsg(msg);
            }
        });
    }

    public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
