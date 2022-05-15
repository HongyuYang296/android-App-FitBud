package com.example.assignment3.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.assignment3.entity.User;
import com.example.assignment3.entity.Movement;
import com.example.assignment3.entity.relationship.UserWithMovements;

import java.util.List;

@Dao
public interface UserDao {

    // Get all user
    @Query("SELECT * FROM user ORDER BY uid ASC")
    LiveData<List<User>> getAllUser();

    // Get user by user email
    @Query("SELECT * FROM user WHERE email = :email")
    LiveData<User> findByEmail(String email);

    // Get all user's movement by his email
    @Transaction
    @Query("SELECT * FROM user WHERE email = :email")
    LiveData<List<UserWithMovements>> getMovementByEmail(String email);

    // Get all movements by user id
    @Query("SELECT * FROM movement WHERE userId = :userID")
    LiveData<List<Movement>> getMovementByID(int userID);

    // Get movement by user id and time
    @Query("SELECT * FROM movement WHERE userId = :uid AND time = :date LIMIT 1")
    Movement checkDailyEntry(int uid, String date);

    // Delete movement by it's attributes
    @Query("DELETE FROM Movement WHERE userId = :uid AND time = :date AND movement = :distance")
    void deleteByRecord(int uid, String date, long distance);

    // Update movement's movement by it's attribute
    @Query("UPDATE Movement SET movement = :newDistance WHERE userId = :uid  AND time =:date AND movement = :distance")
    void editDistanceByRecord(int uid, String date, long distance, long newDistance);

    @Insert
    void insertUser(User user);

    @Insert
    void insertMovement(Movement movement);

    @Delete
    void deleteUser(User user);

    @Delete
    void deleteMovement(Movement movement);

    @Update
    void updateUser(User user);

    @Update
    void updateMovement(Movement movement);

    @Query("DELETE FROM user")
    void deleteAllUser();

    @Query("DELETE FROM movement")
    void deleteAllMovement();
}
