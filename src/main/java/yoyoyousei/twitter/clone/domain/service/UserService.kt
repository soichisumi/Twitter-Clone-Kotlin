package yoyoyousei.twitter.clone.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import yoyoyousei.twitter.clone.app.TwitterCloneController
import yoyoyousei.twitter.clone.domain.model.User
import yoyoyousei.twitter.clone.domain.repository.UserRepository
import java.util.stream.Collectors
import javax.transaction.Transactional

@Service
@Transactional
open class UserService {
    @Autowired
    lateinit var userRepository: UserRepository //open class UserService {  -> open class UserService @Autowired constructor(val userRepository: UserRepository) { 　
    //でもOK

    open fun findAll(): List<User> {
        return userRepository.findAll()
    }

    @Throws(UserIdAlreadyExistsException::class)
    open fun create(user: User): User {
        val tmp = userRepository.findOne(user.userId)
        if (tmp != null)
            throw UserIdAlreadyExistsException(user.userId + " is already exists.")
        return userRepository.save(user)
    }

    open fun update(user: User): User {
        if (!userRepository.exists(user.userId))
            throw UserIdNotFoundException(user.userId + "is not found.")

        userRepository.save(user)
        return user
    }

    open fun delete(id: String) {
        userRepository.delete(id)
    }

    open fun find(id: String): User {
        return userRepository.findOne(id)
    }

    open fun getUnFollowing10Users(loginUser: User, twitterCloneController: TwitterCloneController): List<User> {
        TwitterCloneController.log.info("loginuser is: " + loginUser.toString())

        val alluser = findAll()
        val following = loginUser.following
        val unFollowing10Users = alluser.stream()
                .limit(10)
                .filter { u -> !(following.contains(u) || u == loginUser) }
                .collect(Collectors.toList<User>())
        return unFollowing10Users
    }
}
