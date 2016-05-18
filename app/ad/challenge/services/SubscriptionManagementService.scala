package ad.challenge.services

import javax.inject.{Inject, Singleton}

import ad.challenge.model.MarketplaceEventModel._
import ad.challenge.model.MarketplaceEventModelReads._
import ad.challenge.model.MarketplaceModel._
import ad.challenge.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.OptionT._
import scalaz.Scalaz._


@Singleton
class SubscriptionManagementService @Inject()(eventRetrieverService: EventRetrieverService,
                                              users: Users,
                                              companies: Companies,
                                              accounts: Accounts,
                                              orders: Orders) {

  def processSubscriptionOrderEvent(eventUrl: String): Future[Option[Account]] = {
    (for {
      subscriptionOrder <- optionT(eventRetrieverService.retrieveEvent[SubscriptionOrderEvent](eventUrl))
      accountIdentifier <- optionT(process(subscriptionOrder))
    } yield accountIdentifier).run
  }

  private def process(subscriptionOrder: SubscriptionOrderEvent): Future[Option[Account]] = {
    val account:Account = subscriptionOrder match {
      case SubscriptionOrderEvent(EventMetadata(Some(EventFlag.STATELESS), _), _, company, _) => Account(company.id, Some(AccountStatus.ACTIVE))
      case SubscriptionOrderEvent(_, user, company, order) =>

        val c = companies.save(company)
        val u = users.save(user)
        val o = orders.save(order)
        accounts.save(Account(c.id, Some(AccountStatus.ACTIVE)))
    }
    Future.successful(Some(account))
  }


  def processSubscriptionCancelEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    Future.successful(Some(ResponseEvent()))
  }

  def processSubscriptionChangeEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    Future.successful(Some(ResponseEvent()))
  }

  def processSubscriptionNoticeEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    Future.successful(Some(ResponseEvent()))
  }

}
