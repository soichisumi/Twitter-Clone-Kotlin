package yoyoyousei.twitter.clone.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import yoyoyousei.twitter.clone.domain.model.User

interface UserRepository : JpaRepository<User, String>//User findFirstByUserId(String userId);//他にfindTop10By...が使える
