package com.mattskala.itemadapter.example

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mattskala.itemadapter.BindingItemRenderer
import com.mattskala.itemadapter.Item
import com.mattskala.itemadapter.ItemAdapter
import com.mattskala.itemadapter.example.databinding.ItemExampleBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    companion object {
        private const val ITEM_ADD = 1
    }

    private val adapter = ItemAdapter()

    private val allItems = MutableStateFlow(createItems())
    private val selectedItems = MutableStateFlow(emptySet<Int>())
    private val selectableItems = combine(allItems, selectedItems) { items, selected ->
        items.map { SelectableExampleItem(it, it.id in selected) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exampleRenderer = ExampleRenderer {
            val id = it.data.id
            val selected = selectedItems.value
            selectedItems.value = if (id in selected) selected.minus(id)
            else selected.plus(id)
        }
        adapter.registerRenderer(exampleRenderer)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        selectableItems.onEach { adapter.updateItems(it) }.launchIn(lifecycleScope)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, ITEM_ADD, 0, "Add")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            ITEM_ADD -> {
                val oldItems = allItems.value
                allItems.value = oldItems.plus(ExampleItem(oldItems.size, "What Next?", "X.Y"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createItems(): List<ExampleItem> {
        val items = mutableListOf<ExampleItem>()
        items += ExampleItem(items.size, "Cupcake", "1.5")
        items += ExampleItem(items.size, "Donut", "1.6")
        items += ExampleItem(items.size, "Eclair", "2.0")
        items += ExampleItem(items.size, "Froyo", "2.2")
        items += ExampleItem(items.size, "Gingerbread", "2.3")
        items += ExampleItem(items.size, "Honeycomb", "3.0")
        items += ExampleItem(items.size, "Ice Cream Sandwich", "4.0")
        items += ExampleItem(items.size, "Jelly Bean", "4.1")
        items += ExampleItem(items.size, "KitKat", "4.4")
        items += ExampleItem(items.size, "Lollipop", "5.0")
        items += ExampleItem(items.size, "Marshmallow", "6.0")
        items += ExampleItem(items.size, "Nougat", "7.0")
        items += ExampleItem(items.size, "Oreo", "8.0")
        items += ExampleItem(items.size, "Pie", "9.0")
        return items
    }

    class ExampleRenderer(
        private val onItemClick: (SelectableExampleItem) -> Unit
    ) : BindingItemRenderer<SelectableExampleItem, ItemExampleBinding>(
        SelectableExampleItem::class.java,
        ItemExampleBinding::inflate
    ) {
        override fun bindView(item: SelectableExampleItem, binding: ItemExampleBinding) {
            val data = item.data
            binding.title.text = data.title
            binding.subtitle.text = data.subtitle
            binding.root.setOnClickListener { onItemClick(item) }

            binding.checkbox.isChecked = item.isSelected
            val translation = if (item.isSelected) 100F else 0F
            binding.title.translationX = translation
        }

        override fun updateView(item: SelectableExampleItem, binding: ItemExampleBinding): Boolean {
            binding.checkbox.isChecked = item.isSelected
            val translation = if (item.isSelected) 100F else 0F
            binding.title.animate().translationX(translation)
            return true
        }
    }

    data class ExampleItem(
        val id: Int,
        val title: String,
        val subtitle: String
    ) : Item()

    data class SelectableExampleItem(
        val data: ExampleItem,
        val isSelected: Boolean
    ) : Item() {
        override fun areItemsTheSame(other: Item): Boolean {
            return other is SelectableExampleItem && data.id == other.data.id
        }
    }
}
