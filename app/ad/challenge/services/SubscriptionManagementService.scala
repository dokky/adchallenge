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
                                              orders: Orders)
  extends EventSupport with Logging {

  def processSubscriptionOrderEvent(eventUrl: String): Future[Option[Account]] = {
    (for {
      subscriptionOrder <- optionT(eventRetrieverService.retrieveEvent[SubscriptionOrderEvent](eventUrl))
      accountIdentifier <- optionT(process(subscriptionOrder))
    } yield accountIdentifier).run
  }

  def processSubscriptionCancelEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    (for {
      subscriptionCancel <- optionT(eventRetrieverService.retrieveEvent[SubscriptionCancelEvent](eventUrl))
      accountIdentifier <- optionT(process(subscriptionCancel))
    } yield accountIdentifier).run
  }

  def processSubscriptionChangeEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    (for {
      subscriptionChange <- optionT(eventRetrieverService.retrieveEvent[SubscriptionChangeEvent](eventUrl))
      accountIdentifier <- optionT(process(subscriptionChange))
    } yield accountIdentifier).run
  }

  def processSubscriptionNoticeEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    (for {
      subscriptionNotice <- optionT(eventRetrieverService.retrieveEvent[SubscriptionNoticeEvent](eventUrl))
      accountIdentifier <- optionT(process(subscriptionNotice))
    } yield accountIdentifier).run
  }


  private def process(subscriptionOrder: SubscriptionOrderEvent): Future[Option[Account]] = {
    val account: Account = subscriptionOrder match {
      case SubscriptionOrderEvent(EventMetadata(Some(EventFlag.STATELESS), _), _, company, _) => Account(company.id, Some(AccountStatus.ACTIVE))
      case SubscriptionOrderEvent(_, user, company, order) =>

        val c = companies.save(company)
        val u = users.save(user.copy(companyId = Some(c.id)))
        val o = orders.save(order)
        accounts.save(Account(c.id, Some(AccountStatus.ACTIVE)))
    }
    Future.successful(Some(account))
  }

  private def process(event: SubscriptionCancelEvent): Future[Option[ResponseEvent]] = StatelessEventSupport(event) {
    val result = event match {
      case SubscriptionCancelEvent(_, account) => cancelAccount(account)
    }

    Future.successful(Some(result match {
      case Some(account) => ResponseEvent()
      case None => ResponseEvent(ErrorCode.ACCOUNT_NOT_FOUND, Some("Account not found"))
    }))

  }

  private def cancelAccount(account: Account): Option[Account] = {
    companies.findByAccount(account).foreach({ company =>
      for (user <- users.findByCompanyId(company.id))
        users.delete(user)

      companies.delete(company)
    }
    )
    accounts.delete(account)
  }

  private def process(event: SubscriptionChangeEvent): Future[Option[ResponseEvent]] = StatelessEventSupport(event) {
    val result = event match {
      case SubscriptionChangeEvent(_, account, order) =>

        for {
          a <- accounts.findById(account.id)
          c <- companies.findByAccount(a)
          o <- orders.update(order)
        } yield a
    }

    Future.successful(Some(result match {
      case Some(account) => ResponseEvent()
      case None => ResponseEvent(ErrorCode.ACCOUNT_NOT_FOUND, Some("Account not found"))
    }))
  }

  private def process(event: SubscriptionNoticeEvent): Future[Option[ResponseEvent]] = StatelessEventSupport(event) {
    val result = event match {
      case SubscriptionNoticeEvent(_, account, notice) =>
        accounts.findById(account.id).map { ac =>
          notice match {
            case Notice("DEACTIVATED") => accounts.save(ac.copy(status = Some(AccountStatus.SUSPENDED)))
            case Notice("REACTIVATED") => accounts.save(ac.copy(status = Some(AccountStatus.ACTIVE)))
            case Notice("CLOSED") => cancelAccount(ac)
          }
        }
    }

    Future.successful(Some(result match {
      case Some(account) => ResponseEvent()
      case None => ResponseEvent(ErrorCode.ACCOUNT_NOT_FOUND, Some("Account not found"))
    }))
  }


}
