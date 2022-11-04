package com.mattskala.itemadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * An abstract item renderer that creates an item view and binds an item model to it. Uses
 * view binding to inflate and access the view.
 */
abstract class BindingItemRenderer<M : Item, V : ViewBinding>(
    modelClass: Class<M>,
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> V
) : ItemRenderer<M, BindingViewHolder<V>>(modelClass) {
    /**
     * Binds an item model to the view.
     * @param item An item model.
     * @param binding A view binding.
     */
    abstract fun bindView(item: M, binding: V)

    /**
     * Updates a view to match the new item. Called when only contents of item have changed.
     * @return true if view was updated, false if full bind should be executed
     */
    open fun updateView(item: M, binding: V): Boolean = false

    override fun createViewHolder(parent: ViewGroup): BindingViewHolder<V> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = bindingInflater(inflater, parent, false)
        return BindingViewHolder(binding)
    }

    override fun bindView(item: M, holder: BindingViewHolder<V>) {
        bindView(item, holder.binding)
    }

    override fun updateView(item: M, holder: BindingViewHolder<V>): Boolean {
        return updateView(item, holder.binding)
    }

    override fun onViewRecycled(holder: BindingViewHolder<V>) {
        onViewRecycled(holder.binding)
    }

    override fun onFailedToRecycleView(holder: BindingViewHolder<V>): Boolean {
        return onFailedToRecycleView(holder.binding)
    }

    override fun onViewAttachedToWindow(holder: BindingViewHolder<V>) {
        onViewAttachedToWindow(holder.binding)
    }

    override fun onViewDetachedFromWindow(holder: BindingViewHolder<V>) {
        onViewDetachedFromWindow(holder.binding)
    }

    open fun onViewRecycled(binding: V) {}

    open fun onFailedToRecycleView(binding: V): Boolean {
        return false
    }

    open fun onViewAttachedToWindow(binding: V) {}

    open fun onViewDetachedFromWindow(binding: V) {}
}
