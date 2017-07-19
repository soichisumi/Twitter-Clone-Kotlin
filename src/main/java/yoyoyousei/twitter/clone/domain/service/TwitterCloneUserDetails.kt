package yoyoyousei.twitter.clone.domain.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import yoyoyousei.twitter.clone.domain.model.User

/**
 * Created by s-sumi on 2017/02/28.
 */
class TwitterCloneUserDetails(private val user: User) : UserDetails {
    fun getuser(): User {
        return user
    }

    //定形
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return AuthorityUtils.createAuthorityList("ROLE_" + this.user.roleName.name)
    }

    //認証に使うpassword
    override fun getPassword(): String {
        return this.user.password
    }

    //認証に使うuserId
    override fun getUsername(): String {
        return this.user.userId
    }

    //アカウント期限切れ機能
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    //アカウントロック機能
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    //パスワード有効期限切れ機能
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    //アカウント無効化の機能
    override fun isEnabled(): Boolean {
        return true
    }
}
