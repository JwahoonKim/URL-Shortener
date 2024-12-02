package me.jahni.urlshortener.controller.request

import me.jahni.urlshortener.service.input.ShortenUrlInput

data class ShortenUrlRequest(
    val originalUrl: String,
) {
    fun toInput(): ShortenUrlInput {
        return ShortenUrlInput(
            longUrl = originalUrl
        )
    }
}
