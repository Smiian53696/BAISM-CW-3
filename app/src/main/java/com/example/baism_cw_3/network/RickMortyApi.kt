package com.example.baism_cw_3.network

import com.example.baism_cw_3.model.Character
import com.example.baism_cw_3.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApi {

    // Pobieranie listy postaci
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): CharacterResponse

    // Pobieranie postaci po ID
    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Character
}