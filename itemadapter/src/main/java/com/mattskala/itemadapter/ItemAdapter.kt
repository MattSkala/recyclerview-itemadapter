package com.mattskala.itemadapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

open class ItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<Item>()
    private val renderers = SparseArray<ItemRenderer<out Item, out RecyclerView.ViewHolder>>()

    fun registerRenderer(renderer: ItemRenderer<out Item, out RecyclerView.ViewHolder>) {
        if (renderers[renderer.getType()] != null)
            Log.w("ItemAdapter", "A renderer for this item type (" + renderer.getTypeName() + ") is already registered")
        renderers.put(renderer.getType(), renderer)
    }
    
    fun registerRenderers(vararg renderers: ItemRenderer<*, *>) {
        renderers.forEach(::registerRenderer)
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
        val renderer = getRenderer(viewType)
        return renderer.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val item = items[position]
        val renderer = getRenderer(viewType)
        renderer.bindView(item, holder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        onBindViewHolder(holder, position)
    }

    private fun getRenderer(
        viewType: Int
    ): ItemRenderer<Item, RecyclerView.ViewHolder> {
        @Suppress("UNCHECKED_CAST")
        return renderers[viewType] as? ItemRenderer<Item, RecyclerView.ViewHolder>
            ?: throw createMissingRendererException(viewType)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        getRenderer(holder.itemViewType).onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return getRenderer(holder.itemViewType).onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        getRenderer(holder.itemViewType).onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        getRenderer(holder.itemViewType).onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getType()
    }

    private fun createMissingRendererException(viewType: Int): Exception {
        for (item in items) {
            if (item.getType() == viewType) {
                return IllegalStateException("No renderer registered for item type ${item.javaClass.name}")
            }
        }
        return IllegalStateException("No renderer registered for viewType $viewType")
    }
}
