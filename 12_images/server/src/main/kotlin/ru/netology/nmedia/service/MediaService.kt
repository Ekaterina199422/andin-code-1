package ru.netology.nmedia.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import org.springframework.util.ResourceUtils
import org.springframework.web.multipart.MultipartFile
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.exception.BadContentTypeException
import java.nio.file.Files
import java.util.*

@Service
class MediaService(@Value("\${app.media-location}") private val mediaLocation: String) {
    private val path = ResourceUtils.getFile(mediaLocation).toPath()

    init {
        Files.createDirectories(path)
    }

    fun save(file: MultipartFile): Media {
        val id = UUID.randomUUID().toString() + when(file.contentType) {
            MimeTypeUtils.IMAGE_JPEG_VALUE -> ".jpg"
            MimeTypeUtils.IMAGE_PNG_VALUE -> ".png"
            else -> throw BadContentTypeException()
        }
        file.transferTo(path.resolve(id))
        return Media(id)
    }
}
