package com.example.assignment3.entity.relationship;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.assignment3.entity.User;
import com.example.assignment3.entity.Movement;

import java.util.List;

public class UserWithMovements {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "uid",
            entityColumn = "userId"
    )
    public List<Movement> movements;
}
