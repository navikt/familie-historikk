package no.nav.familie.tilbake.common.exceptionhandler

import no.nav.familie.kontrakter.felles.Ressurs
import org.slf4j.LoggerFactory
import org.springframework.core.NestedExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@Suppress("unused")
@ControllerAdvice
class ApiExceptionHandler {

    private val logger = LoggerFactory.getLogger(ApiExceptionHandler::class.java)
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    private fun rootCause(throwable: Throwable): String {
        return NestedExceptionUtils.getMostSpecificCause(throwable).javaClass.simpleName
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(throwable: Throwable): ResponseEntity<Ressurs<Nothing>> {
        secureLogger.error("En feil har oppstått", throwable)
        logger.error("En feil har oppstått: ${rootCause(throwable)} ")

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Ressurs.failure(errorMessage = "Uventet feil", frontendFeilmelding = "En uventet feil oppstod."))
    }

    @ExceptionHandler(ApiFeil::class)
    fun handleThrowable(feil: ApiFeil): ResponseEntity<Ressurs<Nothing>> {
        return ResponseEntity.status(feil.httpStatus).body(Ressurs.failure(frontendFeilmelding = feil.feil))
    }

    @ExceptionHandler(Feil::class)
    fun handleThrowable(feil: Feil): ResponseEntity<Ressurs<Nothing>> {
        secureLogger.error("En håndtert feil har oppstått(${feil.httpStatus}): ${feil.message}", feil)
        logger.info("En håndtert feil har oppstått(${feil.httpStatus}) exception=${rootCause(feil)}: ${feil.message} ")
        return ResponseEntity.status(feil.httpStatus).body(Ressurs.failure(errorMessage = feil.message,
                                                                           frontendFeilmelding = feil.frontendFeilmelding))
    }

    @ExceptionHandler(HistorikkException::class)
    fun handleThrowable(feil: HistorikkException): ResponseEntity<Ressurs<Nothing>> {
        secureLogger.error("En håndtert feil har oppstått(${feil.httpStatus}): ${feil.message}", feil)
        logger.info("En håndtert feil har oppstått(${feil.httpStatus}) exception=${rootCause(feil)}: ${feil.message} ")
        return ResponseEntity.status(feil.httpStatus).body(Ressurs.failure(errorMessage = feil.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleThrowable(feil: MethodArgumentNotValidException): ResponseEntity<Ressurs<Nothing>> {
        val feilMelding = StringBuilder()
        feil.bindingResult.fieldErrors.forEach { fieldError ->
            secureLogger.error("Validering feil har oppstått: field={} message={} verdi={}",
                               fieldError.field,
                               fieldError.defaultMessage,
                               fieldError.rejectedValue)
            logger.error("Validering feil har oppstått: field={} message={}", fieldError.field, fieldError.defaultMessage)
            feilMelding.append(fieldError.defaultMessage)
            feilMelding.append(";")
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Ressurs.failure(errorMessage = feilMelding.toString(), frontendFeilmelding = feilMelding.toString()))
    }

}
