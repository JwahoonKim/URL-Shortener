package me.jahni.urlshortener.service

import me.jahni.urlshortener.component.UniqueIdGenerator
import me.jahni.urlshortener.component.UrlShortener
import me.jahni.urlshortener.component.UrlStorage
import me.jahni.urlshortener.infra.db.Url
import me.jahni.urlshortener.service.input.ShortenUrlInput
import me.jahni.urlshortener.service.output.ShortenUrlOutput
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
            shortUrl = shortenUrl
        )
    }

}