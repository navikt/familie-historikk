package no.nav.familie.historikk.hendelse

import no.nav.familie.historikk.common.Constants
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class HistorikkinnslagKafkaConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    var latch: CountDownLatch = CountDownLatch(1)

    @KafkaListener(id = "familie-historikk",
                   topics = [Constants.topic],
                   containerFactory = "concurrentKafkaListenerContainerFactory")
    fun listen(consumerRecord: ConsumerRecord<String, String>) {
        logger.info("Data mottatt i kafka $consumerRecord")
        latch.countDown()
    }
}
