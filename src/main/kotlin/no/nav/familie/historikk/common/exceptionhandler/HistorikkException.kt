package no.nav.familie.historikk.common.exceptionhandler

import org.springframework.http.HttpStatus

class HistorikkException(
    val feilmelding: String,
    val httpStatus: HttpStatus,
    throwable: Throwable? = null,
) : RuntimeException(feilmelding, throwable)
