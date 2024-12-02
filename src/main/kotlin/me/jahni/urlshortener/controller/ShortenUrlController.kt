package me.jahni.urlshortener.controller

import me.jahni.urlshortener.controller.request.ShortenUrlRequest
import me.jahni.urlshortener.controller.response.ShortenUrlResponse
import me.jahni.urlshortener.service.ShortenUrlService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShortenUrlController(
    private val shortenUrlService: ShortenUrlService
) {

    @PostMapping("/shorten")
    fun shortenUrl(
        @RequestBody request: ShortenUrlRequest
    ): ShortenUrlResponse {
        val input = request.toInput()
        val output = shortenUrlService.shortenUrl(input)
        return ShortenUrlResponse.from(output)
    }

}