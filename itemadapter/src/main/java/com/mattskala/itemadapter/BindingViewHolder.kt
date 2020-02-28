package com.mattskala.itemadapter

import androidx.viewbinding.ViewBinding

class BindingViewHolder<V : ViewBinding>(val binding: V) : ItemViewHolder(binding.root)
