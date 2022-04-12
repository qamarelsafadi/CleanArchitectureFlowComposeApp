package com.example.cleanarchflow.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cleanarchflow.BaseApplication
import com.example.cleanarchflow.base.Adapter
import com.example.cleanarchflow.databinding.ActivityMainBinding
import com.example.cleanarchflow.helper.initBooksList
import com.example.cleanarchflow.ui.home.model.Books
import com.example.cleanarchflow.ui.home.model.Status
import com.example.cleanarchflow.ui.home.viewmodel.BooksViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Adapter<Books>
    private val viewModel: BooksViewModel by viewModels {
        BooksViewModel.BooksViewModelFactory(
            application,
            (application as BaseApplication).getBooksUseCase,
            (application as BaseApplication).booksMapper,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            adapter = initBooksList(booksList) {}
        }
        viewModel.getBooks("Robert C. Martin")
        subscribeObservers()
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.remoteBooks.collect {
                    val resp = it.peekContent()
                    when (resp.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            adapter.items = resp.data ?: mutableListOf()
                        }
                        Status.ERROR -> {
                            Toast.makeText(this@MainActivity, resp.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

}