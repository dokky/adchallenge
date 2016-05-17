package ad.challenge.model

import ad.challenge.model.SubscriptionManagementModel.SubscriptionStatus.SubscriptionStatus

object SubscriptionManagementModel {

  object SubscriptionStatus extends Enumeration {
    type SubscriptionStatus = Value
    val ACTIVE, CANCELLED = Value
  }

  case class Subscription(accountIdentifier: String, subscriptionStatus: SubscriptionStatus)

}
