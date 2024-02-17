package com.practice.viewpage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FirstFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("FirstFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("FirstFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_first, container, false)
    }
    // onActivityCreated is deprecated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("FirstFragment", "onViewCreated")
    }
    override fun onStart() {
        super.onStart()
        Log.e("FirstFragment", "Start")
    }

    override fun onResume() {
        super.onResume()
        Log.e("FirstFragment", "Resume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("FirstFragment", "Pause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("FirstFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("FirstFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("FirstFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("FirstFragment", "onDetach")
    }
}