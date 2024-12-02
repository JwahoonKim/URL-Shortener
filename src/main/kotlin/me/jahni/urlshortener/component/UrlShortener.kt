package me.jahni.urlshortener.component

import org.springframework.stereotype.Component

@Component
class UrlShortener {

    private val BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    fun shorten(id: Long): String {
        val encoded = encode(id)
        return encoded.toString().padStart(7, '0')
    }

    private fun encode(id: Long): StringBuilder {
        var value = id
        val encoded = StringBuilder()
        while (value > 0) {
            val index = (value % 62).toInt()
            encoded.append(BASE62_CHARS[index])
            value /= 62
        }
        return encoded
    }
}