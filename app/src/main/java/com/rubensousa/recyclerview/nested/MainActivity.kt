package com.rubensousa.recyclerview.nested

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val adapter = ParentAdapter()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadItems()
    }

    private fun loadItems() {
        val lists = arrayListOf<TitledList>()
        repeat(20) { listIndex ->
            val items = arrayListOf<String>()
            repeat(30) { itemIndex -> items.add(itemIndex.toString()) }
            lists.add(TitledList("List number $listIndex", items))
        }
        adapter.setItems(lists)
    }

}
