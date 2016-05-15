package services

import java.util.UUID

import ad.challenge.model.SubscriptionManagementModel.SubscriptionOrder

class SubscriptionManagementService {


  def process(subscriptionOrder: SubscriptionOrder) = UUID.randomUUID().toString


}
