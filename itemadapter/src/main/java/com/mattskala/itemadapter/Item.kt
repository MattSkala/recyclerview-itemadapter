package com.mattskala.itemadapter

/**
 * An abstract item for use in RecyclerView.Adapter.
 */
abstract class Item {
    /**
     * Checks if the items are same.
     */
    open fun areItemsTheSame(other: Item): Boolean {
        return this == other
    }

    /**
     * Checks if the items have the same content.
     */
    open fun areContentsTheSame(other: Item): Boolean {
        return this == other
    }

    /**
     * Returns a change payload for item that has changed
     */
    open fun getChangePayload(other: Item): Any = other

    /**
     * Returns an item type.
     */
    fun getType(): Int {
        return javaClass.hashCode()
    }
}
