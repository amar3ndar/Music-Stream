package com.amar3ndar.musicly.models

data class CategoryModel(
    val name: String,
    val coverUrl: String,
    val songs:List<String>,
){
    constructor(): this("","", listOf())
}
