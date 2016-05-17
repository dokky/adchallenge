package ad.challenge.services

/**
  * Created by stanislavd on 17/05/2016.
  */
object SubscriptionStore {

  def create(creator: User, company: Company, order: Order): Subscription = Subscription(UUID.randomUUID().toString, subscriptionStatus = SubscriptionStatus.ACTIVE)

  def find(accountIdentifier: String): Subscription = ???

}
