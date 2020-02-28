package com.mattskala.itemadapter

import android.view.ViewGroup

/**
 * An abstract item renderer that creates an item view holder and binds an item model to the view.
 */
abstract class ItemRenderer<M : Item, VH : ItemViewHolder>(private val modelClass: Class<M>) {
    /**
     * Creates an item view holder.
     * @param parent A view that can be used as the root when inflating a view.
     */
    abstract fun createViewHolder(parent: ViewGroup): VH

    /**
     * Binds an item model to the view.
     * @param item An item model.
     * @param holder A view holder containing a recycled view.
     */
    abstract fun bindView(item: M, holder: VH)

    /**
     * Returns an item type.
     */
    fun getType(): Int {
        return modelClass.hashCode()
    }
}
