package no.nav.familie.historikk.config

import no.nav.familie.historikk.common.Constants
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker


@Configuration
@EnableKafka
@Profile("local", "integrasjonstest")
class KafkaLokalConfig {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    fun embeddedKafkaBroker(): EmbeddedKafkaBroker {
        logger.info("Starter embedded Kafka")
        val embeddedKafka = EmbeddedKafkaBroker(1, true, Constants.topic)
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString())
        embeddedKafka.afterPropertiesSet()
        return embeddedKafka
    }

    @Bean
    fun getKafkaListenerEndpointRegistry(): KafkaListenerEndpointRegistry? {
        return KafkaListenerEndpointRegistry()
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }

    fun producerConfigs() = mapOf(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to System.getProperty("spring.kafka.bootstrap-servers"),
            ProducerConfig.ACKS_CONFIG to "all",
            CommonClientConfigs.RETRIES_CONFIG to 1,
            CommonClientConfigs.RETRY_BACKOFF_MS_CONFIG to 100,
    )
}
