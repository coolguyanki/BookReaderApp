# Book Reader App

This is a **Book Reader App** built using **Android Kotlin** following **MVVM architecture** and various Android best practices. The app allows users to create accounts, browse books, read their details, and manage their reading lists. The app integrates **Retrofit** for API calls, **Dagger-Hilt** for dependency injection, **Room Database** for local storage, and **Firebase** for authentication and cloud database operations.

## Features

- **User Authentication**: 
  - Users can create an account using Firebase Authentication or log in with their existing credentials.
  - A **splash screen** is displayed on the first launch, followed by user authentication.

- **Home Screen**: 
  - Displays the **Recent Reading List** and **Most Recent Single Book** with user ratings.
  - Ratings are fetched from Firebase Firestore and displayed on the home screen.

- **Search Functionality**: 
  - Users can search for books using an API integrated via **Retrofit**.
  - On search results, users can click on a book to view its details.

- **Book Details Page**: 
  - Displays the book's **author name**, **release date**, and **description**.
  - Users can rate the book, and their rating is saved and displayed on the home screen.

- **Room Database Integration**: 
  - Book data is stored locally in the **Room Database** to manage the user's reading list.

- **Firebase Firestore**: 
  - CRUD operations are performed using **Firebase Firestore** for managing user-specific data, such as reading lists and ratings.

## Technologies and Libraries Used

- **Kotlin**: For Android development.
- **MVVM Architecture**: For separating UI logic, repository management, and data handling.
- **Retrofit**: For API calls to fetch book details and search results.
- **Dagger-Hilt**: For Dependency Injection, ensuring proper management of app components.
- **Room Database**: For storing books locally and managing reading history.
- **Firebase Authentication**: For user registration and login.
- **Firebase Firestore**: For storing and managing book ratings and reading lists.
- **Jetpack Components**: For implementing the lifecycle, ViewModel, and LiveData to manage UI and data.

## Screenshots
*(Optional: Add screenshots of the app to give users a visual idea of the interface)*

## Setup Instructions

To get the project running on your local machine:

1. Clone the repository:
   ```bash
   git clone https://github.com/your_username/your_repository_name.git
Open the project in Android Studio.
Sync Gradle files and build the project.
Set up Firebase Authentication and Firestore by following the official Firebase setup guide.
Run the project on an Android emulator or physical device.

Usage
After launching the app, the user is prompted to create an account or log in.
Once authenticated, users will be taken to the Home Screen, where they can see their Recent Reading List and the most recent book they rated.
Users can search for books using the search bar, and upon selecting a book, they are taken to the Book Details Page.
The Book Details Page shows the book’s information (author, release date, description) and allows users to rate the book.
Ratings are saved and displayed on the Home Screen along with the book in the Recent Reading List.

Future Enhancements 
Add offline mode to allow users to access their reading lists without an internet connection.
Add the ability to download and read books directly within the app.
Implement personalized recommendations based on user reading history.
