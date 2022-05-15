package com.example.assignment3.ui.report;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.BitmapUtil;
import com.example.assignment3.R;
import com.example.assignment3.databinding.FragmentBarchartBinding;
import com.example.assignment3.entity.Movement;
import com.example.assignment3.entity.User;
import com.example.assignment3.entity.relationship.UserWithMovements;
import com.example.assignment3.viewmodel.UserViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// this fragment is for report page, feel free to edit

public class BarChartFragment extends Fragment {

    private FragmentBarchartBinding binding;
    private BarChart barChart;
    private ArrayList barArrayList;

    private UserViewModel userViewModel;
    private Date startReportDate;
    private Date endReportDate;

    private Bitmap bitmap;
    private Button shareButton;
    private Button id;


    /**
     * 截取全屏
     *
     * @return
     */
    public Bitmap captureScreenWindow() {
        getActivity().getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = getActivity().getWindow().getDecorView().getDrawingCache();
        return bmp;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BarChartViewModel barChartViewModel =
                new ViewModelProvider(this).get(BarChartViewModel.class);

        binding = FragmentBarchartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(UserViewModel.class);

        id = (Button) root.findViewById(R.id.back_button);
        shareButton = (Button) root.findViewById(R.id.facebook_button);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    ActivityCompat.requestPermissions(getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //调用
                    extracted(shareButton, id);
                }

            }
        });

        // Dates
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("Message",  Context.MODE_PRIVATE);
        String startReportDateString = sharedPref.getString("startDate",null);
        String endReportDateString = sharedPref.getString("endDate",null);
        startReportDate = convertStringDate(startReportDateString);
        endReportDate = convertStringDate(endReportDateString);


        barChart = root.findViewById(R.id.barReportChart);
        getData();


        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ReportFragment Report = new ReportFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, Report);
                fragmentTransaction.commit();

            }
        });

        final TextView textView = binding.generateReport;
        barChartViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    private void extracted(Button shareButton, Button ID) {
        shareButton.setVisibility(View.GONE);
        ID.setVisibility(View.GONE);
        Bitmap bitmap = captureScreenWindow();
        shareButton.setVisibility(View.VISIBLE);
        ID.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        File file = BitmapUtil.compressImage(bitmap, getContext());
        Uri uriForFile = FileProvider.getUriForFile(getContext(), "com.example.assignment3.fileprovider", file);
        Log.d("TAG", "uriForFile" + uriForFile);
        Intent intent = new Intent(getActivity(), FacebookActivity.class).putExtra("bitmap", uriForFile.toString());
        startActivity(intent);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("TAG", "fragmentonRequestPermissionsResult: ");
        if (grantResults != null && grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 1: {
                    Log.d("TAG", "onRequestPermissionsResult: ");
                    extracted(shareButton, id);
                }
                break;
            }
        }
    }

    private void getData() {
        barArrayList = new ArrayList();

        Bundle bundle = getActivity().getIntent().getExtras();
        User user = bundle.getParcelable("loginUser");

        userViewModel.getMovementByEmail(user.getEmail()).observe(getViewLifecycleOwner(), new Observer<List<UserWithMovements>>() {
            @Override
            public void onChanged(List<UserWithMovements> userWithMovements) {
                float count =0;
                ArrayList<String> labelNames = new ArrayList<>();
                labelNames.add("No entries");
                for (UserWithMovements temp : userWithMovements){
                    for(Movement temp2: temp.movements){

                        Date movementDate = convertStringDate(temp2.getTime());
                        //Get date
                        if(!movementDate.before(startReportDate) && !movementDate.after(endReportDate)){
                            count += 1;
                            barArrayList.add(new BarEntry(count,(float) ((float)temp2.getMovement() / 1000)));
                            labelNames.add(temp2.getTime());
                            System.out.println(temp2.getTime());
                        }
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                BarDataSet barDataSet = new BarDataSet(barArrayList, "All Movement Records: ("
                        + sdf.format(startReportDate) + " - " + sdf.format(endReportDate) + ")");
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextSize(10f);
                barChart.getDescription().setEnabled(true);
                Description description = new Description();
                description.setText("Y-Axis: Distance (kilometres)");
                barChart.setDescription(description);

                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setAxisMinimum(0f);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setGranularity(1f);
                //xAxis.setGranularityEnabled(true);

                //xAxis.setAxisMaximum(labelNames.size()+1);
                xAxis.setLabelCount(labelNames.size()-1);

                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                //xAxis.setCenterAxisLabels(true);
                //xAxis.setLabelRotationAngle(270);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));

                barChart.animateY(1200);
                barChart.invalidate();
             }

        });

    }

    private Date convertStringDate(String stringDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            Date date = sdf.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("hey");
            return null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}