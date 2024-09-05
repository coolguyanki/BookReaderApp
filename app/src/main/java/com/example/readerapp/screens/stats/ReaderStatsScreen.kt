package com.example.readerapp.screens.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.model.Item
import com.example.readerapp.model.MBook
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.home.HomeScreenViewModel
import com.example.readerapp.screens.search.BookRow
import com.example.readerapp.utils.formatDate
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderStatsScreen(navController: NavController,
                      viewModel: HomeScreenViewModel = hiltViewModel()){

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser
    
    Scaffold(topBar = {
        ReaderAppBar(title = "Book Stats",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController) {
            navController.popBackStack()
        }
    }) {
        Surface() {
            // only want to show books which user have read
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)

                }
            } else {
                emptyList()
            }

            Column {
                Row {
                    Box(modifier = Modifier
                        .size(45.dp)
                        .padding(45.dp)) {
                        Icon(imageVector = Icons.Sharp.Person,
                            contentDescription ="icon")
                    }
                    Text(text = "Hi, ${currentUser?.email.toString().split("@")[0].uppercase()}")

                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                    shape = CircleShape,
                    elevation = 5.dp) {
                    val readBooksList: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        books.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    } else {
                        emptyList()
                    }
                    val readingBooks = books.filter { mBook ->
                        (mBook.startReading != null && mBook.finishedReading == null)
                    }

                    Column(modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text(text = "Your Stats", style = MaterialTheme.typography.h5)
                        Divider()
                        Text(text = "You 're reading: ${readingBooks.size} books")
                        Text(text = "You 've read: ${readBooksList.size} books")


                    }

                }

                if (viewModel.data.value.loading == true) {
                    LinearProgressIndicator()
                } else {
                    Divider()
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        contentPadding = PaddingValues(16.dp)) {
                        // filter books by finsished ones
                        val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                            viewModel.data.value.data!!.filter { mBook ->
                                (mBook.userId == currentUser?.uid) &&(mBook.finishedReading != null)
                            }
                        } else {
                            emptyList()
                        }

                        items(items = readBooks) {book ->
                            BookRowStats(book = book)
                        }

                    }
                }


            }

        }
        
    }



}

@Composable
fun BookRowStats(book: MBook) {

    Card(modifier = Modifier
        .clickable {
//            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
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
            val imageUrl: String = if (book.photoUrl.toString().isEmpty()) {
                "http://books.google.com/books/content?id=ZthJlG4o-2wC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"
            } else {
                book.photoUrl.toString()
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

                Row (horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = book.title.toString(),
                        overflow = TextOverflow.Ellipsis)

                    if (book.rating!! >= 4) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Thumbs Up",
                            tint = Color.Red.copy(alpha = 0.5f))
                    } else {
                        Box{}
                    }


                }


                Text(
                    text = "Author: ${book.authors}",
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )

                Text(
                    text = "Started: ${formatDate(book.startReading!!)}",
                    softWrap = true,
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Finished ${formatDate(book.finishedReading!!)}",
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )

            }

        }

    }

}