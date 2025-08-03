import io.micrometer.prometheus.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.logging.LogbackMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics

object Prometheus {
    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT).apply {
        JvmMemoryMetrics().bindTo(this)
        JvmGcMetrics().bindTo(this)
        ProcessorMetrics().bindTo(this)
        LogbackMetrics().bindTo(this)
    }
}

fun Routing.monitoringRoutes() {
    get("/metrics") {
        call.respondText(Prometheus.registry.scrape())
    }
}
