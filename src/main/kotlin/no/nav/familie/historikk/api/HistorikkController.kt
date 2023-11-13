package no.nav.familie.historikk.api

import no.nav.familie.historikk.service.HistorikkService
import no.nav.familie.kontrakter.felles.Applikasjon
import no.nav.familie.kontrakter.felles.Ressurs
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/historikk")
@ProtectedWithClaims(issuer = "azuread")
@Validated
class HistorikkController(private val historikkService: HistorikkService) {

    @GetMapping(
        "/applikasjon/{applikasjon}/behandling/{behandlingId}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun hentHistorikkinnslag(
        @PathVariable("applikasjon") applikasjon: String,
        @PathVariable("behandlingId") behandlingId: String,
    ): Ressurs<List<HistorikkinnslagDto?>> {
        return Ressurs.success(historikkService.hentHistorikkinnslag(Applikasjon.valueOf(applikasjon), behandlingId))
    }
}
