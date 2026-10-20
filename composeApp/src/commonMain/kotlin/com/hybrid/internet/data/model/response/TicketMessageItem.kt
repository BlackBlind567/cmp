package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class TicketMessageItem(
    @SerialName("id")
    private val rawId: JsonElement? = null,

    @SerialName("ticket_id")
    val ticketId: String? = null,

    @SerialName("message")
    private val legacyMessage: String? = null,

    @SerialName("text")
    private val text: String? = null,

    @SerialName("image")
    private val legacyImage: String? = null,

    @SerialName("image_url")
    private val imageUrl: String? = null,

    @SerialName("attachment")
    private val rawAttachment: JsonElement? = null,
    val status: String? = null,

    @SerialName("sender_type")
    private val legacySenderType: String? = null,

    @SerialName("sender")
    private val sender: String? = null,

    @SerialName("sender_name")
    val senderName: String? = null,

    @SerialName("customer_name")
    val customerName: String? = null,

    @SerialName("company_name")
    val companyName: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,
    @kotlinx.serialization.Transient
    val localImage: ByteArray? = null,
    @kotlinx.serialization.Transient
    val localKey: String? = null
) {
    val id: String?
        get() = rawId?.jsonPrimitive?.contentOrNull

    val message: String?
        get() = text ?: legacyMessage

    val image: String?
        get() = listOf(imageUrl, legacyImage, attachment).firstOrNull { !it.isNullOrBlank() }

    val attachment: String?
        get() = when (val value = rawAttachment) {
            is JsonObject -> (value["image"] as? JsonPrimitive)?.contentOrNull
            is JsonPrimitive -> value.contentOrNull
            else -> null
        }

    val senderType: String?
        get() = sender ?: legacySenderType
}
