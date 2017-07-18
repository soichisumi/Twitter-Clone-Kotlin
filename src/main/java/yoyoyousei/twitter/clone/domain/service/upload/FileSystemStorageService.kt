package yoyoyousei.twitter.clone.domain.service.upload

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile

import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

/**
 * Created by s-sumi on 2017/03/05.
 */
@Service
class FileSystemStorageService @Autowired
constructor(properties: StorageProperties) : StorageService {
    private val rootLocation: Path = Paths.get(properties.uploadedFileLocation) //join decralation and assignment

    override fun store(file: MultipartFile): String {
        try {
            if (file.isEmpty) {
                throw StorageException("Failed to store empty file " + file.originalFilename)
            }

            val res = file.originalFilename.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val suffix = "." + res[res.size - 1]

            val tmpFile = File.createTempFile("usericon-", suffix)
            val filename = tmpFile.name
            tmpFile.delete()
            Files.copy(file.inputStream, this.rootLocation.resolve(filename))

            return filename
        } catch (e: IOException) {
            throw StorageException("Failed to store file " + file.originalFilename, e)
        }

    }

    override fun loadAll(): Stream<Path> {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter { path -> path != this.rootLocation }
                    .map { path -> this.rootLocation.relativize(path) }
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }
    }

    override fun load(filename: String): Path {
        return rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource {
        try {
            val file = load(filename)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                return resource
            } else {
                throw StorageFileNotFoundException("Could not read file: " + filename)

            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: " + filename, e)
        }

    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectory(rootLocation)
            }
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }

    }
}
