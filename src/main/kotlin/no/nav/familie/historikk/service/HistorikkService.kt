package no.nav.familie.historikk.service

import no.nav.familie.historikk.domain.Historikkinnslag
import no.nav.familie.historikk.domain.HistorikkinnslagRepository
import no.nav.familie.kontrakter.felles.historikkinnslag.OpprettHistorikkinnslagRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HistorikkService(private val historikkinnslagRepository: HistorikkinnslagRepository) {

    @Transactional
    fun lagreHistorikkinnslag(request: OpprettHistorikkinnslagRequest) {
        val historikkinnslag = Historikkinnslag(behandlingId = request.behandlingId,
                                                eksternFagsakId = request.eksternFagsakId,
                                                fagsystem = request.fagsystem,
                                                applikasjon = request.applikasjon,
                                                aktør = request.aktør,
                                                type = request.type,
                                                tittel = request.tittel,
                                                tekst = request.tekst,
                                                steg = request.steg,
                                                journalpostId = request.journalpostId,
                                                dokumentId = request.dokumentId,
                                                opprettetAv = request.aktørIdent)
        historikkinnslagRepository.insert(historikkinnslag)
    }
}
