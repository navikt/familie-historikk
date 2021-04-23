package no.nav.familie.historikk.common.repository

import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.relational.core.mapping.Embedded
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Sporbar(val opprettetAv: String = "VL",
                   val opprettetTid: LocalDateTime = SporbarUtils.now(),
                   @LastModifiedBy
                   @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
                   val endret: Endret = Endret())

data class Endret(val endretAv: String = "VL",
                  val endretTid: LocalDateTime = SporbarUtils.now())

object SporbarUtils {

    fun now(): LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
}
