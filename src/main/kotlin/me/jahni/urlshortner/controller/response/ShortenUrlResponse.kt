package me.jahni.urlshortner.controller.response

import me.jahni.urlshortner.service.output.ShortenUrlOutput

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
