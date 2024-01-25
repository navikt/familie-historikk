package no.nav.familie.historikk.api

import no.nav.familie.historikk.service.HistorikkService
import no.nav.familie.kontrakter.felles.Applikasjon
import no.nav.familie.kontrakter.felles.Ressurs
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/historikk/stream")
class HistorikkStreamController(private val historikkService: HistorikkService) {
    @GetMapping(
        "/applikasjon/{applikasjon}/behandling/{behandlingId}",
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE],
    )
    fun hentHistorikkinnslag(
        @PathVariable("applikasjon") applikasjon: String,
        @PathVariable("behandlingId") behandlingId: String,
    ): Ressurs<Flux<HistorikkinnslagDto?>> {
        return Ressurs.success(
            Flux.fromIterable(
                historikkService.hentHistorikkinnslag(
                    Applikasjon.valueOf(applikasjon),
                    behandlingId,
                ),
            ),
        )
        // TODO plugg på eventlistener for kafka meldinger som kommer i ettertid
    }
}
