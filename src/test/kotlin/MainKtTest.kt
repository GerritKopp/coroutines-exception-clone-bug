import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@KtorExperimentalAPI
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainKtTest {
    private val environment = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application.conf"))
    }
    private val engine = TestApplicationEngine(environment)

    @Suppress("unused")
    @BeforeAll
    private fun setup() {
        engine.start(wait = false)
    }

    @Test
    fun `Custom error message from MyExceptionWithDefaultMessage is response content`() {
        with(engine) {
            with(handleRequest(HttpMethod.Get, "/my-exception-with-default-value-and-custom-error-message")) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Custom error message", response.content)
            }
        }
    }

    @Test
    fun `Default error message from MyExceptionWithDefaultMessage is response content`() {
        with(engine) {
            with(handleRequest(HttpMethod.Get, "/my-exception-with-default-value")) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Default error message", response.content)
            }
        }
    }

    @Test
    fun `Custom error message from MyExceptionWithoutDefaultMessage is response content`() {
        with(engine) {
            with(handleRequest(HttpMethod.Get, "/my-exception-without-default-value-and-custom-error-message")) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Another custom error message", response.content)
            }
        }
    }
}
