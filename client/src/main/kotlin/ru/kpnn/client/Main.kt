package ru.kpnn.client

import java.net.Socket

import rawhttp.core.RawHttp
import java.io.File



fun main(args: Array<String>) {

    val host = args[0]
    val port = args[1].toInt()

    val path = args[2]

    val http = RawHttp()

    val request = http.parseRequest("""
        GET /${path} HTTP/1.1
        Host: Lab3Client
        Accept: multipart/form-data
    """.trimIndent())

    val response = Socket(host, port).use {
        request.writeTo(it.getOutputStream())
        http.parseResponse(it.getInputStream()).eagerly()
    }

    if (response.statusCode == 200) {
        val file = File(path)
        file.createNewFile()
        file.writeBytes(response.body.get().asRawBytes())

        println("File saved on ${file.absolutePath}")
    } else {
        println("File not found")
    }

}