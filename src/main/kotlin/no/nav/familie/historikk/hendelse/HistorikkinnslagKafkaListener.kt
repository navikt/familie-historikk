package no.nav.familie.historikk.hendelse

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class HistorikkinnslagKafkaListener {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(id = "familie-historikk",
                   topics = ["historikk-topikk"])
    fun listen(consumerRecord: ConsumerRecord<String, String>, ack: Acknowledgment) {
        logger.info("Data mottatt i kafka $consumerRecord")
    }
}
