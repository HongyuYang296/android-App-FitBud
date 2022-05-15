package com.example.assignment3.ui.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.R;
import com.example.assignment3.databinding.FragmentReportBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// this fragment is for report page, feel free to edit

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;

    // Report Type Input
    String[] reportTypes = {"Bar-Chart","Pie-Chart"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterReportTypes;

    // Start/End Date Input
    private Button startDateButton;
    private Button endDateButton;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { ReportViewModel reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);


        assert container != null;
        container.clearDisappearingChildren();
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Start/End Date Picker
        initStartDatePicker();
        initEndDatePicker();
        startDateButton = root.findViewById(R.id.startDatePickerButton);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePickerDialog.show();
            }
        });
        startDateButton.setText(getTodaysDate());
        endDateButton = root.findViewById(R.id.endDatePickerButton);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePickerDialog.show();
            }
        });
        endDateButton.setText(getTodaysDate());






        Button ID = (Button) root.findViewById(R.id.generate_report);
        Button ID2 = (Button) root.findViewById(R.id.generate_pieChart);
        ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date endDate = convertStringDate(startDateButton.getText().toString());
                Date startDate = convertStringDate(endDateButton.getText().toString());
                System.out.println(endDate);
                System.out.println(startDate);
                if (startDate.after(endDate)) {

                    FragmentManager fragmentManager = getFragmentManager();
                    assert fragmentManager != null;
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    BarChartFragment GenerateReport = new BarChartFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment, GenerateReport);
                    fragmentTransaction.commit();
                    SharedPreferences sharedPref = requireActivity().getSharedPreferences("Message", Context.MODE_PRIVATE);
                    SharedPreferences.Editor spEditor = sharedPref.edit();
                    spEditor.putString("startDate", startDateButton.getText().toString());
                    spEditor.putString("endDate", endDateButton.getText().toString());
                    spEditor.apply();
                }
                else{
                    Toast.makeText(getActivity(),"Start date must earlier than end date", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ID2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date endDate = convertStringDate(startDateButton.getText().toString());
                Date startDate = convertStringDate(endDateButton.getText().toString());
                if (startDate.after(endDate)) {
                    FragmentManager fragmentManager = getFragmentManager();
                    assert fragmentManager != null;
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, new PieChartFragment());
                    fragmentTransaction.commit();

                    SharedPreferences sharedPref = requireActivity().getSharedPreferences("Message", Context.MODE_PRIVATE);
                    SharedPreferences.Editor spEditor = sharedPref.edit();
                    spEditor.putString("startDate", startDateButton.getText().toString());
                    spEditor.putString("endDate", endDateButton.getText().toString());
                    spEditor.apply();
                }
                else{
                    Toast.makeText(getActivity(),"Start date must earlier than end date", Toast.LENGTH_SHORT).show();
                }
            }
        });

         return root;
    }

    private Date convertStringDate(String stringDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        try {
            Date date = sdf.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("hey");
            return null;
        }
    }

    private void initStartDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                startDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        startDatePickerDialog = new DatePickerDialog(getActivity(),style,dateSetListener, year,month, day);
        startDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

    }

    private void initEndDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                endDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        endDatePickerDialog = new DatePickerDialog(getActivity(),style,dateSetListener, year,month, day);
        endDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    private String makeDateString(int day, int month, int year) {
        if (month < 10){
            return day + "/0" + month + "/" + year;
        }
        return day + "/" + month + "/" + year;
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i("test", "----- requestCode= "+requestCode);
        Log.i("test", "----- resultCode= "+resultCode);
        switch (resultCode) {
            case 1: // get the information
                Toast.makeText(getContext(),"successful",Toast.LENGTH_LONG).show();
                break;
            case 2:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}