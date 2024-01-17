package com.practice.ordersys;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.practice.ordersys.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                (ActivityResultCallback<ActivityResult>) result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle b = result.getData().getExtras();
                        binding.tvMeal.setText(String.format(
                            "飲料： %s\n\n" + "甜度： %s\n\n" + "冰塊： %s",
                                b.getString("drink"),
                                b.getString("sugar"),
                                b.getString("ice"))
                        );
                    }
                });

        binding.btnChoice.setOnClickListener(view -> {
            Intent intent = new Intent(this, SecActivity.class);
            resultLauncher.launch(intent);
        });
        }
}