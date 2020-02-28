package com.mattskala.itemadapter

import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

open class ItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<Item>()
    private var renderers = SparseArray<ItemRenderer<out Item, out RecyclerView.ViewHolder>>()

    fun registerRenderer(renderer: ItemRenderer<out Item, out RecyclerView.ViewHolder>) {
        if (renderers[renderer.getType()] != null)
            throw IllegalArgumentException("A renderer for this item type is already registered")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val renderer = renderers.get(viewType)
            ?: throw Exception("No renderer registered for view type $viewType")
        return renderer.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val item = items[position]
        @Suppress("UNCHECKED_CAST")
        val renderer = renderers.get(viewType) as? ItemRenderer<Item, RecyclerView.ViewHolder>
            ?: throw Exception("No renderer registered for item $item")
        renderer.bindView(item, holder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getType()
    }
}
