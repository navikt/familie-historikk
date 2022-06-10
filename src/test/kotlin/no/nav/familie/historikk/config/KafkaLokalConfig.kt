package no.nav.familie.historikk.config

import no.nav.familie.historikk.common.Constants
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.test.EmbeddedKafkaBroker

@Configuration
@EnableKafka
@Profile("local", "integrasjonstest")
class KafkaLokalConfig {

    @Bean
    fun broker(): EmbeddedKafkaBroker {
        return EmbeddedKafkaBroker(1)
            .kafkaPorts(9092)
            .brokerProperty("listeners", "PLAINTEXT://localhost:9092,REMOTE://localhost:9093")
            .brokerProperty("advertised.listeners", "PLAINTEXT://localhost:9092,REMOTE://localhost:9093")
            .brokerProperty("listener.security.protocol.map", "PLAINTEXT:PLAINTEXT,REMOTE:PLAINTEXT")
            .brokerListProperty("spring.kafka.bootstrap-servers")
    }

    @Bean
    fun topic(): NewTopic {
        return TopicBuilder.name(Constants.topic).partitions(1).replicas(1).build()
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun concurrentKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.setConcurrency(1)
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        factory.consumerFactory = consumerFactory()
        println(
            "concurrentKafkaListenerContainerFactory - " +
                "BOOTSTRAP_SERVERS_CONFIG:${
                factory.consumerFactory
                    .configurationProperties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG]
                } "
        )
        return factory
    }

    fun consumerConfigs() = mapOf(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.GROUP_ID_CONFIG to "familie-historikk",
        ConsumerConfig.CLIENT_ID_CONFIG to "consumer-familie-historikk-1",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest",
        CommonClientConfigs.RETRIES_CONFIG to 10,
        CommonClientConfigs.RETRY_BACKOFF_MS_CONFIG to 100,
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"
    )
}
