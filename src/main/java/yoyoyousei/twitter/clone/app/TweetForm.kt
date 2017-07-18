package yoyoyousei.twitter.clone.app

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * Created by s-sumi on 2017/02/28.
 */
class TweetForm {
    @Size(min = 1, max = 255, message = "ツイートは1文字以上255文字以下です")
            //@Pattern(regexp = "[a-zA-Z0-9]*",message = "アルファベットまたは数字のみ使用できます")
    var content: String = ""
}
