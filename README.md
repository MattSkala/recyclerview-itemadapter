# RecyclerView ItemAdapter

**A RecyclerView adapter reducing boilerplate and improving item reusability**

[ ![Download](https://api.bintray.com/packages/mattskala/maven/recyclerview-itemadapter/images/download.svg?version=0.2) ](https://bintray.com/mattskala/maven/recyclerview-itemadapter/0.2/link)

Never have to write an adapter again. Just use the provided `ItemAdapter` across the whole project
and only define `ItemRenderer` for your items. You can register multiple renderers to one adapter,
which allows you to display multiple item types in a single recycler view. It also helps you to
reuse the same items across different recycler views in your app.

## Installation

Add the following to the dependencies section in `build.gradle`:

```groovy
implementation 'com.mattskala:itemadapter:0.2'
```

## Usage

### 1. Define an Item model

The item model defines the item that is going to be rendered. In a simple case it can be only
a wrapper for your data model, but it can also contain hints and formatting functions useful for
rendering.

```kotlin
class ExampleItem(val title: String, val subtitle: String) : Item()
```

### 2. Extend the ItemRenderer class

The item renderer defines how the item should be rendered. The base `ItemRenderer` requires you to
implement two methods. Firstly, `createViewHolder` should create a new view holder instance
containing the item view. Secondly, `bindView` should bind a given item to the previously
created (and possibly recycled) view holder.  

```kotlin
class ExampleRenderer : ItemRenderer<ExampleItem, ItemViewHolder>(ExampleItem::class.java) {
    override fun createViewHolder(parent: ViewGroup): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExampleViewHolder(inflater.inflate(R.layout.item_example, parent, false))
    }

    override fun bindView(item: ExampleItem, holder: ItemViewHolder) = with(holder) {
        title.text = item.title
        subtitle.text = item.subtitle
    }
}
```

If you do not need direct access to a view holder, you can subclass `ItemViewRenderer` instead, 
which allows to return a view from the `createView` method and wraps it in a view holder internally.

```kotlin
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

With `ItemLayoutRenderer`, you only have to return the layout resource ID in `getLayoutResourceId`, 
and the renderer inflates the layout automatically. Note that this class is easy to misuse. If
you use synthetic properties to access child views in `bindView`, the views will not be cached and 
`findViewById` will be called on every invocation of `bindView` method!

```kotlin
class ExampleRenderer : ItemLayoutRenderer<ExampleItem, ExampleView>(ExampleItem::class.java) {
    override fun getLayoutResourceId(): Int {
        return R.layout.item_example
    }

    override fun bindView(item: ExampleItem, view: ExampleView) = with(view) {
        title.text = item.title
        subtitle.text = item.subtitle
    }
}
```

For this reason, it is recommended to use `BindingItemRenderer` which provides support for View 
Binding, a type-safe way to access layout views with caching and the minimum amount of boilerplate:

```kotlin
class ExampleRenderer : BindingItemRenderer<ExampleItem, ItemExampleBinding>(
    ExampleItem::class.java,
    ItemExampleBinding::inflate
) {
    override fun bindView(item: ExampleItem, binding: ItemExampleBinding) {
        binding.title.text = item.title
        binding.subtitle.text = item.subtitle
    }
}
```

### 3. Register your renderer in ItemAdapter

Finally, instantiate `ItemAdapter` and register your renderers. You can register multiple renderers
for different item types. Note that you should not register multiple renderers for the same item
type, which would result in `IllegalArgumentException`.

```kotlin
val adapter = ItemAdapter()
adapter.items = listOf(ExampleItem("Hello", "World"))
val exampleRenderer = ExampleRenderer()
adapter.registerRenderer(exampleRenderer)
```

### 4. Update data

The `ItemAdapter` has a built-in support for `DiffUtil`, which allows it to only re-render items 
that have been changed when data has been changed. To take advantage of it, pass an updated item 
list to `ItemAdapter.updateItems` method whenever the data changes. 

```kotlin
adapter.updateItems(items)
```

Additionally, you can override `Item.areItemsTheSame` and `Item.areContentsTheSame` methods to 
provide custom logic for determining if items represent the same entity or that the content has 
changed. Refer to the documentation of the `DiffUtil.Callback` class for the expected behavior.

## References

[A RecyclerView with multiple item types](https://android.jlelse.eu/a-recyclerview-with-multiple-item-types-dfba3979050)
