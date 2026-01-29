package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceRequestItem(
    val id: Int? = null,
    @SerialName("location_name") val locationName: String? = null,
    @SerialName("bandwidth_capacity") val bandwidth: String? = null,
    @SerialName("bts_code_m6_code") val btsCode: String? = null,
    @SerialName("mux_id") val muxId: String? = null,
    @SerialName("bts_address") val address: String? = null,
    @SerialName("report_type") val reportType: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    val note: String? = null,
    val remark: String? = null,
    val reason: String? = null,

)