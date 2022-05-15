package com.example.assignment3.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.R;
import com.example.assignment3.databinding.FragmentHomeBinding;
import com.example.assignment3.entity.User;
import com.example.assignment3.viewmodel.UserViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//version 1.0.1: home fragment --Hongyu
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        assert container != null;
        container.clearDisappearingChildren();
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = requireActivity().getIntent().getExtras();
        User user = bundle.getParcelable("loginUser");
        int id = user.getUid();
//
//
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat2 = new SimpleDateFormat("EEE, d MMM yyyy");
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat3 = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String dates = dateFormat.format(date);
        String dates2 = dateFormat2.format(date);
        String dates3 = dateFormat3.format(date);

        UserViewModel userViewModel = ViewModelProvider
                .AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(UserViewModel.class);
        userViewModel.getMovementById(id).observe(getViewLifecycleOwner(), movements -> {
            for (int i = 0; i < movements.size(); i++) {
                if (movements.get(i).getTime().equals(dates)) {
                    long distance = movements.get(i).getMovement();
                    String dailyDistance = String.valueOf(distance);
                    String calories = String.valueOf((int) (distance * 0.062));
                    binding.dailyCalories.setText(calories + " Kcal");
                    binding.dailyDistance.setText(dailyDistance + " M");
                }
            }
        });

        binding.todayDate.setText(dates3 + " • " + dates2);
        final TextView textView = binding.location;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        weatherApi();

        return view;

    }


    public void weatherApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiInterface weatherApiInterface = retrofit.create(WeatherApiInterface.class);

        Call<Root> call = weatherApiInterface.getWeather();

        call.enqueue(new Callback<Root>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                Root root = response.body();
                assert root != null;
                double temp = root.getMain().getTemp() - 273.15;
                String weather = root.getWeather().get(0).getDescription();
                int weatherId = root.getWeather().get(0).getId();
                int temps = (int) temp;
                Calendar calendar = Calendar.getInstance();
                int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
                try {
                    binding.tempTextView.setText(temps + "°C");
                    TextView tempDesc = (TextView) requireActivity().getWindow().getDecorView().findViewById(R.id.tempDesc);
                    tempDesc.setText(weather);
                    if (weatherId == 800 && (hour24hrs <= 6 || hour24hrs >= 20)) {
                        binding.weatherIcon.setImageResource(R.drawable.ic_clear_night);
                    } else if (weatherId == 800) {
                        binding.weatherIcon.setImageResource(R.drawable.ic_clear_day);
                    } else if (weatherId >= 200 && weatherId <= 232) {
                        binding.weatherIcon.setImageResource(R.drawable.thunderstorms);
                    } else if (weatherId >= 600 && weatherId <= 622) {
                        binding.weatherIcon.setImageResource(R.drawable.ic_snow);
                    } else if (weatherId >= 300 && weatherId <= 321) {
                        binding.weatherIcon.setImageResource(R.drawable.ic_drizzle);
                    } else if (weatherId >= 701 && weatherId <= 781) {
                        binding.weatherIcon.setImageResource(R.drawable.ic_haze);
                    } else if (weatherId >= 801 && weatherId <= 804) {
                        binding.weatherIcon.setImageResource(R.drawable.ic_cloudy);
                    } else if (weatherId >= 500 && weatherId <= 531) {
                        binding.weatherIcon.setImageResource(R.drawable.ic_rain);
                    }
                    if (hour24hrs <= 6 || hour24hrs >= 20) {
                        binding.tempSuggestions.setText(R.string.weather_night);
                    } else if (temps > 10 && temps < 30 && weatherId == 800 || (weatherId >= 801 && weatherId <= 804)) {
                        binding.tempSuggestions.setText(R.string.weather_good);
                    } else if (
                            temps < 9 || temps > 31 ||
                                    ((weatherId >= 600 && weatherId <= 622) ||           //snow
                                            (weatherId >= 500 && weatherId <= 531) ||    //rain
                                            (weatherId >= 300 && weatherId <= 321) ||    //drizzle
                                            (weatherId >= 200 && weatherId <= 232) ||    //thunderstorms
                                            (weatherId >= 701 && weatherId <= 781)       //haze
                                    )) {
                        binding.tempSuggestions.setText(R.string.weathet_bad);
                    }
                } catch (NullPointerException e) {
                    System.out.println("Unknown error appear");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}