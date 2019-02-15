import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.PipelineContext


class MyExceptionWithDefaultValue(message: String = "Default error message") : RuntimeException(message)
class MyExceptionWithoutDefaultValue(message: String) : RuntimeException(message)

@Suppress("unused")
@UseExperimental(KtorExperimentalAPI::class)
fun Application.main() {
    install(Routing)
    install(StatusPages) {
        val internalServerErrorHandler: suspend PipelineContext<*, ApplicationCall>.(Throwable) -> Unit = {
            call.respondText(
                it.localizedMessage ?: "Unknown error",
                ContentType.Application.Json,
                HttpStatusCode.InternalServerError
            )
        }
        exceptions[MyExceptionWithDefaultValue::class.java] = internalServerErrorHandler
        exceptions[MyExceptionWithoutDefaultValue::class.java] = internalServerErrorHandler
    }
    routing {
        get("/my-exception-with-default-value-and-custom-error-message") {
            throw MyExceptionWithDefaultValue("Custom error message")
        }
        get("/my-exception-with-default-value") {
            throw MyExceptionWithDefaultValue()
        }
        get("/my-exception-without-default-value-and-custom-error-message") {
            throw MyExceptionWithoutDefaultValue("Another custom error message")
        }
    }
}
