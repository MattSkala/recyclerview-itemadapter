package com.mattskala.itemadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * An abstract item renderer that creates an item view holder and binds an item model to the view.
 */
abstract class ItemRenderer<M : Item, VH : RecyclerView.ViewHolder>(
    private val modelClass: Class<M>
) {
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
     * Updates a view to match the new item. Called when only contents of item have changed.
     * @return true if view was updated, false if full bind should be executed
     */
    open fun updateView(item: M, holder: VH): Boolean = false

    /**
     * Called when a view created by this renderer has been recycled.
     * @see RecyclerView.Adapter.onViewRecycled
     */
    open fun onViewRecycled(holder: VH) {}

    /**
     * Called if a view created by this renderer cannot be recycled due to its transient state.
     * @see RecyclerView.Adapter.onFailedToRecycleView
     */
    open fun onFailedToRecycleView(holder: VH): Boolean {
        return false
    }

    /**
     * Called when a view created by this renderer has been attached to a window.
     * @see RecyclerView.Adapter.onViewAttachedToWindow
     */
    open fun onViewAttachedToWindow(holder: VH) {}

    /**
     * Called when a view created by this renderer has been detached from its window.
     * @see RecyclerView.Adapter.onViewDetachedFromWindow
     */
    open fun onViewDetachedFromWindow(holder: VH) {}

    /**
     * Returns an item type.
     */
    fun getType(): Int {
        return modelClass.hashCode()
    }

    /**
     * Returns an item type class name.
     */
    fun getTypeName(): String {
        return modelClass.name
    }
}
