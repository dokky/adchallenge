package ad.challenge.services.persistense.inmemory

import ad.challenge.model.Accounts
import ad.challenge.model.MarketplaceModel.Account

import scala.collection.JavaConverters._
import scala.language.postfixOps

class AccountsInMemoryStore extends Accounts {
  val accounts = new java.util.concurrent.ConcurrentHashMap[String, Account] asScala

  override def findById(id: String): Option[Account] = accounts.get(id)

  override def delete(account: Account) = accounts.remove(account.id)

  override def save(account: Account): Account = {
    accounts.put(account.id, account)
    account
  }
}
