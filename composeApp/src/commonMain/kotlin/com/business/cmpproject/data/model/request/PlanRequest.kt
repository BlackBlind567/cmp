package com.business.cmpproject.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanRequest (
    @SerialName("plan_id") val planId: Int,
    val reason: String,
    @SerialName("action_type") val actionType: String, // "upgrade", "downgrade", "terminate"
    @SerialName("new_qty") val newQty: Int? = null
)