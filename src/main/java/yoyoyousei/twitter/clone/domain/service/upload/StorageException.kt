package yoyoyousei.twitter.clone.domain.service.upload

/**
 * Created by s-sumi on 2017/03/05.
 */
open class StorageException : RuntimeException {

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
