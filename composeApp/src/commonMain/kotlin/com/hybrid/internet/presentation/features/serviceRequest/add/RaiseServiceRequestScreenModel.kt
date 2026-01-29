package com.hybrid.internet.presentation.features.serviceRequest.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.domain.repository.serviceRequest.ServiceRequestRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RaiseServiceRequestScreenModel(
    private val repo: ServiceRequestRepository,
    // Pre-filled data from previous screen
    initialLocationId: String?,
    initialLocationName: String?
) : BaseScreenModel() {

    // Form States
    var reportType by mutableStateOf("ip_request")
    var locationName by mutableStateOf(initialLocationName ?: "")
    var locationId by mutableStateOf(initialLocationId ?: "")

    // Fields
    var userNameJaze by mutableStateOf("")
    var reason by mutableStateOf("")
    var remark by mutableStateOf("")
    var bandwidth by mutableStateOf("")
    var latLong by mutableStateOf("")
    var btsCode by mutableStateOf("")
    var muxId by mutableStateOf("")
    var btsAddress by mutableStateOf("")
    var note by mutableStateOf("")

    var isSubmitting by mutableStateOf(false)

    fun submitForm(onSuccess: (Boolean) -> Unit) {
        if (!validate()) return

        screenModelScope.launch {
            isSubmitting = true
            val params = mutableMapOf<String, String>().apply {
                put("report_type", reportType)
                put("location_id", locationId)
                put("location_name", locationName)
                put("remark", remark)

                when (reportType) {
                    "ip_request" -> {
                        put("user_name_jaze", userNameJaze)
                        put("reason", reason)
                    }
                    "feasibility" -> {
                        put("lat_long", latLong)
                        put("bts_code_m6_code", btsCode)
                        put("mux_id", muxId)
                        put("bts_address", btsAddress)
                    }
                    "new_link" -> {
                        put("bandwidth_capacity", bandwidth)
                        put("bts_code_m6_code", btsCode)
                        put("mux_id", muxId)
                        put("bts_address", btsAddress)
                        put("note", note)
                    }
                }
            }

            when (val result = repo.submitServiceRequest(params)) {
                is NetworkResult.Success -> {
                    sendEvent(UiEvent.ShowSnackBar("Data submitted successfully"))
                    delay(1500)
                    onSuccess(true)
                }
                is NetworkResult.Failure -> {
                    sendEvent(UiEvent.ShowSnackBar(result.error.toString(), isError = true))
                }
            }
            isSubmitting = false
        }
    }

    private fun validate(): Boolean {
        if (locationName.isEmpty()) return false
        return when (reportType) {
            "ip_request" -> userNameJaze.isNotEmpty() && reason.isNotEmpty()
            "feasibility" -> btsCode.isNotEmpty() && btsAddress.isNotEmpty()
            "new_link" -> bandwidth.isNotEmpty() && btsCode.isNotEmpty()
            else -> false
        }
    }
}