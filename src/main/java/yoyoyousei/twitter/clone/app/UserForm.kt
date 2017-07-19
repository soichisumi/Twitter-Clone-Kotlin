package yoyoyousei.twitter.clone.app

import javax.validation.constraints.Size

/**
 * Created by s-sumi on 2017/02/28.
 */
class UserForm {
    @Size(max = 25, message = "ハンドルネームは25文字以下です")
    var screenName: String = ""
    var biography: String = ""
}
