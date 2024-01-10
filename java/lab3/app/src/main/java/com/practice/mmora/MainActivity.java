package com.practice.mmora;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.practice.mmora.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnMora.setOnClickListener(view -> {
            if (binding.edName.length() <1 ) {
                binding.tvText.setText("請輸入姓名以開始遊戲");
                return;
            }
            String playerName = binding.edName.getText().toString();

            int comMora = (int)(Math.random() *3);

            String playerMoraText = "";
            if(binding.btnScissor.isChecked()){
                playerMoraText = "剪刀";
                    } else if (binding.btnStone.isChecked()) {
                playerMoraText = "石頭";
            } else {
                playerMoraText = "布";
            }

            String comMoraText = "";
            switch(comMora){
                case  0: comMoraText = "剪刀";
                case  1: comMoraText = "石頭";
                default: comMoraText = "布";
            }

            binding.tvText.setText(String.format("名字\n%s", binding.edName.getText().toString()));
            binding.tvMmora.setText(String.format("我方出拳\n%s", playerMoraText));
            binding.tvCmora.setText(String.format("電腦出拳\n%s", comMoraText));
        }

        );
    }
}