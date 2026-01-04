package com.business.cmpproject.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Invoice(
    val id: Int? = null,
    @SerialName("customer_id") val customerId: Int? = null,
    @SerialName("branch_id") val branchId: Int? = null,
    @SerialName("count_mail") val countMail: Int? = null,
    @SerialName("gross_amount") val grossAmount: String? = null,
    @SerialName("net_amount") val netAmount: String? = null,
    @SerialName("paid_amount") val paidAmount: String? = null,
    @SerialName("balance_amount") val balanceAmount: String? = null,
    @SerialName("wallet_debit") val walletDebit: String? = null,
    val discount: String? = null,
    @SerialName("total_discount_amount") val totalDiscountAmount: String? = null,
    @SerialName("gross_amount_befor_discount") val grossAmountBeforeDiscount: String? = null,
    val cgst: String? = null,
    val sgst: String? = null,
    val igst: String? = null,
    @SerialName("gst_slabe") val gstSlab: Int? = null,
    @SerialName("round_of_value") val roundOfValue: String? = null,
    @SerialName("invoice_no") val invoiceNo: String? = null,
    @SerialName("invoice_date") val invoiceDate: String? = null,
    val month: String? = null,
    @SerialName("billing_days") val billingDays: Int? = null,
    val status: Int? = null,
    @SerialName("payment_status") val paymentStatus: String? = null,
    @SerialName("created_by") val createdBy: String? = null,
    val type: String? = null,
    val recurring: Int? = null,
    @SerialName("sign_img") val signImg: String? = null,
    @SerialName("upi_qr") val upiQr: String? = null,
    @SerialName("pdf_file") val pdfFile: String? = null
)