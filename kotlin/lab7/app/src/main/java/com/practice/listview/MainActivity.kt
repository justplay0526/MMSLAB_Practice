package com.practice.listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.practice.listview.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val count = ArrayList<String>()
        val item = ArrayList<Item>()
        val priceRange = IntRange(10, 100)
        val img_lst =
            resources.obtainTypedArray(R.array.image_list)
        for (i in 0 until img_lst.length()){
            val photo = img_lst.getResourceId(i, 0)
            val name = "水果${i+1}"
            val price = priceRange.random()
            count.add("${i+1}個")
            item.add(Item(photo, name, price))
        }
        img_lst.recycle()

        binding.spSpinner.adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, count)

        binding.gridView.numColumns = 3
        binding.gridView.adapter = MyAdapter(this, item, R.layout.adapter_vert)
        binding.listView.adapter = MyAdapter(this, item, R.layout.adapter_horizon)
    }
}

data class Item(
    val photo:  Int,
    val name:   String,
    val price:  Int
)

