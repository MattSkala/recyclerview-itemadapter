package com.mattskala.itemadapter

import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup

open class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    var items = listOf<Item>()
    private var renderers = SparseArray<ItemRenderer<out Item, out ItemViewHolder>>()

    fun registerRenderer(renderer: ItemRenderer<out Item, out ItemViewHolder>) {
        renderers.put(renderer.getType(), renderer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return renderers.get(viewType).createViewHolder(parent)
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
        return items[position].javaClass.hashCode()
    }
}