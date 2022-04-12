package com.example.cleanarchflow.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.cleanarchflow.BaseApplication
import com.example.cleanarchflow.ui.home.model.Books
import com.example.cleanarchflow.ui.home.model.Event
import com.example.cleanarchflow.ui.home.model.Resource
import com.example.cleanarchflow.ui.home.model.Status
import com.example.cleanarchflow.ui.home.ui.theme.CleanArchitectureFlowTheme
import com.example.cleanarchflow.ui.home.viewmodel.BooksViewModel

class HomeActivity : ComponentActivity() {

    private val viewModel: BooksViewModel by viewModels {
        BooksViewModel.BooksViewModelFactory(
            application,
            (application as BaseApplication).getBooksUseCase,
            (application as BaseApplication).booksMapper,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanArchitectureFlowTheme {
                HomeView(viewModel)
            }
        }
    }
}

@Composable
fun HomeView(booksViewModel: BooksViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val books: Event<Resource<List<Books>>> by booksViewModel.remoteBooks.collectAsState(
        initial = Event(
            Resource.loading(null)
        )
    )
    Column() {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newText ->
                searchQuery = newText
            },
            label = { Text(text = "Enter Author Name") },
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.White),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    isLoading = true
                    booksViewModel.getBooks(searchQuery.text)
                }
            )
        )
        BooksList(books, isLoading)

    }
}

@Composable
fun BooksList(response: Event<Resource<List<Books>>>, isLoading: Boolean) {
    var selectedIndex by remember { mutableStateOf(-1) }
    val resp = response.peekContent()
    Box(
        contentAlignment = Alignment.Center, // you apply alignment to all children
        modifier = Modifier.fillMaxSize()
    ) {
        when (resp.status) {
            Status.LOADING -> {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center) // or to a specific child
                    )
                }
            }
            Status.SUCCESS -> {
                LazyColumn {
                    itemsIndexed(items = resp.data ?: listOf()) { index, item ->
                        BooksItem(books = item, index, selectedIndex) { i ->
                            selectedIndex = i
                        }
                    }
                }
            }
            Status.ERROR -> {

            }
        }

    }

}

@Composable
fun BooksItem(books: Books, index: Int, selectedIndex: Int, onClick: (Int) -> Unit) {
    val backgroundColor =
        if (index == selectedIndex) MaterialTheme.colors.primary else MaterialTheme.colors.background
    Card(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .clickable { onClick(index) }
            .height(110.dp), shape = RoundedCornerShape(8.dp), elevation = 4.dp
    ) {
        Surface(color = backgroundColor) {
            Row(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = books.imageUrl,

                        builder = {
                            scale(Scale.FILL)
                            transformations(CircleCropTransformation())

                        }
                    ),
                    "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .weight(0.8f)
                ) {
                    Text(
                        text = books.title,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CleanArchitectureFlowTheme {
        HomeView()
    }
}

