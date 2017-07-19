package yoyoyousei.twitter.clone.app

import javax.validation.constraints.Size

/**
 * Created by s-sumi on 2017/02/28.
 */
class TweetForm {
    @Size(min = 1, max = 255, message = "ツイートは1文字以上255文字以下です")
    var content: String = ""
}
