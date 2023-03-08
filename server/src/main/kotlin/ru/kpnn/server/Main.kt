package ru.kpnn.server

import java.net.ServerSocket
import java.net.Socket

import rawhttp.core.RawHttp
import rawhttp.core.body.FileBody
import java.io.File

import java.util.concurrent.Semaphore



fun handleClient(client: Socket) {
    println("Client in work: ${client}")

    val reader = client.getInputStream()
    val writer = client.getOutputStream()

    val http = RawHttp()

    val request = http.parseRequest(reader)

    val file = File("/tmp/${request.uri.path.drop(1)}")

    if (file.isFile) {

        val response = RawHttp().parseResponse("""
            HTTP/1.1 200 OK
            Server: Lab3Server
            Content-Length: 0
        """.trimIndent()
        ).withBody(FileBody(file, "multipart/form-data"))

        response.writeTo(writer)

    } else {
        val response = RawHttp().parseResponse("""
            HTTP/1.1 404 Not found
            Server: Lab3Server
            Content-Length: 0
        """.trimIndent())

        response.writeTo(writer)
    }

    client.close()

    println("Client socket closed: ${client}")
}

fun main(args: Array<String>) {

    val port = args[0].toInt()
    val permits = args[1].toInt()

    val s = Semaphore(permits)

    val server = ServerSocket(port)
    println("Server is running on port ${server.localPort}")

    while (true) {
        val client = server.accept()

        println("Client connected: ${client}")

        Thread {
            s.acquire()
            handleClient(client)
            s.release()
        }.start()
    }

}