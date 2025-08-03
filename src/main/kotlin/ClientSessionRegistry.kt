import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

object ClientSessionRegistry {
    private val sessions = ConcurrentHashMap<String, DefaultWebSocketServerSession>()

    fun register(clientId: String, session: DefaultWebSocketServerSession) {
        sessions[clientId] = session
    }

    fun get(clientId: String): DefaultWebSocketServerSession? =
        sessions[clientId]

    fun unregister(clientId: String) {
        sessions.remove(clientId)
    }

    fun allClientIds(): Set<String> = sessions.keys
}
