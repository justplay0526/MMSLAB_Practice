package com.practice.viewpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FirstFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("FirstFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("FirstFragment", "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("FirstFragment", "onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("FirstFragment", "Start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FirstFragment", "Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("FirstFragment", "Pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("FirstFragment", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FirstFragment", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("FirstFragment", "onDetach");
    }
}