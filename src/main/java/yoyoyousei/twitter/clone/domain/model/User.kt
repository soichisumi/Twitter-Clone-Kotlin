package yoyoyousei.twitter.clone.domain.model

import javax.persistence.*


@Entity
class User {

    @Id
    lateinit var userId: String
    //TODO: not nullアノテーションがないとDBのカラムがnotnullにならないかどうか。kotlinなのでnotnull属性ついてほしい
    lateinit var password: String

    lateinit var screenName: String

    var roleName = RoleName.USER

    var biography = ""
    var iconPath = "/images/noicon.png"

    //双方向ならmappedbyが必要で、どのプロパティと関連するのか指定する必要がある。
    @OneToMany(mappedBy = "tweetUser")
    var tweets = mutableListOf<Tweet>()

    //fetchtype: Eager フィールドの呼び出しを最初の呼び出しで行う  lazy:フィールドにアクセスが合った時点で
    //cascade: このプロパティをどのように変更した際に関連するentityに変更を反映するか
    //persist:新規保存 merge:更新 remove:削除 refresh:再取得したとき detatch:永続性コンテキストの管理外になったとき all:全て
    //cascade={hoge,fuga}と複数指定できる
    //勝手に反映してほしくないしcascade不要
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "relation", joinColumns = arrayOf(JoinColumn(name = "user_id")), inverseJoinColumns = arrayOf(JoinColumn(name = "following_id")))
    var following= mutableListOf<User>()

    constructor(userId: String, password: String, screenName: String?) {
        this.userId = userId
        this.password = password
        this.screenName =   if (screenName == null || screenName == "")
                                "no name"
                            else
                                screenName
    }

    //spring jpaに必要
    constructor()

    override fun toString(): String {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", screenName='" + screenName + '\'' +
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is User) return false

        val user = o

        if (userId != user.userId) return false
        return password == user.password
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }
}
