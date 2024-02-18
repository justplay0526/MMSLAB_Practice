package com.practice.viewpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ThirdFragment extends Fragment {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ThirdFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ThirdFragment", "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("ThirdFragment", "onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("ThirdFragment", "Start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ThirdFragment", "Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("ThirdFragment", "Pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("ThirdFragment", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ThirdFragment", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("ThirdFragment", "onDetach");
    }
}