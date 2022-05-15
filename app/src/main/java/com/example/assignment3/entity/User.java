package com.example.assignment3.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Version 1.0.3: data entry --- Lichen
@Entity(tableName = "User")
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String email;
    private String password;
    private String role;
    private String name;
    public String address;

    public User(Parcel in) {
        this.uid = in.readInt();
        this.email = in.readString();
        this.password = in.readString();
        this.role = in.readString();
        this.name = in.readString();
        this.address = in.readString();
    }

    public User(String email, String password, String role, String name, String address) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.address = address;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(uid);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(role);
        parcel.writeString(name);
        parcel.writeString(address);
    }

    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
