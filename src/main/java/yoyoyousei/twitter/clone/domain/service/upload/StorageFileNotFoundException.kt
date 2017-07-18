package yoyoyousei.twitter.clone.domain.service.upload

class StorageFileNotFoundException : StorageException {

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}