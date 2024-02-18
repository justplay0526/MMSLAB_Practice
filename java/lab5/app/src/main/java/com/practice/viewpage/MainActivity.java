package com.practice.viewpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.util.Log;

import com.practice.viewpage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.e("MainActivity", "onCreate");
        FragmentManager fm = getSupportFragmentManager();
        Lifecycle lc = getLifecycle();
        ViewPageAdapter adapter = new ViewPageAdapter(fm, lc);
        binding.viewPager.setAdapter(adapter);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("MainActivity", "Restart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity", "Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", "onDestroy");
    }
}
class ViewPageAdapter extends FragmentStateAdapter{
    public ViewPageAdapter(@NonNull FragmentManager fm,@NonNull Lifecycle lc){
        super(fm, lc);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    }

    @Override
    public int getItemCount(){
        return 3;
    }
}

