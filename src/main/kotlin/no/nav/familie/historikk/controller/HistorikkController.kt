package no.nav.familie.historikk.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration


@RestController
@RequestMapping("/historikk")
class HistorikkController {

    @GetMapping("/{applikasjon}/{behandlingId}",  produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    private fun getAllEmployees(): Flux<String?> {

       return  Flux.fromIterable(listOf("a", "b", "c")) //hent data fra postgres
                .delayElements(Duration.ofMillis(100))
                //.doOnNext(kafkaservice::lyttPåeventsForBehandling) // lytt på nye eventer fra kafka
    }
}

