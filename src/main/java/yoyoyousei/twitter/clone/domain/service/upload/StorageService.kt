package yoyoyousei.twitter.clone.domain.service.upload

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Path
import java.util.stream.Stream

/**
 * Created by s-sumi on 2017/03/05.
 */
interface StorageService {
    fun init()

    fun store(file: MultipartFile): String

    fun loadAll(): Stream<Path>

    fun load(filename: String): Path

    fun loadAsResource(filename: String): Resource

    fun deleteAll()
}
