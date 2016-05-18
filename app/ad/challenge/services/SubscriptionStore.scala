package ad.challenge.services

import java.util.UUID

import ad.challenge.model.MarketplaceModel.{Company, User, Order}
import ad.challenge.model.SubscriptionManagementModel._

object SubscriptionStore {

  def create(creator: User, company: Company, order: Order): Subscription = Subscription(UUID.randomUUID().toString, subscriptionStatus = SubscriptionStatus.ACTIVE)

  def find(accountIdentifier: String): Subscription = ???

}
