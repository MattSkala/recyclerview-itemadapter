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

If you do not need direct access to a view holder, you can subclass `ItemViewRenderer` instead, which allows to return a view from the `createView` method and wraps it in a view holder internally.

```
class ExampleRenderer : ItemViewRenderer<ExampleItem, MyView>(ExampleItem::class.java) {
    override fun createView(parent: ViewGroup): MyView {
        return MyView(parent.context)
    }

    override fun bindView(item: ExampleItem, view: MyView) = with(view) {
        view.title = item.title
        view.subtitle = item.subtitle
    }
}
```

With `ItemLayoutRenderer`, you only have to return the layout resource ID in `getLayoutResourceId`, and the renderer inflates the layout automatically.

```
class ExampleRenderer : ItemLayoutRenderer<ExampleItem, View>(ExampleItem::class.java) {
    override fun getLayoutResourceId(): Int {
        return R.layout.item_example
    }

    override fun bindView(item: ExampleItem, view: View) = with(view) {
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

### 4. Update data

The `ItemAdapter` has a built-in support for `DiffUtil`, which allows it to only re-render items that have been changed when data has been changed. To take advantage of it, pass an updated item list to `ItemAdapter.updateData` method whenever the data changes. Additionally, you can override `Item.areItemsTheSame` and `Item.areContentsTheSame` methods to provide custom logic for determining if items represent the same entity or that a content has changed. Refer to the documentation of the `DiffUtil.Callback` class for the expected behavior.

## References

[A RecyclerView with multiple item types](https://android.jlelse.eu/a-recyclerview-with-multiple-item-types-dfba3979050)
