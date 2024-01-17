package com.practice.ordersys;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import com.practice.ordersys.databinding.ActivityMainBinding;
import com.practice.ordersys.databinding.ActivitySecBinding;

public class SecActivity extends AppCompatActivity {
    private ActivitySecBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSend.setOnClickListener(view -> {
            if (binding.edDrink.length() < 1){
                Toast.makeText(this,"請輸入飲料名稱", Toast.LENGTH_SHORT).show();
            } else {
                Bundle b = new Bundle();
                RadioButton rbSugar = findViewById(binding.rgSugar.getCheckedRadioButtonId());
                RadioButton rbIce   = findViewById(binding.rgIce.getCheckedRadioButtonId());
                String sugarValue = rbSugar.getText().toString();
                String iceValue = rbIce.getText().toString();
                b.putString("drink", binding.edDrink.getText().toString());
                b.putString("sugar", sugarValue);
                b.putString("ice", iceValue);
                setResult(Activity.RESULT_OK, new Intent().putExtras(b));
                finish();
            }
        });
    }

}