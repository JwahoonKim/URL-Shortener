package me.jahni.urlshortener.controller.response

import me.jahni.urlshortener.service.output.ShortenUrlOutput

data class ShortenUrlResponse(
    val shortUrl: String,
) {
    companion object {
        fun from(output: ShortenUrlOutput): ShortenUrlResponse {
            return ShortenUrlResponse(
                shortUrl = output.shortUrl
            )
        }
    }
}
