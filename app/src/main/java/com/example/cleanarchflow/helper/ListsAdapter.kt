package com.example.cleanarchflow.helper

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanarchflow.R
import com.example.cleanarchflow.base.Adapter
import com.example.cleanarchflow.databinding.BookItemBinding
import com.example.cleanarchflow.ui.home.model.Books

@BindingAdapter(value = ["list", "onClick"])
fun initBooksList(
    view: RecyclerView,
    list: MutableList<Books>? = mutableListOf(),
    onClick: () -> Unit,
): Adapter<Books> {
    var adapter: Adapter<Books>? = null
    adapter = Adapter(
        R.layout.book_item,
        onClick = { position: Int, category: Books ->
            onClick()
        },
        onBind = { position: Int, item: Books, _: View, bind: ViewDataBinding ->
            val binding = bind as BookItemBinding
            with(binding) {
                book = item
            }
        }
    )
    view.adapter = adapter
    adapter.items = list?.toMutableList() ?: mutableListOf()
    return adapter
}
