package yoyoyousei.twitter.clone.app


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import yoyoyousei.twitter.clone.domain.model.Tweet
import yoyoyousei.twitter.clone.domain.model.User
import yoyoyousei.twitter.clone.domain.service.*
import yoyoyousei.twitter.clone.domain.service.upload.FileSystemStorageService
import yoyoyousei.twitter.clone.util.Util

import java.security.Principal
import java.util.*

/**
 * Created by s-sumi on 2017/02/28.
 */
@Controller
//@SessionAttributes(value = {"userinfo"})
class TwitterCloneController//各フィールドに@autowiredしてもいいが、推奨されてない(nulpoが起きやすくなるらしい)
@Autowired
constructor(private val tweetService: TweetService,
            private val userService: UserService,
            private val userDetailsService: TwitterCloneUserDetailsService,
            private val fileSystemStorageService: FileSystemStorageService) {

    @GetMapping(value = "/")
    internal fun timeline(principal: Principal, model: Model): String {
        model.addAttribute("tweetForm", TweetForm())    //attribute can be omitted.

        //default attribute name is Classname whose first letter is lower case.


        val loginUser = Util.getLoginuserFromPrincipal(principal)
        model.addAttribute("userinfo", loginUser)

        model.addAttribute("tweets", tweetService.getTimeLineforLoginUser(loginUser))

        model.addAttribute("recommend", userService.getUnFollowing10Users(loginUser, this))

        log.info("util.noicon: " + Util.getNoIcon())

        return "timeline"
    }


    @PostMapping(value = "/")
    internal fun tweet(principal: Principal, @Validated form: TweetForm, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            val err = HashSet<String>()
            bindingResult.allErrors.forEach { e -> err.add(e.defaultMessage) }
            model.addAttribute("errors", err)
            return timeline(principal, model)
            //return "redirect:/";
        }
        val tweet = Tweet(form.content!!, Util.getLoginuserFromPrincipal(principal))

        //tweetService.save(tweet);
        try {
            tweetService.save(tweet)
        } catch (e: Exception) {
            val err = HashSet<String>()
            err.add("an error occured. try again.")
            model.addAttribute("errors", err)
            log.info(e.toString())
            e.printStackTrace()
            //return timeline(principal,model);
            return "redirect:/"
        }

        return "redirect:/"
    }

    //register
    @GetMapping(value = "/register")
    internal fun registerPage(model: Model): String {
        model.addAttribute("registerForm", RegisterForm())
        return "register"
    }

    @PostMapping(value = "/register")
    internal fun register(@Validated form: RegisterForm, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            log.info("user:" + form.userId!!)
            log.info("pass:" + form.password!!)
            log.info("scr:" + form.screenName!!)
            val err = HashSet<String>()
            bindingResult.allErrors.forEach { e -> err.add(e.defaultMessage) }
            model.addAttribute("errors", err)
            return "register"
        }

        log.info("user:" + form.userId!!)
        log.info("pass:" + form.password!!)
        log.info("scr:" + form.screenName!!)

        val encoder = BCryptPasswordEncoder()
        val user = User(form.userId!!, encoder.encode(form.password!!), form.screenName)
        try {
            userService.create(user)
        } catch (e: UserIdAlreadyExistsException) {
            val errors = HashSet<String>()
            errors.add(e.message!!)
            model.addAttribute("errors", errors)
            return "register"
        } catch (e: Exception) {

            val errors = HashSet<String>()
            errors.add("unexpected error occured. try again.")
            model.addAttribute("errors", errors)

            log.info(e.toString())
            return "register"
        }

        return "redirect:/loginForm"
    }

    @GetMapping("/update")
    internal fun updateUserDataPage(model: Model): String {
        model.addAttribute("userForm", UserForm())
        //model.addAttribute("uploadForm",new UploadFileForm());
        return "mypage"
    }

    @PostMapping("/update")
    internal fun updateUserData(principal: Principal, @Validated form: UserForm, bindingResult: BindingResult,
                                model: Model): String {
        if (bindingResult.hasErrors()) {
            val err = HashSet<String>()
            bindingResult.allErrors.forEach { e -> err.add(e.defaultMessage) }
            model.addAttribute("errors", err)
            //return updateUserDataPage(model);
            return "mypage" //多分上と同義
        }

        try {
            val newUser = userService.find(Util.getLoginuserFromPrincipal(principal).userId)
            if (form.screenName != "")
                newUser.screenName = form.screenName
            if (form.biography != "")
                newUser.biography = form.biography
            userService.update(newUser)

            Util.updateAuthenticate(principal, newUser)

            model.addAttribute("userinfo", newUser)
        } catch (e: UserIdNotFoundException) {
            val errors = HashSet<String>()
            errors.add(e.message!!)
            model.addAttribute("errors", errors)
            return "mypage"
        } catch (e: Exception) {
            val errors = HashSet<String>()
            errors.add("unexpected error occured. try again.")
            model.addAttribute("errors", errors)
            log.info(e.message)
            return "mypage"
        }

        return "redirect:/"
    }

    //TODO案: urlにuseridを出さない
    @PostMapping(value = "/follow/{userid}")
    internal fun follow(principal: Principal, @PathVariable("userid") userid: String, attributes: RedirectAttributes): String {
        val loginUser = Util.getLoginuserFromPrincipal(principal)
        try {
            val target = userService.find(userid)
            loginUser.following!!.add(target)
            userService.update(loginUser)  //DBに反映
            Util.updateAuthenticate(principal, loginUser)  //セッション情報を更新
        } catch (e: Exception) {
            val errors = HashSet<String>()
            errors.add("unexpected error occured. try again.")
            log.info(e.toString())
            attributes.addFlashAttribute("errors", errors)
            log.info(e.message)
        }

        return "redirect:/"
    }


    //------Util--------------------------------------

    @GetMapping("/debug")
    internal fun debug(): String {
        val users = userService.findAll()
        for (u in users) {
            log.info(u.toString())
        }
        return "redirect:/"
    }

    companion object {

        val log = LoggerFactory.getLogger(TwitterCloneController::class.java)
    }

}
