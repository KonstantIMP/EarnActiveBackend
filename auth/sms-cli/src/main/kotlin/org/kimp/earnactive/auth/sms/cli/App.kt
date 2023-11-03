package org.kimp.earnactive.auth.sms.cli

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.kimp.earnactive.auth.sms.SmscClient

val smscLogin = System.getenv("SMSC_LOGIN") ?: ""
val smscPassword = System.getenv("SMSC_PASSWORD") ?: ""

fun main (args: Array<String>) {
    println("sms-cli: simple smsc api checker")

    if (smscLogin.isEmpty() || smscPassword.isEmpty()) {
        println("Specify SMSC_LOGIN and SMSC_PASSWORD environments to continue")
        return
    }

    if (args.isEmpty()) {
        println("USAGE: sms-cli <balance> <status [phone] [message id]> <send [phone] [content]>")
        return
    }

    val smscClient = SmscClient(smscLogin, smscPassword)

    when (args[0]) {
        "balance" -> runBlocking (Dispatchers.IO) {
            val balanceInfo = smscClient.getBalance()
            println("Your balance is ${balanceInfo.balance} ${balanceInfo.currency}")
        }
        "status" -> runBlocking (Dispatchers.IO) {
            val phone = args.getOrElse(1) { _ -> "" }
            val messageId = args.getOrElse(2) { _ -> "" }

            if (messageId.isEmpty() || phone.isEmpty()) {
                println("Specify message and phone id to get info")
                return@runBlocking
            }

            val messageInfo = smscClient.getMessageStatus(messageId, listOf(phone))
            println("Message ${messageId} to '${messageInfo.phone}' is '${messageInfo.statusName}'")
        }
        "send" -> runBlocking (Dispatchers.IO) {
            val phone = args.getOrElse(1) { _ -> "" }
            val content = args.getOrElse(2) { _ -> "" }

            if (phone.isEmpty() || content.isEmpty()) {
                println("Phone or content isn't provided")
                return@runBlocking
            }

            val smsStatus = smscClient.sendMessage(content, listOf(phone))
            println("Message was sent with ${smsStatus.id} id")
        }
        else -> println("Unknown option '${args[1]}' found, skipping")
    }
}
