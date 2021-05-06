package no.nav.familie.historikk.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.server.support.DefaultHandshakeHandler

@Configuration @EnableWebSocketMessageBroker
class SendTilBrukerConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic/", "/queue/")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/greeting").setHandshakeHandler(object : DefaultHandshakeHandler() {
            //Get sessionId from request and set it in Map attributes
            @Throws(Exception::class) fun beforeHandshake(request: ServerHttpRequest?,
                                                          response: ServerHttpResponse?,
                                                          wsHandler: WebSocketHandler?,
                                                          attributes: MutableMap<String, String>): Boolean {
                if (request is ServletServerHttpRequest) {
                    val session = request.servletRequest.session
                    attributes["sessionId"] = session.id
                }
                return true
            }
        }).withSockJS()
    }
}