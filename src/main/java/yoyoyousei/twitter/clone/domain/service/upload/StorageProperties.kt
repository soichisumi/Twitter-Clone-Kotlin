package yoyoyousei.twitter.clone.domain.service.upload

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created by s-sumi on 2017/03/05.
 */
@ConfigurationProperties("storage")
class StorageProperties {
    var uploadedFileLocation = "upload-dir"
}
