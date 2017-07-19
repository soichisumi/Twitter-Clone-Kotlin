package yoyoyousei.twitter.clone.util

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import yoyoyousei.twitter.clone.app.FileUploadController
import yoyoyousei.twitter.clone.domain.model.User
import yoyoyousei.twitter.clone.domain.service.TwitterCloneUserDetails
import java.security.Principal

/**
 * Created by s-sumi on 2017/03/02.
 */
//component service repository controllerは基本どれも同じで、
//クラスをDIコンテナにbeanとして登録する
object Util {
    private var noIcon: String? = null

    var imageExtensions = arrayOf("jpg", "jpeg", "png", "gif")

    fun getLoginuserFromPrincipal(principal: Principal): User {
        val authentication = principal as Authentication
        val userDetails = TwitterCloneUserDetails::class.java.cast(authentication.principal)
        return userDetails.getuser()
        /*TwitterCloneUserDetails userDetails=(TwitterCloneUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.getuser();*/
    }

    fun updateAuthenticate(principal: Principal, newUser: User) {
        val oldAuth = principal as Authentication
        val newAuth = UsernamePasswordAuthenticationToken(TwitterCloneUserDetails(newUser), oldAuth.credentials, oldAuth.authorities)
        SecurityContextHolder.getContext().authentication = newAuth
    }

    fun getNoIcon(): String? {
        if (noIcon == null)
            noIcon = MvcUriComponentsBuilder.fromMethodName(FileUploadController::class.java, "serveFile", "noicon.png").build().toString()
        return noIcon
    }
}
