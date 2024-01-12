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

            int comMora = (int)(Math.random() * 3);

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
                case  0: comMoraText = "剪刀";break;
                case  1: comMoraText = "石頭";break;
                default: comMoraText = "布";break;
            }

            binding.tvName.setText(String.format("名字\n%s", binding.edName.getText().toString()));
            binding.tvMmora.setText(String.format("我方出拳\n%s", playerMoraText));
            binding.tvCmora.setText(String.format("電腦出拳\n%s", comMoraText));

            if((binding.btnScissor.isChecked() && comMora == 2) ||
                    (binding.btnStone.isChecked() && comMora == 0) ||
                    (binding.btnPaper.isChecked() && comMora == 1)){
                binding.tvWinner.setText(String.format("勝利者\n%s", binding.edName.getText().toString()));
                binding.tvText.setText("恭喜你獲勝了");
            } else if ((binding.btnScissor.isChecked() && comMora == 1) ||
                    (binding.btnStone.isChecked() && comMora == 2) ||
                    (binding.btnPaper.isChecked() && comMora == 0)) {
                binding.tvWinner.setText("勝利者\n電腦");
                binding.tvText.setText("可惜，電腦獲勝了!");
            }else {
                binding.tvWinner.setText("勝利者\n平手");
                binding.tvText.setText("平局，請再試一次");
            }
        }

        );
    }
}