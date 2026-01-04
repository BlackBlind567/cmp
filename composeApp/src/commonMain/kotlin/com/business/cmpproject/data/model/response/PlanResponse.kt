package com.business.cmpproject.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanResponse(
    val id: Int? = null,
    @SerialName("customer_id") val customerId: Int? = null,
    val customer: String? = null,
    @SerialName("branch_id") val branchId: String? = null,
    @SerialName("branch_name") val branchName: String? = null,
    @SerialName("branch_code") val branchCode: String? = null,
    @SerialName("branch_mobile") val branchMobile: String? = null,
    @SerialName("branch_email") val branchEmail: String? = null,

    @SerialName("item_id") val itemId: Int? = null,
    val item: String? = null,
    @SerialName("item_unit") val itemUnit: String? = null,
    val gst: String? = null,
    @SerialName("location_id") val locationId: Int? = null,
    @SerialName("master_end_b_id") val masterEndBId: Int? = null,

    @SerialName("end_a") val endA: String? = null,
    @SerialName("end_b") val endB: String? = null,
    val remark: String? = null,
    @SerialName("circuit_id") val circuitId: String? = null,
    @SerialName("location_name") val locationName: String? = null,

    val price: String? = null,
    val qty: Int? = null,
    val amount: String? = null,
    @SerialName("gross_amount") val grossAmount: String? = null,

    @SerialName("discount_value") val discountValue: Int? = null,
    @SerialName("discount_type") val discountType: String? = null,
    @SerialName("billing_cycle") val billing_cycle: String? = null,
    @SerialName("one_time_bill") val oneTimeBill: Int? = null,

    @SerialName("start_date") val startDate: String? = null,
    @SerialName("end_date") val endDate: String? = null,
    val status: Int? = null
)