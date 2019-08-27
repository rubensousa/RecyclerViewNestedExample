/*
 * Copyright 2019 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rubensousa.recyclerview.nested

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper


class ParentAdapter(private val scrollStateHolder: ScrollStateHolder) :
    RecyclerView.Adapter<ParentAdapter.VH>() {

    private var items = listOf<TitledList>()

    fun setItems(list: List<TitledList>) {
        this.items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.nested_adapter_list,
            parent, false
        )
        val vh = VH(view, scrollStateHolder)
        vh.onCreated()
        return vh
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBound(items[position])
    }

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetachedFromWindow()
    }

    class VH(view: View, private val scrollStateHolder: ScrollStateHolder) :
        RecyclerView.ViewHolder(view), ScrollStateHolder.ScrollStateKeyProvider {

        private val titleTextView: TextView = view.findViewById(R.id.nestedTitleTextView)
        private val recyclerView: RecyclerView = view.findViewById(R.id.nestedRecyclerView)
        private val layoutManager = LinearLayoutManager(
            view.context,
            RecyclerView.HORIZONTAL, false
        )
        private val adapter = ChildAdapter()
        private val snapHelper = GravitySnapHelper(Gravity.START)
        private var currentItem: TitledList? = null

        override fun getScrollStateKey(): String? = currentItem?.title

        fun onCreated() {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator?.changeDuration = 0
            snapHelper.attachToRecyclerView(recyclerView)
            scrollStateHolder.setupRecyclerView(recyclerView, this)
        }

        fun onBound(item: TitledList) {
            currentItem = item
            titleTextView.text = item.title
            adapter.setItems(item.texts)
            scrollStateHolder.restoreScrollState(recyclerView, this)
        }

        fun onRecycled() {
            scrollStateHolder.saveScrollState(recyclerView, this)
            currentItem = null
        }

        /**
         * If we fast scroll while this ViewHolder's RecyclerView is still settling the scroll,
         * the view will be detached and won't be snapped correctly
         *
         * To fix that, we snap again without smooth scrolling.
         */
        fun onDetachedFromWindow() {
            snapHelper.findSnapView(layoutManager)?.let {
                val snapDistance = snapHelper.calculateDistanceToFinalSnap(layoutManager, it)
                if (snapDistance!![0] != 0 || snapDistance[1] != 0) {
                    recyclerView.scrollBy(snapDistance[0], snapDistance[1])
                }
            }
        }
    }
}
