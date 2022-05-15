package com.example.assignment3.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// version 1.0.5 : movement --- Lichen
@Entity(tableName = "Movement")
public class Movement {

    @PrimaryKey(autoGenerate = true)
    private int mid;

    private int userId;
    private String time;
    private long movement;

    public Movement(int userId, String time, long movement) {
        this.userId = userId;
        this.time = time;
        this.movement = movement;
    }

    public int getMid() { return mid; }

    public void setMid(int mid) { this.mid = mid; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public long getMovement() { return movement; }

    public void setMovement(long movement) { this.movement = movement; }

    @Override
    public String toString() {
        return "Movement{" +
                "mid=" + mid +
                ", userId=" + userId +
                ", time='" + time + '\'' +
                ", movement=" + movement +
                '}';
    }
}
