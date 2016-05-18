package ad.challenge.services.persistense.inmemory

import ad.challenge.model.Accounts
import ad.challenge.model.MarketplaceModel.Account

class AccountsInMemoryStore extends Accounts {
  override def findById(id: String): Option[Account] = None

  override def delete(account: Account) = {}

  override def save(account: Account): Account = account
}
