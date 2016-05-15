package services

import java.util.UUID
import javax.inject.Singleton

import ad.challenge.model.SubscriptionManagementModel._
import services.SubscriptionStatus.SubscriptionStatus

import scala.concurrent.Future

object SubscriptionStatus extends Enumeration {
  type SubscriptionStatus = Value
  val ACTIVE, CANCELLED = Value
}

case class Subscription(accountIdentifier: String, subscriptionStatus: SubscriptionStatus)

object SubscriptionStore {

  def create(creator: User, company: Company, order: Order): Subscription = Subscription(UUID.randomUUID().toString, subscriptionStatus = SubscriptionStatus.ACTIVE)

  def find(accountIdentifier: String): Subscription = ???


}

@Singleton
class SubscriptionManagementService {

  def process(subscriptionOrder: SubscriptionOrder): Future[Option[String]] = {


    Future.successful(Some(UUID.randomUUID().toString))
  }


}
