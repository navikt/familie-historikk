package no.nav.familie.historikk

import no.nav.familie.historikk.common.Constants
import no.nav.familie.historikk.hendelse.HistorikkinnslagKafkaConsumer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
internal class KafkaProducerTest : OppslagSpringRunnerTest() {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var kafkaConsumer: HistorikkinnslagKafkaConsumer

    @Test
    fun `send melding til kafka`() {
        val producerRecord = ProducerRecord<String, String>(Constants.topic, "test","test")
        kafkaTemplate.defaultTopic = Constants.topic
        kafkaTemplate.send(producerRecord)

        kafkaConsumer.latch.await(1000, TimeUnit.MILLISECONDS)

        assertEquals(0L, kafkaConsumer.latch.count)
    }

}
