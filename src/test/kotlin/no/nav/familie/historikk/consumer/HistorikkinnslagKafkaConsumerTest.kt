package no.nav.familie.historikk.consumer

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.historikk.OppslagSpringRunnerTest
import no.nav.familie.historikk.common.Constants
import no.nav.familie.historikk.domain.HistorikkinnslagRepository
import no.nav.familie.kontrakter.felles.Applikasjon
import no.nav.familie.kontrakter.felles.Fagsystem
import no.nav.familie.kontrakter.felles.historikkinnslag.Aktør
import no.nav.familie.kontrakter.felles.historikkinnslag.Historikkinnslagstype
import no.nav.familie.kontrakter.felles.historikkinnslag.OpprettHistorikkinnslagRequest
import no.nav.familie.kontrakter.felles.objectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.support.Acknowledgment
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class HistorikkinnslagKafkaConsumerTest : OppslagSpringRunnerTest() {
    @Autowired
    private lateinit var kafkaConsumer: HistorikkinnslagKafkaConsumer

    @Autowired
    private lateinit var historikkinnslagRepository: HistorikkinnslagRepository

    private lateinit var acknowledgment: Acknowledgment

    @BeforeEach
    fun init() {
        acknowledgment = mockk()
        every { acknowledgment.acknowledge() } returns Unit
    }

    @Test
    fun `listen skal lagre historikkinnslag når mottatt gyldig melding`() {
        val behandlingId = UUID.randomUUID().toString()
        val opprettetTidspunkt = LocalDateTime.now()
        val request =
            OpprettHistorikkinnslagRequest(
                behandlingId = behandlingId,
                eksternFagsakId = UUID.randomUUID().toString(),
                fagsystem = Fagsystem.BA,
                applikasjon = Applikasjon.FAMILIE_TILBAKE,
                type = Historikkinnslagstype.HENDELSE,
                aktør = Aktør.SAKSBEHANDLER,
                aktørIdent = "Z0000",
                opprettetTidspunkt = opprettetTidspunkt,
                tittel = "Behandling Opprettet",
            )

        kafkaConsumer.listen(
            consumerRecord =
                ConsumerRecord(
                    Constants.TOPIC,
                    1,
                    0L,
                    behandlingId,
                    objectMapper.writeValueAsString(request),
                ),
            ack = acknowledgment,
        )

        val historikkinnslagene = historikkinnslagRepository.findByBehandlingId(behandlingId)
        assertTrue { historikkinnslagene.isNotEmpty() }
        assertEquals(1, historikkinnslagene.size)
        val historikkinnslag = historikkinnslagene.first()
        assertEquals(behandlingId, historikkinnslag.behandlingId)
        assertEquals(request.eksternFagsakId, historikkinnslag.eksternFagsakId)
        assertEquals(request.fagsystem, historikkinnslag.fagsystem)
        assertEquals(request.applikasjon, historikkinnslag.applikasjon)
        assertEquals(request.aktør, historikkinnslag.aktør)
        assertEquals(request.aktørIdent, historikkinnslag.opprettetAv)
        assertEquals(request.opprettetTidspunkt, opprettetTidspunkt)
        assertEquals(request.type, historikkinnslag.type)
        assertEquals(request.tittel, historikkinnslag.tittel)
        assertNull(historikkinnslag.tekst)
        assertNull(historikkinnslag.dokumentId)
        assertNull(historikkinnslag.journalpostId)
    }

    @Test
    fun `listen skal ikke lagre historikkinnslag når mottatt ugyldig melding`() {
        val behandlingId = UUID.randomUUID().toString()
        val request = "testverdi"
        kafkaConsumer.listen(
            consumerRecord =
                ConsumerRecord(
                    Constants.TOPIC,
                    1,
                    0L,
                    behandlingId,
                    objectMapper.writeValueAsString(request),
                ),
            ack = acknowledgment,
        )

        val historikkinnslag = historikkinnslagRepository.findByBehandlingId(behandlingId)
        assertTrue { historikkinnslag.isEmpty() }
    }
}
