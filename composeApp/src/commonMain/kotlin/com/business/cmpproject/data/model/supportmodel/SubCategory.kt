package com.business.cmpproject.data.model.supportmodel

data class SubCategory(
    val id: String,
    val name: String,
    val fields: List<String> // Ye fields input placeholders banenge
)

data class Category(
    val id: String,
    val name: String,
    val subCategories: List<SubCategory>
)