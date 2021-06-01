package no.nav.familie.historikk.config

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties


@Configuration
@EnableKafka
@Profile("prepod", "prod")
class KafkaConfig(@Value("\${KAFKA_BROKERS:localhost}") private val kafkaBrokers: String,
                  @Value("\${KAFKA_SECURITY_PROTOCOL:SSL}") private val kafkaSecurityProtocol: String,
                  @Value("\${KAFKA_TRUSTSTORE_PATH}") private val kafkaTruststorePath: String,
                  @Value("\${KAFKA_CREDSTORE_PASSWORD}") private val kafkaCredstorePassword: String,
                  @Value("\${KAFKA_KEYSTORE_PATH}") private val kafkaKeystorePath: String) {

    private val JAVA_KEYSTORE = "JKS"
    private val PKCS12 = "PKCS12"

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
        println("concurrentKafkaListenerContainerFactory - " +
                "BOOTSTRAP_SERVERS_CONFIG:${factory.consumerFactory.configurationProperties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG]} ")
        return factory
    }

    fun consumerConfigs() = mapOf(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.GROUP_ID_CONFIG to "familie-historikk",
            ConsumerConfig.CLIENT_ID_CONFIG to "consumer-familie-historikk-1",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest",
            CommonClientConfigs.RETRIES_CONFIG to 10,
            CommonClientConfigs.RETRY_BACKOFF_MS_CONFIG to 100

    ) + commonConfig()



    fun commonConfig() = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaBrokers
    ) + securityConfig()

    private fun securityConfig() = mapOf(
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to kafkaSecurityProtocol,
            SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG to "", // Disable server host name verification
            SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG to JAVA_KEYSTORE,
            SslConfigs.SSL_KEYSTORE_TYPE_CONFIG to PKCS12,
            SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG to kafkaTruststorePath,
            SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG to kafkaCredstorePassword,
            SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG to kafkaKeystorePath,
            SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG to kafkaCredstorePassword,
            SslConfigs.SSL_KEY_PASSWORD_CONFIG to kafkaCredstorePassword,
    )
}
