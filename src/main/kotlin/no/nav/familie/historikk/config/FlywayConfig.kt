package no.nav.familie.historikk.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

@ConstructorBinding
data class FlywayConfig(private val role: String) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun flywayConfig(@Value("\${spring.flyway.placeholders.ignoreIfProd}") ignoreIfProd: String,
                     environment: Environment): FlywayConfigurationCustomizer {
        logger.info("DB-oppdateringer kjøres med rolle $role")
        val isProd = environment.activeProfiles.contains("prod")
        val ignore = ignoreIfProd == "--"
        return FlywayConfigurationCustomizer {
            it.initSql(String.format("SET ROLE \"%s\"", role))
            if (isProd && !ignore) {
                throw RuntimeException("Prod profile-en har ikke riktig verdi for placeholder ignoreIfProd=$ignoreIfProd")
            }
            if (!isProd && ignore) {
                throw RuntimeException("Profile=${environment.activeProfiles} har ignoreIfProd=false")
            }
        }
    }
}
