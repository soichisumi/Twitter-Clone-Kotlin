package yoyoyousei.twitter.clone.domain.model

import org.hibernate.annotations.CreationTimestamp

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by s-sumi on 2017/02/27.
 */
@Entity
class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var tweetId: Int? = null //primitive型にはlateinit効かないからしかたないか
    
    lateinit var postTime: Timestamp

    @ManyToOne
    lateinit var tweetUser: User

    @NotNull
    lateinit var content: String

    constructor() {}

    constructor(content: String, tweetUser: User) {
        this.content = content
        this.tweetUser = tweetUser
        this.postTime = Timestamp(System.currentTimeMillis()) //TODO: ここをcreationtimestampに。アノテーションが効かない？
    }
}
