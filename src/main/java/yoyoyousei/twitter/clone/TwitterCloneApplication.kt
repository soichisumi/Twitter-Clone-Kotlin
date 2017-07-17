package yoyoyousei.twitter.clone

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import yoyoyousei.twitter.clone.domain.service.upload.StorageProperties
import yoyoyousei.twitter.clone.domain.service.upload.StorageService

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
open class TwitterCloneApplication {
    @Bean
    open internal fun init(storageService: StorageService): CommandLineRunner {
        return CommandLineRunner { storageService.init() }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(TwitterCloneApplication::class.java, *args)
        }
    }
}
