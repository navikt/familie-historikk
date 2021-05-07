package no.nav.familie.historikk.websockets

import com.google.gson.Gson
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class SendTilBrukerController {

    private val gson: Gson = Gson()

    @MessageMapping("/message")
    @SendToUser("/queue/reply") @Throws(Exception::class)
    fun processMessageFromClient(@Payload message: String?, principal: Principal?): String {
        return gson.fromJson(message, MutableMap::class.java).get("fra").toString()
    }

    @MessageExceptionHandler @SendToUser("/queue/errors") fun handleException(exception: Throwable): String? {
        return exception.message
    }
}