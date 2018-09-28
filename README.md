# RecyclerView ItemAdapter

**A RecyclerView adapter reducing boilerplate and improving item reusability**

Never have to write an adapter again. Just use the provided `ItemAdapter` across the whole project and only define `ItemRenderer` for your items. You can register multiple renderers to one adapter, which allows you to display multiple item types in a single recycler view. It also helps you to reuse the same items across different recycler views in your app.


## Usage

### 1. Define an Item model

```
class ExampleItem(val title: String, val subtitle: String) : Item()
```

### 2. Extend the ItemRenderer class

```
class ExampleRenderer : ItemRenderer<ExampleItem, ItemViewHolder>(ExampleItem::class.java) {
    override fun createViewHolder(parent: ViewGroup): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_example, parent, false))
    }

    override fun bindView(item: ExampleItem, holder: ItemViewHolder) = with(holder.itemView) {
        title.text = item.title
        subtitle.text = item.subtitle
    }
}
```

### 3. Register your renderer in ItemAdapter

```
val adapter = ItemAdapter()
adapter.items = listOf(ExampleItem("Hello", "World"))
val exampleRenderer = ExampleRenderer()
adapter.registerRenderer(exampleRenderer)
```


## References

[A RecyclerView with multiple item types](https://android.jlelse.eu/a-recyclerview-with-multiple-item-types-dfba3979050)
