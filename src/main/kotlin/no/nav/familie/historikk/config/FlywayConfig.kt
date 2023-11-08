package no.nav.familie.historikk.config

import org.flywaydb.core.api.configuration.FluentConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@Profile("!dev")
@ConditionalOnProperty("spring.flyway.enabled")
class FlywayConfig(private val role: String) {
    @Bean
    fun flywayConfig(): FlywayConfigurationCustomizer {
        return FlywayConfigurationCustomizer { c: FluentConfiguration ->
            c.initSql("SET ROLE \"$role\"")
        }
    }
}
