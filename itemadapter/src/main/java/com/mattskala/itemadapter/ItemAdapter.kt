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
                return oldItems[oldItemPosition] == newItems[newItemPosition]
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
        val renderer = renderers.get(viewType) as? ItemRenderer<*, *>
                ?: throw Exception("No renderer registered for item " + getItemByViewType(viewType)?.javaClass)
        return renderer.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val item = items[position]
        val renderer = renderers.get(viewType) as? ItemRenderer<Item, ItemViewHolder>
                ?: throw Exception("No renderer registered for item " + items[position])
        renderer.bindView(item, holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getType()
    }

    private fun getItemByViewType(viewType: Int): Item? {
        return items.find { it.getType() == viewType }
    }
}