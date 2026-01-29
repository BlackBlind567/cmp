package com.hybrid.internet.data.remote

import com.hybrid.internet.data.model.request.PlanRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class PlanApi(
    private val client: HttpClient
) {

    suspend fun fetchPlan(): HttpResponse {
        return client.post("/api/customer-plans-api")
//            url {
//                parameters.append("page", page.toString())
//            }
//        }
    }

    suspend fun updatePlan(request: PlanRequest): HttpResponse {
        return client.post("/api/customer-qty-update-api") {
            setBody(request)
        }
    }

    suspend fun fetchPlanTracking(page: Int): HttpResponse {
        return client.post("/api/customer-upgrade-request-api"){
            url {
               parameters.append("page", page.toString())
            }
        }}
}