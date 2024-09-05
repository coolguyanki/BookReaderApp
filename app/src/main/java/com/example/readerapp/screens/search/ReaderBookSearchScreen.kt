package com.example.readerapp.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.model.Item
import com.example.readerapp.navigation.ReaderScreens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController,
                 viewModel: BookSearchViewModel = hiltViewModel()) {

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Search",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }
    }) {
        Surface() {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                   viewModel = viewModel) { query ->
                    viewModel.searchBooks(query)

                }
                Spacer(modifier = Modifier.height(8.dp))
                BookList(navController, viewModel)

            }

        }

    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun BookList(navController: NavController, viewModel: BookSearchViewModel = hiltViewModel()) {

    val listOfBooks = viewModel.list

//    if (viewModel.isLoading) {
//        LinearProgressIndicator()
//    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = listOfBooks) { book ->
                BookRow(book, navController)
            }

        }
//    }




}

@Composable
fun BookRow(book: Item, navController: NavController) {

    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            val imageUrl: String = if (book.volumeInfo.imageLinks.smallThumbnail.isEmpty()) {
                "http://books.google.com/books/content?id=ZthJlG4o-2wC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"
            } else {
                book.volumeInfo.imageLinks.smallThumbnail.toString()
            }

            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )

            Column() {

                Text(text = book.volumeInfo.title,
                    overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Author: ${book.volumeInfo.authors}",
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )
                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = "Date: ${book.volumeInfo.publishedDate}",
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "${book.volumeInfo.categories}",
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )

            }

        }

    }

}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    viewModel: BookSearchViewModel,
    onSearch: (String) -> Unit = {},
) {
    Column() {

        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current


        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        OutlinedTextField(modifier = modifier,
            value = searchQueryState.value,
            onValueChange = { searchQueryState.value = it },
            label = { Text(hint) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })
        )

    }

}