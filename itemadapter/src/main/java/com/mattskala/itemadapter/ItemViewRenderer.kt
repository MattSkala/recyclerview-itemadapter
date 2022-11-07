package com.mattskala.itemadapter

import android.view.View
import android.view.ViewGroup

/**
 * An abstract item renderer that creates an item view and binds an item model to it. It
 * eliminates the need for a ViewHolder, but requires another view caching mechanism to be used
 * (e.g. Kotlin synthetic properties, or a custom view holding references to child views).
 */
abstract class ItemViewRenderer<M : Item, V : View>(
    modelClass: Class<M>
) : ItemRenderer<M, ItemViewHolder>(modelClass) {
    /**
     * Creates an item view.
     * @param parent A view that can be used as the root when inflating a view.
     */
    abstract fun createView(parent: ViewGroup): V

    /**
     * Binds an item model to the view.
     * @param item An item model.
     * @param view A recycled view.
     */
    abstract fun bindView(item: M, view: V)

    /**
     * Updates a view to match the new item. Called when only contents of item have changed.
     * @return true if view was updated, false if full bind should be executed
     */
    open fun updateView(item: M, view: V): Boolean = false

    override fun createViewHolder(parent: ViewGroup): ItemViewHolder {
        return ItemViewHolder(createView(parent))
    }

    override fun bindView(item: M, holder: ItemViewHolder) {
        @Suppress("UNCHECKED_CAST")
        bindView(item, holder.itemView as V)
    }

    override fun updateView(item: M, holder: ItemViewHolder): Boolean {
        @Suppress("UNCHECKED_CAST")
        return updateView(item, holder.itemView as V)
    }
}
