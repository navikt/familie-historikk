package no.nav.familie.historikk.consumer

import com.fasterxml.jackson.databind.JsonMappingException
import no.nav.familie.historikk.common.Constants
import no.nav.familie.historikk.service.HistorikkService
import no.nav.familie.kontrakter.felles.historikkinnslag.OpprettHistorikkinnslagRequest
import no.nav.familie.kontrakter.felles.objectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
@Profile("!e2e")
class HistorikkinnslagKafkaConsumer(private val historikkService: HistorikkService) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    var latch: CountDownLatch = CountDownLatch(1)

    @KafkaListener(
        id = "familie-historikk",
        topics = [Constants.topic],
        containerFactory = "concurrentKafkaListenerContainerFactory",
    )
    fun listen(consumerRecord: ConsumerRecord<String, String>, ack: Acknowledgment) {
        logger.info("Data mottatt i kafka $consumerRecord")
        secureLogger.info("Data mottatt i kafka $consumerRecord")
        val data: String = consumerRecord.value()
        try {
            val request: OpprettHistorikkinnslagRequest = objectMapper.readValue(data, OpprettHistorikkinnslagRequest::class.java)
            historikkService.lagreHistorikkinnslag(request)
        } catch (exception: JsonMappingException) {
            logger.error("Ugyldig request mottatt for key=${consumerRecord.key()} med feilmelding ${exception.message}")
            secureLogger.error("Ugyldig request mottatt for key=${consumerRecord.key()} med feilmelding ${exception.message}")
        }
        latch.countDown()
        ack.acknowledge()
    }
}
