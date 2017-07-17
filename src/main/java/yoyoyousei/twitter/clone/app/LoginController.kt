package yoyoyousei.twitter.clone.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by s-sumi on 2017/02/28.
 */
@Controller
class LoginController {
    @RequestMapping("/loginForm")
    fun loginForm(): String{
        return "login";
    }
}
