package ad.challenge.services.persistense.inmemory

import ad.challenge.model.MarketplaceModel.User
import ad.challenge.model.Users


class UsersInMemoryStore extends Users{

  override def findById(id: String): Option[User] = None

  override def findByOpenId(openId: String)(): Option[User] = None

  override def delete(user: User): Unit = {}

  override def save(user: User): User = user

  override def findByCompanyId(companyId: Long): Seq[User] = Seq()
}
