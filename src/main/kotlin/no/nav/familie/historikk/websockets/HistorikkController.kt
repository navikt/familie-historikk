package no.nav.familie.historikk.websockets

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.text.SimpleDateFormat
import java.util.Date

@Controller
class ChatController {

    @MessageMapping("/historikk")
    @SendTo("/topic/messages") @Throws(Exception::class)
    fun send(message: HistorikkInnMelding): HistorikkUtMelding {
        val time = SimpleDateFormat("HH:mm").format(Date())
        return HistorikkUtMelding(message.from!!, message.text!!, time)
    }
}