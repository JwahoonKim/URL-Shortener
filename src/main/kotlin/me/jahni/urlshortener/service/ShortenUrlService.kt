package me.jahni.urlshortener.service

import me.jahni.urlshortener.component.UniqueIdGenerator
import me.jahni.urlshortener.component.UrlShortener
import me.jahni.urlshortener.component.UrlStorage
import me.jahni.urlshortener.infra.db.Url
import me.jahni.urlshortener.service.input.ShortenUrlInput
import me.jahni.urlshortener.service.output.GetOriginalUrlOutput
import me.jahni.urlshortener.service.output.ShortenUrlOutput
import org.springframework.stereotype.Service

@Service
class ShortenUrlService(
    private val urlStorage: UrlStorage,
    private val uniqueIdGenerator: UniqueIdGenerator,
    private val urlShortener: UrlShortener,
) {

    fun shortenUrl(input: ShortenUrlInput): ShortenUrlOutput {
        val existUrl = urlStorage.findByOriginalUrl(input.longUrl)
        if (existUrl != null) {
            return ShortenUrlOutput(
                shortUrl = existUrl.shortUrl
            )
        }

        val shortenUrl = createAndSaveShortenUrl(input)
        return ShortenUrlOutput(
            shortUrl = shortenUrl
        )
    }

    private fun createAndSaveShortenUrl(input: ShortenUrlInput): String {
        val id = uniqueIdGenerator.generate()
        val shortenUrl = urlShortener.shorten(id)
        val url = Url(
            shortUrl = shortenUrl,
            originalUrl = input.longUrl
        )
        urlStorage.save(url)
        return shortenUrl
    }

    fun getOriginalUrl(shortUrl: String): GetOriginalUrlOutput {
        val findUrl = urlStorage.findByShortUrl(shortUrl)

        return GetOriginalUrlOutput(
            originalUrl = findUrl?.originalUrl
        )
    }

}