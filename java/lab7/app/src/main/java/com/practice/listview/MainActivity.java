package com.practice.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.practice.listview.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ArrayList<String> count = new ArrayList<>();
        ArrayList<Data> data = new ArrayList<>();
        TypedArray img_lst =
                getResources().obtainTypedArray(R.array.image_list);
        for (int i = 0; i < img_lst.length(); i++) {
            int photo = img_lst.getResourceId(i, 0);
            String name = "水果" + (i+1);
            int price = new Random().nextInt(100- 10) + 10;
            count.add((i+1) + "個");
//            data.add(Data(photo, name, price));
        }
        ArrayAdapter<String> countAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,count);
        binding.spSpinner.setAdapter(countAdapter);



//        binding.listView.setAdapter(messageAdapter);
//        binding.gridView.setNumColumns(3);
//        binding.gridView.setAdapter(cubeeAdapter);

    }

    public class MyAdapter extends BaseAdapter{
        private MainActivity.Data[] data;
        private int view;

        public MyAdapter(MainActivity.Data[] data,int view){
            this.data = data;
            this.view = view;
        }
        @Override
        public int getCount(){
            return data.length;
        }
        @Override
        public Object getItem(int position){
            return data[position];
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            convertView = getLayoutInflater().inflate(view, parent, false);
            TextView name =convertView.findViewById(R.id.tv_msg);
            name.setText(data[position].name);
            ImageView imageView = convertView.findViewById(R.id.img_photo);
            imageView.setImageResource(data[position].photo);
            return convertView;
        }

    }
    class Data{
        int photo;
        String name;
        int price;
    }
}