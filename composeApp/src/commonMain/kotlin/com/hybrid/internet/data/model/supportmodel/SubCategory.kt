package com.hybrid.internet.data.model.supportmodel

data class SubCategory(
    val id: String,
    val name: String,
    val fields: List<String> // Ye fields input placeholders banenge
)

data class Category(
    val id: String,
    val name: String,
    val subCategories: List<SubCategory> = emptyList(),
    val fields: List<String> = emptyList()
)