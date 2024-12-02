package me.jahni.urlshortner.controller.request

import me.jahni.urlshortner.service.input.ShortenUrlInput

data class ShortenUrlRequest(
    val originalUrl: String,
) {
    fun toInput(): ShortenUrlInput {
        return ShortenUrlInput(
            longUrl = originalUrl
        )
    }
}
