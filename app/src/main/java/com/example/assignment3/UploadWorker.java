package com.example.assignment3;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.assignment3.entity.User;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

public class UploadWorker extends Worker {
    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Result doWork() {

        String userJsonString = getInputData().getString("UserTest");
        System.out.println(userJsonString);
        if (userJsonString != null) {

            System.out.println("getInputData(): Receiving JsonString ...");
            // upload to Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://as3-5046-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference databaseReference = database.getReference("User");
            System.out.println("FirebaseDatabase.getInstance(): Connecting to firebase ...");

            Gson gson = new Gson();
            Type userMapType = new TypeToken<Map<String, User>>() {
            }.getType();

            Map<String, User> userMap = gson.fromJson(userJsonString, userMapType);
            System.out.println("Preparing upload data...");

            databaseReference.setValue(userMap);
            System.out.println("setValue(): Writing data to firebase...");
        }
        return Result.success();
    }
}

