package com.business.cmpproject.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SupportRequest(
    @SerialName("alt_mobile")
    val altMobile: String,

    @SerialName("alt_email")
    val altEmail: String,

    @SerialName("category")
    val category: String,

    @SerialName("subcategory")
    val subcategory: String,

    @SerialName("message")
    val message: String,
    
    @SerialName("image")
    val image: String? = null
)