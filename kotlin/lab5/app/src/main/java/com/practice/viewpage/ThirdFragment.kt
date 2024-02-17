package com.practice.viewpage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ThirdFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ThirdFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("ThirdFragment", "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    // onActivityCreated is deprecated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("ThirdFragment", "onViewCreated")
    }
    override fun onStart() {
        super.onStart()
        Log.e("ThirdFragment", "Start")
    }

    override fun onResume() {
        super.onResume()
        Log.e("ThirdFragment", "Resume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("ThirdFragment", "Pause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("ThirdFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("ThirdFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ThirdFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("ThirdFragment", "onDetach")
    }
}