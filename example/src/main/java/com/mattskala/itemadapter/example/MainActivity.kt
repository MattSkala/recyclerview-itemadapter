package com.mattskala.itemadapter.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mattskala.itemadapter.*
import com.mattskala.itemadapter.example.databinding.ItemExampleBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val ITEM_ADD = 1
    }

    private val adapter = ItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.items = createItems()
        val exampleRenderer = ExampleRenderer {
            val items = adapter.items.toMutableList()
            items.remove(it)
            adapter.updateItems(items)
        }
        adapter.registerRenderer(exampleRenderer)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, ITEM_ADD, 0, "Add")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            ITEM_ADD -> {
                val items = adapter.items.toMutableList()
                items.add(ExampleItem("What Next?", "X.Y"))
                adapter.updateItems(items)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createItems() : List<ExampleItem> {
        val items = mutableListOf<ExampleItem>()
        items += ExampleItem("Cupcake", "1.5")
        items += ExampleItem("Donut", "1.6")
        items += ExampleItem("Eclair", "2.0")
        items += ExampleItem("Froyo", "2.2")
        items += ExampleItem("Gingerbread", "2.3")
        items += ExampleItem("Honeycomb", "3.0")
        items += ExampleItem("Ice Cream Sandwich", "4.0")
        items += ExampleItem("Jelly Bean", "4.1")
        items += ExampleItem("KitKat", "4.4")
        items += ExampleItem("Lollipop", "5.0")
        items += ExampleItem("Marshmallow", "6.0")
        items += ExampleItem("Nougat", "7.0")
        items += ExampleItem("Oreo", "8.0")
        items += ExampleItem("Pie", "9.0")
        return items
    }

    class ExampleRenderer(
        private val onItemClick: (ExampleItem) -> Unit
    ) : BindingItemRenderer<ExampleItem, ItemExampleBinding>(
        ExampleItem::class.java,
        ItemExampleBinding::inflate
    ) {
        override fun bindView(item: ExampleItem, binding: ItemExampleBinding) {
            binding.title.text = item.title
            binding.subtitle.text = item.subtitle
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class ExampleItem(val title: String, val subtitle: String) : Item()
}
