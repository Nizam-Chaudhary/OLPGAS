package com.example.olpgas.auth.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://femgcgptgzpajrzfyaer.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZlbWdjZ3B0Z3pwYWpyemZ5YWVyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTAzMzMwNjcsImV4cCI6MjAyNTkwOTA2N30.JumWW-1TWVVGeE9offdn8gy3Ijmi96_mICebiK0gKaw"
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}