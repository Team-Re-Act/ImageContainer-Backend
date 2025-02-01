package team.react.imagecontainer.domain.image.api

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.webp.WebpWriter
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import team.react.imagecontainer.domain.image.application.ImageService
import java.io.ByteArrayOutputStream
import java.io.File

@RestController
@RequestMapping("/api/image")
class ImageApi(
    private val imageService: ImageService
) {
    @PostMapping("")
    fun postImage(@RequestParam("img") img: MultipartFile) = imageService.saveImage(img)

    @GetMapping("/{id}")
    fun getImage(@PathVariable("id") id: String): ResponseEntity<UrlResource> =
        imageService.getImage(id).let { image ->
            ResponseEntity
                .ok()
                .contentType(image.contentType)
                .body(image.resource)
        }

    @PostMapping("/webp")
    fun saveWebpImage(@RequestParam("img") img: MultipartFile) = imageService.saveWebpImage(img)
}