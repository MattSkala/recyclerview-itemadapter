package com.mattskala.itemadapter

import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

open class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    var items = listOf<Item>()
    private var renderers = SparseArray<ItemRenderer<out Item, out ItemViewHolder>>()

    fun registerRenderer(renderer: ItemRenderer<out Item, out ItemViewHolder>) {
        renderers.put(renderer.getType(), renderer)
    }

    fun updateItems(newItems: List<Item>) {
        val oldItems = this.items
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].areItemsTheSame(newItems[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].areContentsTheSame(newItems[newItemPosition])
            }

            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }
        }, true)
        this.items = newItems
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return renderers.get(viewType).createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val item = items[position]
        @Suppress("UNCHECKED_CAST")
        val renderer = renderers.get(viewType) as? ItemRenderer<Item, ItemViewHolder>
                ?: throw Exception("No renderer registered for item $item")
        renderer.bindView(item, holder)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, payloads: MutableList<Any>) {
        val viewType = getItemViewType(position)
        val item = items[position]
        @Suppress("UNCHECKED_CAST")
        val renderer = renderers.get(viewType) as? ItemRenderer<Item, ItemViewHolder>
                ?: throw Exception("No renderer registered for item $item")
        renderer.bindView(item, holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getType()
    }
}