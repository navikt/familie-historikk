package no.nav.familie.historikk.hendelse

import no.nav.familie.historikk.common.Constants
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
@Profile("prepod")
class HistorikkinnslagKafkaListener {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(id = "familie-historikk",
                   topics = [Constants.topic],
                   containerFactory = "concurrentKafkaListenerContainerFactory")
    fun listen(consumerRecord: ConsumerRecord<String, String>, ack: Acknowledgment) {
        logger.info("Data mottatt i kafka $consumerRecord")
    }
}
