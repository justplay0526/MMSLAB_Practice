package com.practice.viewpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SecondFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SecondFragment", "onCreate");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("SecondFragment", "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("SecondFragment", "onViewCreated");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.e("SecondFragment", "Start");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("SecondFragment", "Resume");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e("SecondFragment", "Pause");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.e("SecondFragment", "onStop");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SecondFragment", "onDestroy");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("SecondFragment", "onDetach");
    }
}