package team.react.imagecontainer.domain.image.dto

import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType

data class ImgLoad(
    val contentType: MediaType,
    val resource: UrlResource,
)
