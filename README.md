# Kindred
A media tracking app for movies, TV shows, books, and audiobooks — built with Kotlin and Jetpack Compose.

Kindred lets you log and organise your media across four categories, track what you have watched or read, manage wishlists, and import your existing Audible library directly from a JSON export. Authentication and data storage are handled via Supabase, keeping your collection synced across devices.

## Features

- **Books & Audiobooks** — log titles, authors, ratings, genres, themes, and notes
- **Movies & TV** — track watched status and personal ratings
- **Google Books autocomplete** — title search with live suggestions to speed up adding entries
- **Audible import** — parse and import your Audible library or wishlist directly from a JSON export
- **Authentication** — email and password login via Supabase Auth
- **Cloud storage** — all data persisted to Supabase per user account
- **Wishlist & read/watched status** — filter and manage your backlog separately from completed entries
- **Favourites** — mark any entry as a favourite

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

## Getting Started

This is a university project — the repository is available privately upon request.

The project requires a Supabase project with the following tables: `books`, `audiobooks`, `movies`, `tv`. A `.env` or local config file with your Supabase URL and anon key is needed to build locally.
