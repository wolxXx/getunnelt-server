import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands
import io.lettuce.core.pubsub.RedisPubSubAdapter
import java.net.InetAddress
import java.util.concurrent.CompletableFuture

object RedisManager {
    private val client = RedisClient.create("redis://redis:6379")
    private val conn = client.connect()
    private val pubSubConn = client.connectPubSub()
    private val async = conn.async()
    private val listeners = mutableMapOf<String, (String) -> Unit>()

    init {
        pubSubConn.addListener(object : RedisPubSubAdapter<String, String>() {
            override fun message(channel: String, message: String) {
                listeners[channel]?.invoke(message)
            }
        })
    }

    fun subscribe(channel: String, handler: (String) -> Unit) {
        listeners[channel] = handler
        pubSubConn.async().subscribe(channel)
    }

    fun publish(channel: String, message: String) {
        async.publish(channel, message)
    }

    fun setClientHost(clientId: String, instanceId: String) {
        async.set("client:$clientId", instanceId)
    }

    fun getClientHost(clientId: String): CompletableFuture<String?> {
        return async.get("client:$clientId").toCompletableFuture()
    }
}
