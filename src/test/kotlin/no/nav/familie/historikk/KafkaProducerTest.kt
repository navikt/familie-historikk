package no.nav.familie.historikk

import no.nav.familie.historikk.common.Constants
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate

internal class KafkaProducerTest :OppslagSpringRunnerTest(){

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String,String>

    @Test
    fun `send melding til kafka`(){
        val producerRecord = ProducerRecord<String,String>(Constants.topic, "test")
        kafkaTemplate.defaultTopic = Constants.topic
        kafkaTemplate.send(producerRecord)
    }

}
