package com.example.baism_cw_3.model

data class CharacterResponse(
    val info: PageInfo,
    val results: List<Character>
)

data class PageInfo(
    val next: String?
)