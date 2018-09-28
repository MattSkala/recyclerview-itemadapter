package com.mattskala.itemadapter.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mattskala.itemadapter.*
import kotlinx.android.synthetic.main.item_example.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ItemAdapter()
        adapter.items = createItems()
        val exampleRenderer = ExampleRenderer()
        adapter.registerRenderer(exampleRenderer)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
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

    class ExampleRenderer : ItemViewRenderer<ExampleItem, View>(ExampleItem::class.java) {
        override fun createView(parent: ViewGroup): View {
            val inflater = LayoutInflater.from(parent.context)
            return inflater.inflate(R.layout.item_example, parent, false)
        }

        override fun bindView(item: ExampleItem, view: View) = with(view) {
            title.text = item.title
            subtitle.text = item.subtitle
        }
    }

    class ExampleItem(val title: String, val subtitle: String) : Item()
}
