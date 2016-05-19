package ad.challenge.services

import javax.inject.{Inject, Singleton}

import ad.challenge.model.MarketplaceEventModel._
import ad.challenge.model._

import scala.concurrent.Future


@Singleton
class UserAssignmentManagementService @Inject()(eventRetrieverService: EventRetrieverService,
                                                users: Users,
                                                companies: Companies,
                                                accounts: Accounts,
                                                orders: Orders) {


  def processUserAssignmentEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    Future.successful(Some(ResponseEvent()))
  }

  def processUserUnassignmentEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    Future.successful(Some(ResponseEvent()))
  }


}
