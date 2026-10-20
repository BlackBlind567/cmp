package com.hybrid.internet.domain.repository.ticket

import com.hybrid.internet.core.network.HttpError
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull

internal fun closedTicketError(body: JsonObject?): HttpError.TicketClosed? {
    if (body == null) return null
    if ((body["success"] as? JsonPrimitive)?.booleanOrNull != false) return null
    val detail = (body["data"] as? JsonPrimitive)?.contentOrNull
    val message = (body["message"] as? JsonPrimitive)?.contentOrNull
    val reason = listOfNotNull(detail, message).firstOrNull {
        it.contains("ticket is closed", ignoreCase = true)
    } ?: return null
    return HttpError.TicketClosed(reason)
}
