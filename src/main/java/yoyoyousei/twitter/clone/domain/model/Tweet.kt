package yoyoyousei.twitter.clone.domain.model

import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotNull

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

    //spring jpaに必要
    constructor() {}

    constructor(content: String, tweetUser: User) {
        this.content = content
        this.tweetUser = tweetUser
        this.postTime = Timestamp(System.currentTimeMillis()) //TODO: ここをcreationtimestampに。アノテーションが効かない？
    }
}
