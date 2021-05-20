package no.nav.familie.historikk.controller

import no.nav.familie.historikk.domain.Historikkinnslag
import no.nav.familie.historikk.service.HistorikkService
import no.nav.familie.kontrakter.felles.Applikasjon
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/stream/historikk")
class HistorikkStreamController(private val historikkService: HistorikkService) {

    @GetMapping("/{applikasjon}/{behandlingId}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    private fun hentHistorikk(applikasjon: String, behandlingId: String): Flux<Historikkinnslag?> {
        return Flux.fromIterable(historikkService.hentHistorikkinnslag(Applikasjon.valueOf(applikasjon),behandlingId))
        //TODO plugg på eventlistener for kafka meldinger som kommer i ettertid
    }
}