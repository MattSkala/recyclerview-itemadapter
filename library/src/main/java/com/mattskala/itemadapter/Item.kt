package com.mattskala.itemadapter

/**
 * An abstract item for use in RecyclerView.Adapter.
 */
abstract class Item {
    /**
     * Checks if the items are same.
     */
    open fun areItemsTheSame(other: Any?): Boolean {
        return this == other
    }

    /**
     * Returns an item type.
     */
    fun getType(): Int {
        return javaClass.hashCode()
    }
}