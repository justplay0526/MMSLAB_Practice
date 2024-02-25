package com.practice.hintmsg;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.practice.hintmsg.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    final String[] item = {"選項1", "選項2", "選項3", "選項4", "選項5"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnToast.setOnClickListener(view -> showToast("預設Toast"));

        binding.btnCustom.setOnClickListener(view -> {
            Toast toast = new Toast(this);
            View customView = getLayoutInflater().inflate(R.layout.custom_toast, null);
            toast.setGravity(Gravity.TOP, 0, 50);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(customView);
            toast.show();
        });

        binding.btnSnackbar.setOnClickListener(view -> {
                Snackbar snackbar = Snackbar.make(view, "按鈕式Snackbar", Snackbar.LENGTH_SHORT)
                        .setAction("按鈕", view1 -> showToast("已回應"));
                snackbar.show();
            }
        );

        binding.btnDialog1.setOnClickListener(view -> {
            AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
            dialog_list.setTitle("按鈕式AlertDialog");
            dialog_list.setMessage("AlertDialog內容");
            dialog_list.setNeutralButton("左按鈕", (dialogInterface, i) -> showToast("左按鈕"));
            dialog_list.setNegativeButton("中按鈕", (dialogInterface, i) -> showToast("中按鈕"));
            dialog_list.setPositiveButton("右按鈕", (dialogInterface, i) -> showToast("右按鈕"));
            dialog_list.show();
        });

        binding.btnDialog2.setOnClickListener(view -> {
            AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
            dialog_list.setTitle("列表式AlertDialog");
            dialog_list.setItems(item, (dialogInterface, i) -> showToast("你選的是" + item[i]));
            dialog_list.show();
        });

        binding.btnDialog3.setOnClickListener(view -> {
            final int[] position = {0};
            AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
            dialog_list.setTitle("單選式AlertDialog");
            dialog_list.setSingleChoiceItems(item, 0, (dialogInterface, i) -> position[0] = i);
            dialog_list.setPositiveButton("確定", (dialogInterface, i) -> showToast("你選的是" + item[position[0]]));
            dialog_list.show();
        });
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}