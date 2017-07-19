package yoyoyousei.twitter.clone.app;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by s-sumi on 2017/03/01.
 */
class LoginForm {

    @Size(min = 4, max = 20, message = "ユーザIDは4文字以上20文字以下です")
    @Pattern(regexp = "[a-zA-Z0-9]", message = "アルファベットまたは数字のみ使用できます")
    var userId: String = ""

    @Size(min = 4, max = 20, message = "パスワードは4文字以上20文字以下です")
    @Pattern(regexp = "[a-zA-Z0-9]", message = "アルファベットまたは数字のみ使用できます")
    var password: String = ""
}
