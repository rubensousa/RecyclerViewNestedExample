package com.rubensousa.recyclerview.nested

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ParentAdapter : RecyclerView.Adapter<ParentAdapter.VH>() {

    private var items = listOf<TitledList>()

    fun setItems(list: List<TitledList>) {
        this.items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.nested_adapter_list, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {

        private val titleTextView: TextView = view.findViewById(R.id.nestedTitleTextView)
        private val recyclerView: RecyclerView = view.findViewById(R.id.nestedRecyclerView)
        private val adapter = ChildAdapter()

        init {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
        }

        fun bind(item: TitledList) {
            titleTextView.text = item.title
            adapter.setItems(item.texts)
        }

    }
}