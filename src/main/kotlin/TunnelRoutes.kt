import io.ktor.server.routing.*
import io.ktor.websocket.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.*
import java.net.InetAddress
import java.util.*

fun Routing.tunnelRoutes() {
    webSocket("/tunnel") {
        val clientId = UUID.randomUUID().toString()
        val instanceId = InetAddress.getLocalHost().hostName

        RedisManager.setClientHost(clientId, instanceId)
        ClientSessionRegistry.register(clientId, this)

        RedisManager.subscribe("client:$clientId") { msg ->
            if (msg.startsWith("proxy-request|")) {
                val connId = msg.substringAfter("connId=")
                launch { handleProxyRequest(clientId, connId) }
            }
        }

        try {
            for (frame in incoming) {
                // TODO: Kommunikation mit Tunnel-Client
            }
        } finally {
            ClientSessionRegistry.unregister(clientId)
        }
    }

    get("/{clientId}/{...}") {
        val clientId = call.parameters["clientId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val connId = UUID.randomUUID().toString()

        val host = RedisManager.getClientHost(clientId).get()
        if (host == InetAddress.getLocalHost().hostName) {
            // Direkt lokal verbinden
            handleProxyRequest(clientId, connId)
        } else {
            // Anfrage an richtige Instanz weiterleiten
            RedisManager.publish("client:$clientId", "proxy-request|connId=$connId")
        }

        call.respond(HttpStatusCode.Accepted, "Bridging request...")
    }
}

suspend fun handleProxyRequest(clientId: String, connId: String) {
    // TODO: Brücke zwischen WebSocket und eingehender Verbindung herstellen
}
