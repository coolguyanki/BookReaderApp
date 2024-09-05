package com.example.readerapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName


data class MBook(
    // here we are excluding the id
    @Exclude var id: String? = null,
    var title: String? = null,
    var authors: String? = null,
    var notes: String? = null,
    // we have done this because only in this structure firebase can save data with long names
    @get:PropertyName("book_photo_url")
    @set:PropertyName("book_photo_url")
    var photoUrl: String? = null,
    var categories: String? = null,
    @get:PropertyName("book_published_date")
    @set:PropertyName("book_published_date")
    var publishedDate: String? = null,
    var rating: Double? = null,
    var description: String? = null,
    @get: PropertyName("book_page_count")
    @set: PropertyName("book_page_count")
    var pageCount: String? = null,
    @get: PropertyName("book_start_reading")
    @set: PropertyName("book_start_reading")
    var startReading: Timestamp? = null,
    @get: PropertyName("book_finished_reading")
    @set: PropertyName("book_finished_reading")
    var finishedReading: Timestamp? = null,
    @get: PropertyName("user_id")
    @set: PropertyName("user_id")
    var userId: String? = null,
    @get: PropertyName("google_book_id")
    @set: PropertyName("google_book_id")
    var googleBookId: String? = null
)
