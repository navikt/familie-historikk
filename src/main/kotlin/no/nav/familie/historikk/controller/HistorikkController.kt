package no.nav.familie.historikk.controller

import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration


@RestController
@RequestMapping("/historikk")
@Unprotected
class HistorikkController {

    @GetMapping("/BA/123",  produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    private fun hentHistorikk(): Flux<String?> {
        val tregFlux: Flux<String> = Flux.fromIterable(listOf("1","2","3", "4", "5", "6", "7")).delayElements(Duration.ofMillis(1000*180))
        val raskFlux: Flux<String> = Flux.fromIterable(listOf("a", "b", "c")).delayElements(Duration.ofMillis(1000))

        return Flux.concat(raskFlux,tregFlux)

    }
}

