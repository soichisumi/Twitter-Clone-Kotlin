package yoyoyousei.twitter.clone.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import yoyoyousei.twitter.clone.domain.model.Tweet
import yoyoyousei.twitter.clone.domain.model.User
import yoyoyousei.twitter.clone.domain.repository.TweetRepository
import java.util.*
import java.util.stream.Collectors
import javax.transaction.Transactional

/**
 * Created by s-sumi on 2017/02/28.
 */

@Service
@Transactional
open class TweetService {
    @Autowired
    lateinit var tweetRepository: TweetRepository

    open fun findAllDesc(): List<Tweet> {
        var res: List<Tweet>? = tweetRepository.findAllByOrderByPostTimeDesc()
        if (res == null) {
            res = ArrayList<Tweet>()
        }
        return res
    }

    open fun getTimeLineforLoginUser(loginuser: User): List<Tweet> {
        val following = loginuser.following
        following.add(loginuser)
        val ids = following.stream().map<String>({ it.userId }).collect(Collectors.toList<String>())
        return tweetRepository.findTop100ByTweetUser_UserIdInOrderByPostTimeDesc(ids)
    }

    open fun save(tweet: Tweet): Tweet {
        return tweetRepository.save(tweet)
    }

    open fun delete(id: Int) {
        tweetRepository.delete(id)
    }

    open fun find(id: Int): Tweet {
        return tweetRepository.findOne(id)
    }
}
