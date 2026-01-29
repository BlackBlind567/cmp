package com.hybrid.internet.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Payments (
    val id: Int,
    val customer_id: Int,
    val branch_id: Int,
    val amount: String,
    val due_amt: String,
    val charges: String,
    val tds: Double,
    val tds_percent: String,
    val payment_date: String,
    val mode: String,
    val deposit_to: String,
    val ref_no: String,
    val remark: String,
    val del_remark: String,
    val del_by: String,
    val status: Int
)