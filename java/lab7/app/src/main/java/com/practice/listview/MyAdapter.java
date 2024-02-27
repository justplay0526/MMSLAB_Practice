package com.practice.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Item> {
    private Context context;
    private ArrayList<Item> data;
    private int layout;
    public ArrayAdapter<Item> MyAdapter(Context context, ArrayList<Item> data, int layout){
        this.context = context;
        this.data = data;
        this.layout = layout;
    }

}
