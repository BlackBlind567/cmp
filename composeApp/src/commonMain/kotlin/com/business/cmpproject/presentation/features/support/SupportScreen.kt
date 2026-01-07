package com.business.cmpproject.presentation.features.support

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.business.cmpproject.data.model.request.SupportRequest
import com.business.cmpproject.presentation.features.ticket.TicketScreenModel

class SupportScreen: Screen {
    @Composable
    override fun Content() {



        val screenModel = getScreenModel<SupportScreenModel>()

        val state by screenModel.state.collectAsState()



        val request = SupportRequest(
            altMobile = "9806821832", // Aap ise alag text field se le sakte hain
            altEmail = "rohitatoms02@gmail.com",
            category =  "",
            subcategory = "",
            message =  "No message",
            image = "data:image/png;base64,..." // Yahan aapko image ko Base64 mein convert karna hoga
        )

        screenModel.submitReport(request)
    }
}