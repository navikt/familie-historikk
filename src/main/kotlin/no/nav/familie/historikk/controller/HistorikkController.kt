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

       return  Flux.fromIterable(listOf("a", "b", "c")) //hent data fra postgres
                .delayElements(Duration.ofMillis(1000))
                //.doOnNext(kafkaservice::lyttPåeventsForBehandling) // lytt på nye eventer fra kafka
    }
}

