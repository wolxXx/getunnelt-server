import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.net.InetAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.micrometer.prometheus.*
import io.ktor.server.metrics.micrometer.*
import io.micrometer.core.instrument.*
import io.micrometer.core.instrument.binder.jvm.*
import io.micrometer.core.instrument.binder.system.*
import io.micrometer.core.instrument.binder.logging.*

fun main() {
    embeddedServer(CIO, port = 7000, module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {
    install(WebSockets)
    install(MicrometerMetrics) {
        registry = Prometheus.registry
    }
    routing {
        monitoringRoutes()
        tunnelRoutes()
    }
}
