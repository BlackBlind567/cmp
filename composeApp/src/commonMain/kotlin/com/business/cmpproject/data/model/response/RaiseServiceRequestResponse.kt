package com.business.cmpproject.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RaiseServiceRequestResponse(
    val id: Int? = null,
    @SerialName("customer_id") val customerId: Int? = null,
    @SerialName("location_name") val locationName: String? = null,
    @SerialName("user_name_jaze") val userNameJaze: String? = null,
    @SerialName("bandwidth_capacity") val bandwidth: String? = null,
    @SerialName("bts_code_m6_code") val btsCode: String? = null,
    @SerialName("mux_id") val muxId: String? = null,
    @SerialName("lat_long") val latLong: String? = null,
    @SerialName("bts_address") val btsAddress: String? = null,
    val note: String? = null,
    val reason: String? = null,
    val remark: String? = null,
    @SerialName("report_type") val reportType: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)