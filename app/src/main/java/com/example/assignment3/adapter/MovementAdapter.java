package com.example.assignment3.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.assignment3.MainActivity;
import com.example.assignment3.R;
import com.example.assignment3.dao.UserDao;
import com.example.assignment3.entity.Movement;
import com.example.assignment3.entity.User;
import com.example.assignment3.entity.relationship.UserWithMovements;
import com.example.assignment3.repository.UserRepository;
import com.example.assignment3.viewmodel.UserViewModel;

import java.util.Collections;
import java.util.List;

public class MovementAdapter extends RecyclerView.Adapter<MovementAdapter.MyViewHolder>{
    private List<Movement> records;
    private UserViewModel userViewModel;
    private User user;
    private long oldDistance;

    public MovementAdapter(List<Movement> records, UserViewModel userViewModel, User user) {

        this.records = records;
        Collections.reverse(this.records);
        this.userViewModel = userViewModel;
        this.user = user;

    }

    @NonNull
    @Override
    public MovementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_record_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovementAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String date = records.get(position).getTime();
        holder.dateText.setText(date);
        long distance = records.get(position).getMovement();
        String distanceString = String.valueOf(distance);
        holder.distanceText.setText(distanceString + " metres");


        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Record")
                        .setMessage("Are you sure you want to delete?")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Movement move1  =  new Movement(111,"13/05/2022",200);

                                // Add code to remove record
                                userViewModel.deleteByRecord(user.getUid(),date,distance);

                                records.remove(position);
                                notifyItemRemoved(position);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();

            }
        });

        holder.llCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.layout_add_record);

                TextView dateUpdateText = dialog.findViewById(R.id.dateTextView);
                EditText editDistance = dialog.findViewById(R.id.addRecordDistance);
                Button confirmUpdate = dialog.findViewById(R.id.confirmAddRecord);


                dateUpdateText.setText(records.get(position).getTime());
                editDistance.setText(String.valueOf((records.get(position)).getMovement()));
                oldDistance = Long.parseLong(editDistance.getText().toString());

                ImageView exitButton = dialog.findViewById(R.id.exitButton);

                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        return;
                    }
                });

                confirmUpdate.setText("Update");
                confirmUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String date = "";
                        long newDistance = 0;


                        date = dateUpdateText.getText().toString();
                        newDistance = Long.parseLong(editDistance.getText().toString());


                        // Add code to edit movement record
                        userViewModel.editDistanceByRecord(user.getUid(),date,oldDistance,newDistance );

                        records.set(position,new Movement(user.getUid(), date,newDistance));
                        notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateText;
        public TextView distanceText;
        public ConstraintLayout llCard;
        public ImageView deleteIcon;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            dateText = itemView.findViewById(R.id.recordCardDate);
            distanceText = itemView.findViewById(R.id.recordCardDistance);
            llCard = itemView.findViewById(R.id.llCard);
            deleteIcon = itemView.findViewById(R.id.recordCardDelete);
        }
    }


}
