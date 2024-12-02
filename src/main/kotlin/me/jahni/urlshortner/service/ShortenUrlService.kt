package me.jahni.urlshortner.service

import me.jahni.urlshortner.component.UniqueIdGenerator
import me.jahni.urlshortner.component.UrlShortener
import me.jahni.urlshortner.component.UrlStorage
import me.jahni.urlshortner.infra.db.Url
import me.jahni.urlshortner.service.input.ShortenUrlInput
import me.jahni.urlshortner.service.output.ShortenUrlOutput
import org.springframework.stereotype.Service

@Service
class ShortenUrlService(
    private val urlStorage: UrlStorage,
    private val uniqueIdGenerator: UniqueIdGenerator,
    private val urlShortener: UrlShortener,
) {

    fun shortenUrl(input: ShortenUrlInput): ShortenUrlOutput {
        val findUrl = urlStorage.findByOriginalUrl(input.longUrl)

        if (findUrl != null) {
            return ShortenUrlOutput(
                shortUrl = findUrl.shortUrl
            )
        }

        val id = uniqueIdGenerator.generate()
        val shortenUrl = urlShortener.shorten(id)

        val url = Url(
            shortUrl = shortenUrl,
            originalUrl = input.longUrl
        )

        urlStorage.save(url)

        return ShortenUrlOutput(
            shortUrl = "shortUrl"
        )
    }

}