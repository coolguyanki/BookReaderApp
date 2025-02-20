package com.example.readerapp.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.components.RoundedButton
import com.example.readerapp.data.Resource
import com.example.readerapp.model.Item
import com.example.readerapp.model.MBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookDetailsScreen(
    navController: NavController, bookId: String,
    viewModel: DetailViewModel = hiltViewModel()
) {

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Book Details",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {

        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                // here get the issue of suspend function only invoke with coroutine which solved by following line
                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId)
                }.value

                if (bookInfo.data == null) {
                    // Here make our loader text with loading
                    Row() {
                        LinearProgressIndicator()
                        Text(text = "Loading...")
                    }
                } else {
                    ShowBookDetails(bookInfo, navController)
                }

            }

        }
    }
}

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavController) {

    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Card(
        modifier = Modifier.padding(35.dp),
        shape = CircleShape,
        elevation = 4.dp
    ) {
        Image(
            painter = rememberImagePainter(data = bookData!!.imageLinks.thumbnail),
            contentDescription = "Book detail image",
            modifier = Modifier
                .width(190.dp)
                .height(190.dp)
                .padding(1.dp)
        )
    }

    Text(
        text = bookData!!.title,
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis,
        maxLines = 19
    )

    Text(text = "Authors: ${bookData.authors}")
    Text(text = "Page Count: ${bookData.pageCount}")

    Text(
        text = "Categories: ${bookData.categories}",
        style = MaterialTheme.typography.subtitle1,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )

    Text(
        text = "Published: ${bookData.publishedDate}",
        style = MaterialTheme.typography.subtitle1
    )

    Spacer(modifier = Modifier.height(5.dp))

    // Here we do this because in our description tag we are getting the html codes
    val cleanDescription = HtmlCompat.fromHtml(
        bookData.description,
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()

    val localDims = LocalContext.current.resources.displayMetrics

    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {
                Text(text = cleanDescription)
            }

        }
    }

    //Buttons Save and Cancel
    Row(
        modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        RoundedButton(label = "Save") {
            val book = MBook(
                title = bookData?.title.toString(),
                authors = bookData.authors.toString(),
                description = bookData.description.toString(),
                categories = bookData.categories.toString(),
                notes = "",
                photoUrl = bookData.imageLinks.thumbnail,
                pageCount = bookData.pageCount.toString(),
                rating = 0.0,
                googleBookId = googleBookId,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            )

            saveToFirebase(book = book, navController = navController)

        }

        Spacer(modifier = Modifier.width(25.dp))
        RoundedButton(label = "Cancel") {
            navController.popBackStack()
        }

    }

}

fun saveToFirebase(book: Any, navController: NavController) {
    // Saving this book to firebase database
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }

                    }.addOnFailureListener {
                        Log.d("ERROR", "SaveToFireBase: Error uploading Doc", it)
                    }

            }
    } else {

    }


}
