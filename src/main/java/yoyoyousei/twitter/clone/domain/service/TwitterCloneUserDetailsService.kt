package yoyoyousei.twitter.clone.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import yoyoyousei.twitter.clone.domain.repository.UserRepository

/**
 * Created by s-sumi on 2017/02/28.
 */
@Service
class TwitterCloneUserDetailsService : UserDetailsService {

    internal var userRepository: UserRepository

    @Autowired
    constructor(userRepository: UserRepository) {
        this.userRepository = userRepository
    }


    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findOne(username) ?: throw UsernameNotFoundException(username + " is not found.")
        return TwitterCloneUserDetails(user)
    }
}
