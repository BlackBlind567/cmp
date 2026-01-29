package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanRequestItem(
    val id: Int,
    val item: String,
    val location: String,
    @SerialName("action_type") val actionType: String,
    @SerialName("old_qty") val oldQty: Int,
    @SerialName("new_qty") val newQty: Int,
    @SerialName("old_amount") val oldAmount: String,
    @SerialName("new_amount") val newAmount: String,
    val reason: String,
    val status: String, // Pending, Rejected, Approved
    @SerialName("remark") val adminRemark: String?
)