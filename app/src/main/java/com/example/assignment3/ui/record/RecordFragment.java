package com.example.assignment3.ui.record;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;
import com.example.assignment3.adapter.MovementAdapter;
import com.example.assignment3.databinding.FragmentRecordBinding;
import com.example.assignment3.entity.Movement;
import com.example.assignment3.entity.User;
import com.example.assignment3.entity.relationship.UserWithMovements;
import com.example.assignment3.viewmodel.UserViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class RecordFragment extends Fragment {

    private ArrayList<Movement> recordList;
    private FragmentRecordBinding binding;
    private Button addRecordButtonDialog;
    private RecyclerView recyclerView;
    private MovementAdapter adapter;
    private UserViewModel userViewModel;
    private View root;

    User user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // User
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getParcelable("loginUser");

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();
        root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_record,container,false);

        //
        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(UserViewModel.class);

        // Recycler View
        setMovementInfo();
        recyclerView = root.findViewById(R.id.recordRecycle);
        setAdapter();


        // Add Record
        addRecordButtonDialog = root.findViewById(R.id.addRecordButton);
        addRecordButtonDialog.setOnClickListener(view -> {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();

            //If statement for card display
            if (recordList.size() != 0) {
                if (recordList.get(0).getTime().equals(dateFormat.format(date))) {
                    Toast.makeText(getActivity(), "Already exists!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Dialog dialog = new Dialog(root.getContext());
            dialog.setContentView(R.layout.layout_add_record);
            TextView dateTxt = dialog.findViewById(R.id.dateTextView);

            dateTxt.setText(dateFormat.format(date));
            EditText editDistance = dialog.findViewById(R.id.addRecordDistance);
            Button confirmButton = dialog.findViewById(R.id.confirmAddRecord);
            ImageView exitButton = dialog.findViewById(R.id.exitButton);

            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    return;
                }
            });

            confirmButton.setOnClickListener(view1 -> {
                String distance;
                String todayDate;
                distance = editDistance.getText().toString();
                todayDate = dateTxt.getText().toString();
                long distanceNum = Long.parseLong(distance);

                // Database
                Movement movement = new Movement(user.getUid(),todayDate,distanceNum);
                userViewModel.insertMovement(movement);

                // Recycler
                if (recordList.size() == 0){
                    recordList.add(new Movement(user.getUid(), todayDate, distanceNum));
                    adapter.notifyItemInserted(0);
                }
                else{
                    recordList.add(recordList.size()-1, new Movement(user.getUid(), todayDate, distanceNum));
                    adapter.notifyItemInserted(recordList.size()-1);
                }


                recyclerView.scrollToPosition(0);

                dialog.dismiss();

            });
            dialog.show();
        });

        //final TextView textView = binding.textRecord;
        //recordViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;

    }

    private void setAdapter() {
        adapter = new MovementAdapter(recordList, userViewModel,user);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setMovementInfo() {
        recordList = new ArrayList<Movement>();

        userViewModel.getMovementByEmail(user.getEmail()).observe(getActivity(), userWithMovements -> {
            recordList = new ArrayList<Movement>();
            for (UserWithMovements temp : userWithMovements){
                for (Movement temp2 : temp.movements){
                    Movement test1 = new Movement(temp2.getUserId(),temp2.getTime(),temp2.getMovement());
                    recordList.add(test1);
                    //Collections.reverse(recordList);
                }
            }
            setAdapter();
    });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
