package no.nav.familie.historikk.domain

import no.nav.familie.historikk.common.repository.InsertUpdateRepository
import no.nav.familie.historikk.common.repository.RepositoryInterface
import no.nav.familie.kontrakter.felles.Applikasjon
import java.util.UUID

interface HistorikkinnslagRepository : RepositoryInterface<Historikkinnslag, UUID>, InsertUpdateRepository<Historikkinnslag> {

    fun findByBehandlingId(behandlingId: String): List<Historikkinnslag>

    fun findByBehandlingIdAndApplikasjon(behandlingId: String, applikasjon: Applikasjon): List<Historikkinnslag>
}
