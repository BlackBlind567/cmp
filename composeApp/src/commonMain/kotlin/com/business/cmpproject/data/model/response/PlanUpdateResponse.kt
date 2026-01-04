package com.business.cmpproject.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanUpdateResponse(
    @SerialName("customer_id") val customerId: Int? = null,
    @SerialName("plan_id") val planId: Int? = null,
    val type: String? = null,
    @SerialName("old_qty") val oldQty: Int? = null,
    @SerialName("new_qty") val newQty: Int? = null,
    @SerialName("old_amount") val oldAmount: Double? = null,
    @SerialName("new_amount") val newAmount: Double? = null,
    val reason: String? = null,
    val id: Int? = null
)