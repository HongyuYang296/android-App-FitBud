package com.example.assignment3.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assignment3.entity.Movement;
import com.example.assignment3.entity.User;
import com.example.assignment3.entity.relationship.UserWithMovements;
import com.example.assignment3.repository.UserRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository uRepository;
    private final LiveData<List<User>> allUsers;

    public UserViewModel(Application application) {
        super(application);
        uRepository = new UserRepository(application);
        allUsers = uRepository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public LiveData<User> findByEmail(final String email) {
        return uRepository.findByEmail(email);
    }

    public LiveData<List<UserWithMovements>> getMovementByEmail(final String email) { return uRepository.getMovementByEmail(email); }

    public LiveData<List<Movement>> getMovementById(final int userID) { return uRepository.getMovementByID(userID); }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Movement> checkDailyEntry(final int id, final String date) { return uRepository.checkDailyEntry(id, date); }

    public void deleteByRecord(final int uid, final String date, final long distance) { uRepository.deleteByRecord(uid, date, distance); }

    public void editDistanceByRecord(final int uid, final String date, final long distance, final long newDistance) {
        uRepository.editDistanceByRecord(uid, date, distance, newDistance);
    }

    public void insertUser(User user) {
        uRepository.insertUser(user);
    }

    public void insertMovement(Movement movement) {
        uRepository.insertMovement(movement);
    }

    public void deleteUser(User user) { uRepository.deleteUser(user); }

    public void deleteMovement(Movement movement) { uRepository.deleteMovement(movement); }

    public void updateUser(User user) {
        uRepository.updateUser(user);
    }

    public void updateMovement(Movement movement) {
        uRepository.updateMovement(movement);
    }

    public void deleteAllUser() {
        uRepository.deleteAllUser();
    }

    public void deleteAllMovement() {
        uRepository.deleteAllMovement();
    }

}
