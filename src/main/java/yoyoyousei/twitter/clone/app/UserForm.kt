package yoyoyousei.twitter.clone.app

import javax.validation.constraints.Size

/**
 * Created by s-sumi on 2017/02/28.
 */
class UserForm {
    @Size(max = 25, message = "ハンドルネームは25文字以下です")
    lateinit var screenName: String

    lateinit var biography: String
}
