package ad.challenge.services

import java.util.UUID
import javax.inject.{Inject, Singleton}

import ad.challenge.model.MarketplaceEventModel.SubscriptionOrderEvent

import scala.concurrent.Future
import scalaz.OptionT._


@Singleton
class SubscriptionManagementService @Inject()(eventRetrieverService: EventRetrieverService) {

  def processSubscriptionOrderEvent(eventUrl: String): Future[Option[String]] = {
    (for {
      subscriptionOrder <- optionT(eventRetrieverService.retrieveEvent[SubscriptionOrderEvent](eventUrl))
      accountIdentifier <- optionT(process(subscriptionOrder))
    } yield accountIdentifier).run
  }

  def process(subscriptionOrder: SubscriptionOrderEvent): Future[Option[String]] = {
    Future.successful(Some(UUID.randomUUID().toString))
  }


}
