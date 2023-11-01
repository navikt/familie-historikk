package no.nav.familie.historikk

import no.nav.familie.tilbake.config.TestLauncherConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import java.util.Properties

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class LauncherLocalPostgres

fun main(args: Array<String>) {
    val properties = Properties()
    properties["DATASOURCE_URL"] = "jdbc:postgresql://localhost:5432/familie-historikk"
    properties["DATASOURCE_USERNAME"] = "postgres"
    properties["DATASOURCE_PASSWORD"] = "test"
    properties["DATASOURCE_DRIVER"] = "org.postgresql.Driver"

    TestLauncherConfig().settClientIdOgSecretForLokalKjøring()

    SpringApplicationBuilder(LauncherLocalPostgres::class.java)
        .profiles("local", "mock-pdl", "mock-oauth")
        .properties(properties)
        .run(*args)
}
