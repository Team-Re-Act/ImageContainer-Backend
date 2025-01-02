package team.react.imagecontainer.domain.image.application

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.webp.WebpWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import team.react.imagecontainer.domain.image.dto.ImgLoad
import team.react.imagecontainer.domain.image.dto.ImgUpload
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Service
class ImageService(
    @Value("\${project.img.upload.path}")
    private val uploadPath: String,
    @Value("\${project.img.upload.result.url}")
    private val uploadResultUrl: String,
) {
    private val uploadDir: Path = Paths.get(uploadPath)

    init {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir)
        }
    }

    fun saveImage(img: MultipartFile): ImgUpload {
        val fileName = "${UUID.randomUUID()}_${img.originalFilename ?: "none_title"}"

        val filePath = uploadDir.resolve(fileName)

        img.inputStream.use { inputStream ->
            Files.copy(inputStream, filePath)
        }

        return ImgUpload(path = "${uploadResultUrl}/${fileName}")
    }

    @Async("imageUploadTaskExecutor")
    fun saveWebpImage(img: MultipartFile): CompletableFuture<ImgUpload> {
        val fileName = "${UUID.randomUUID()}_${img.originalFilename ?: "none_title"}.webp"

        val filePath = uploadDir.resolve(fileName)

        img.inputStream.use { inputStream ->
            val image = ImmutableImage.loader().fromStream(inputStream)
            image.output(WebpWriter.DEFAULT.withLossless(), filePath.toFile())
        }

        val result = ImgUpload(path = "${uploadResultUrl}/${fileName}")

        return CompletableFuture.completedFuture(result)
    }

    fun getImage(id: String): ImgLoad {
        val filePath = uploadDir.resolve(id).normalize()

        val resource = UrlResource(filePath.toUri())

        val mimeType = Files.probeContentType(filePath)

        return ImgLoad(MediaType.parseMediaType(mimeType ?: "image/webp"), resource)
    }
}