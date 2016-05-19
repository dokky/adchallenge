package ad.challenge.services.persistense.inmemory

import ad.challenge.model.MarketplaceModel.User
import ad.challenge.model.Users

import scala.collection.JavaConverters._
import scala.language.postfixOps


class UsersInMemoryStore extends Users {

  val users = new java.util.concurrent.ConcurrentHashMap[String, User] asScala

  override def findById(id: String): Option[User] = users.get(id)

  override def findByOpenId(openId: String): Option[User] = users.values.find(_.openId == openId)

  override def delete(user: User): Unit = {users.remove(user.id)}

  override def findByCompanyId(companyId: Long): Iterable[User] = users.values.filter(_.companyId == companyId)

  override def save(user: User): User = {
    users.putIfAbsent(user.id, user)
    user
  }
}
