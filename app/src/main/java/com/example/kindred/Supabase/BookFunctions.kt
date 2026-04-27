package com.example.kindred.Supabase

import com.example.kindred.DataModels.Book
import com.example.kindred.DataModels.BookSuggestion
import io.github.jan.supabase.postgrest.postgrest


/**
 * Sends book data to Supabase
 */
suspend fun sendBookData(book: Book) {
    SupabaseClient.supabase.postgrest["books"]
        .insert(book)
}

/**
 * Gets all books from Supabase
 */
suspend fun getBooks(): List<Book> {
    return SupabaseClient.supabase.postgrest["books"]
        .select()
        .decodeList<Book>()
}

/**
 * Deletes book from Supabase
 */
suspend fun deleteBook(bookId: Int) {
    SupabaseClient.supabase.postgrest["books"]
        .delete {
            filter {
                eq("book_id", bookId)
            }
        }
}

/**
 * Gets book suggestion from Supabase
 */
suspend fun getBookSuggestions(): List<BookSuggestion> {
    return SupabaseClient.supabase.postgrest["suggestions"]
        .select {
            filter { eq("entity_type", "book") }
        }
        .decodeList<BookSuggestion>()
}

/**
 * deletes the book suggestion from Supabase
 */
suspend fun deleteBookSuggestion(suggestionId: Int) {
    SupabaseClient.supabase.postgrest["suggestions"]
        .delete {
            filter { eq("suggestion_id", suggestionId) }
        }
}

/**
 * Sends book suggestion to Supabase
 */
suspend fun sendBookSuggestionData(bookSuggestion: BookSuggestion) {
    SupabaseClient.supabase.postgrest["suggestions"]
        .insert(bookSuggestion)
}