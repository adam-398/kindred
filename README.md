# Kindred
A media tracking app for movies, TV shows, books, and audiobooks built with Kotlin and Jetpack Compose.

Kindred lets you log and organise your media across four categories, track what you have watched or read, manage wishlists, and import your existing Audible library directly from a JSON export. Authentication and data storage are handled via Supabase.

 **Note: This project is actively in development. Current functionality is outlined below.**

## Current Features

- **Add books** - log title, author, rating, genres, themes, and notes with Google Books autocomplete
- **Add audiobooks** - log title, author, narrator, genres, themes and notes
- **Add movies** - log title, director, writer and stars along with genres, themes and notes
- **Add tv shows** - log title, director, writer and stars along with genres, themes and notes
- **Audible import** - parse and import your Audible library or wishlist from a JSON export
- **Authentication** - email and password login via Supabase Auth

## Roadmap

- AI-powered recommendations based on favourites, themes, genres with weightings of importance, API 
- Play Store release

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Backend & Auth | Supabase |
| State Management | ViewModel + StateFlow |
| Navigation | Jetpack Navigation Compose |
| Serialisation | kotlinx.serialization |

## APIs

| API | Usage |
|---|---|
| [Google Books API](https://developers.google.com/books) | Live title autocomplete when adding books |
| [Supabase](https://supabase.com) | Authentication and cloud data storage |

