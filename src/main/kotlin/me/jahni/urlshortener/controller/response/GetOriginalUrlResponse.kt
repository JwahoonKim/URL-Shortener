package me.jahni.urlshortener.controller.response

import me.jahni.urlshortener.service.output.GetOriginalUrlOutput

data class GetOriginalUrlResponse(
    val originalUrl: String?
) {
    companion object {
        fun from(output: GetOriginalUrlOutput): GetOriginalUrlResponse {
            return GetOriginalUrlResponse(
                originalUrl = output.originalUrl
            )
        }
    }
}
