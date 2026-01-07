package com.business.cmpproject.presentation.features.home

import com.business.cmpproject.core.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiEvent.ShowSnackBar
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.data.model.response.HomeResponse
import com.business.cmpproject.data.model.response.Invoice
import com.business.cmpproject.data.model.response.Ticket
import com.business.cmpproject.domain.repository.dashboard.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenModel (
    private val repo: DashboardRepository
) : BaseScreenModel() {
    private val _state =
        MutableStateFlow<UiState<HomeResponse>>(UiState.Loading)
    val state: StateFlow<UiState<HomeResponse>> = _state

    init {
        println("HomeScreenModel init")
        loadDashboard()
    }

    fun loadDashboard() {
        println("HomeScreenModel loadDashboard")
        screenModelScope.launch {
            try {
            when (val result = repo.getDashboard()) {

                is NetworkResult.Success -> {
                    _state.value =
                        UiState.Success(result.data)
                }

                is NetworkResult.Failure -> {
                    _state.value = UiState.Error(result.error.message)
                    sendEvent(
                        ShowSnackBar(
                            message = result.error.message,
                            isError = true
                        )
                    )
                }

                else -> {}
            }
        } catch (e: Exception) {
            println("Dashboard API Crash: ${e.message}") // Look for this in logs!
            _state.value = UiState.Error(e.message ?: "Unknown Connection Error")
        }
        }
    }

//    fun HomeResponse.toUiModel(): HomeResponse =
//        HomeResponse(
//            totalActivePlans = totalActivePlans,
//            openTickets = openTickets,
//            pendingTickets = pendingTickets,
//            closedTickets = closedTickets,
//            totalDueAmount = totalDueAmount,
//            thisMonthInvoiceAmount = thisMonthInvoiceAmount,
//            recentTickets = recentTickets!!.map {
//                Ticket(
//                    id = it.id,
//                    ticketId = it.ticketId,
//                    category = it.category,
//                    status = it.status,
//                    createdAt = it.createdAt,
//                    updatedAt = it.updatedAt,
//
//                )
//            },
//            recentInvoices = recentInvoices!!.map {
//                Invoice(
//                    id = it.id,
//                    invoiceNo = it.invoiceNo,
//                    invoiceDate = it.invoiceDate,
//                    netAmount = it.netAmount,
//                    paymentStatus = it.paymentStatus,
//                    pdfFile = it.pdfFile,
//                    recurring = it.recurring,
//                    status = it.status,
//                    type = it.type,
//                    cgst = it.cgst,
//                    sgst = it.sgst,
//                    igst = it.igst,
//                    gstSlab = it.gstSlab,
//                    roundOfValue = it.roundOfValue,
//                    balanceAmount = it.balanceAmount,
//                    countMail = it.countMail,
//                    discount = it.discount,
//                    grossAmount = it.grossAmount,
//                    grossAmountBeforeDiscount = it.grossAmountBeforeDiscount,
//                    paidAmount = it.paidAmount,
//                    totalDiscountAmount = it.totalDiscountAmount,
//                    walletDebit = it.walletDebit
//                )
//            }
//        )
}