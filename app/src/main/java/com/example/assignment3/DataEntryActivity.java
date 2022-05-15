package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.databinding.DataEntryBinding;
import com.example.assignment3.entity.User;

//Version 1.0.3: data entry --- Lichen
public class DataEntryActivity extends AppCompatActivity {
    private DataEntryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataEntryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Picker
        final String[] roles = {"User", "Gym"};
        binding.numberPicker.setMinValue(0);
        binding.numberPicker.setMaxValue(roles.length - 1);
        binding.numberPicker.setWrapSelectorWheel(true);
        binding.numberPicker.setDisplayedValues(roles);
        // Clear button
        binding.clearButton.setOnClickListener(v -> {
            binding.nameEditText.setText("");
            binding.streetEditText.setText("");
            binding.suburbEditText.setText("");
            binding.stateEditText.setText("");
            String msg = "Clear All";
            toastMsg(msg);
        });
        // return to login screen
        binding.leaveButton.setOnClickListener(v -> startActivity(new Intent(DataEntryActivity.this, LoginActivity.class)));
        // Create button --- pass user object to check page
        binding.createButton.setOnClickListener(v -> {
            Bundle bundle = getIntent().getExtras();
            User user = bundle.getParcelable("user");
            int role_id = binding.numberPicker.getValue();
            String role_txt = roles[role_id];
            String name_txt = binding.nameEditText.getText().toString();
            String address_txt_1 = binding.streetEditText.getText().toString();
            String address_txt_2 = binding.suburbEditText.getText().toString();
            String address_txt_3 = binding.stateEditText.getText().toString();
            //validation
            if (TextUtils.isEmpty(role_txt) || TextUtils.isEmpty(name_txt) ||
                    TextUtils.isEmpty(address_txt_1) || TextUtils.isEmpty(address_txt_2) || TextUtils.isEmpty(address_txt_3)) {
                String msg = "Please enter your information";
                toastMsg(msg);
            } else {
                user.setRole(role_txt);
                user.setName(name_txt);
                String address_txt = address_txt_1 + ", " + address_txt_2 + ", " + address_txt_3;
                user.setAddress(address_txt);
                addUserData(user);
            }
        });
    }

    private void addUserData(User user) {
        // complete a User object and pass it to next view
        Intent intent = new Intent(DataEntryActivity.this, RegistrationCheckActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("userInfo", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
