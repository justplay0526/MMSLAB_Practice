package com.practice.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.practice.listview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}

class Item{
    private int photo;
    private String name;
    private int price;
    public Item(int photo, String name, int price){
        this.photo = photo;
        this.name = name;
        this.price = price;
    }
}