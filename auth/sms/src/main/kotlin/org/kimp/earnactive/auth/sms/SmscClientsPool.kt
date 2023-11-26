package org.kimp.earnactive.auth.sms

import io.ktor.utils.io.core.Closeable
import org.kimp.earnactive.auth.sms.model.BalanceInfo
import org.kimp.earnactive.auth.sms.model.Message
import org.kimp.earnactive.auth.sms.model.MessageInfo
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class SmscClientsPool(
    val clients: List<SmscClient>
) {
    private val freeClientsQueue: BlockingQueue<Int> = ArrayBlockingQueue(clients.size)

    init {
        clients.indices.forEach { freeClientsQueue.put(it) }
    }

    fun getResource(): SmscClientResource {
        return SmscClientResource(freeClientsQueue.take())
    }

    inner class SmscClientResource(
        private val clientIndex: Int
    ) : SmscApi, Closeable {
        override suspend fun sendMessage(content: String, phones: List<String>): Message =
            clients[clientIndex].sendMessage(content, phones)

        override suspend fun getMessageStatus(messageId: String, phones: List<String>): MessageInfo =
            clients[clientIndex].getMessageStatus(messageId, phones)

        override suspend fun getBalance(): BalanceInfo = clients[clientIndex].getBalance()

        override fun close() {
            freeClientsQueue.put(clientIndex)
        }
    }
}
