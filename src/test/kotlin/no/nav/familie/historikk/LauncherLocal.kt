package no.nav.familie.historikk

import no.nav.familie.historikk.config.ApplicationConfig
import no.nav.familie.historikk.database.DbContainerInitializer
import no.nav.familie.tilbake.config.TestLauncherConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class LauncherLocal

fun main(args: Array<String>) {
    TestLauncherConfig().settClientIdOgSecretForLokalKjøring()

    SpringApplicationBuilder(ApplicationConfig::class.java)
        .initializers(DbContainerInitializer())
        .profiles("local", "mock-pdl", "mock-oauth")
        .run(*args)
}
