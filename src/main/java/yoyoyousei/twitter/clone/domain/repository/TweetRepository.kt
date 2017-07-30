package yoyoyousei.twitter.clone.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import yoyoyousei.twitter.clone.domain.model.Tweet

/**
 * Created by s-sumi on 2017/02/28.
 */
//モデルのCLUD担当
interface TweetRepository : JpaRepository<Tweet, Int> {
    //条件にマッチする結果がなかったらnullです
    fun findAllByOrderByPostTimeDesc(): List<Tweet>
    fun findTop100ByTweetUser_UserIdInOrderByPostTimeDesc(tweetUserId: List<String>): List<Tweet>
}
