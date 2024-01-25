package no.nav.familie.historikk

import no.nav.familie.historikk.common.Constants
import no.nav.familie.historikk.consumer.HistorikkinnslagKafkaConsumer
import no.nav.familie.kontrakter.felles.Applikasjon
import no.nav.familie.kontrakter.felles.Fagsystem
import no.nav.familie.kontrakter.felles.historikkinnslag.Aktør
import no.nav.familie.kontrakter.felles.historikkinnslag.Historikkinnslagstype
import no.nav.familie.kontrakter.felles.historikkinnslag.OpprettHistorikkinnslagRequest
import no.nav.familie.kontrakter.felles.objectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

internal class KafkaProducerTest : OppslagSpringRunnerTest() {
    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var kafkaConsumer: HistorikkinnslagKafkaConsumer

    @Test
    fun `send melding til kafka`() {
        val behandlingId = UUID.randomUUID().toString()
        val request =
            OpprettHistorikkinnslagRequest(
                behandlingId = behandlingId,
                eksternFagsakId = UUID.randomUUID().toString(),
                fagsystem = Fagsystem.BA,
                applikasjon = Applikasjon.FAMILIE_TILBAKE,
                type = Historikkinnslagstype.HENDELSE,
                aktør = Aktør.SAKSBEHANDLER,
                aktørIdent = "Z0000",
                opprettetTidspunkt = LocalDateTime.now(),
                tittel = "Behandling Opprettet",
            )
        val producerRecord =
            ProducerRecord(
                Constants.TOPIC,
                behandlingId,
                objectMapper.writeValueAsString(request),
            )
        kafkaTemplate.send(producerRecord)

        kafkaConsumer.latch.await(1000, TimeUnit.MILLISECONDS)

        assertEquals(0L, kafkaConsumer.latch.count)
    }
}
