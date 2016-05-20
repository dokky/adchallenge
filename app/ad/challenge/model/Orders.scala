package ad.challenge.model

import ad.challenge.model.MarketplaceModel.{Account, Company, Order, User}


trait Users {

  def findById(id: String): Option[User]

  def findByOpenId(openId: String): Option[User]

  def findByCompanyId(companyId: String): Iterable[User]

  def save(user: User): User

  def delete(user: User)

}

trait Companies {

  def findById(id: String): Option[Company]

  def save(company: Company): Company

  def delete(company: Company)

}


trait Accounts {

  def findById(id: String): Option[Account]

  def save(account: Account): Account

  def delete(account: Account)

}

trait Orders {

  def save(order: Order): Order

}
