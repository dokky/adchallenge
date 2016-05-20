package ad.challenge.services

import javax.inject.{Inject, Singleton}

import ad.challenge.model.MarketplaceEventModel._
import ad.challenge.model.MarketplaceEventModelReads._
import ad.challenge.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.OptionT._
import scalaz.Scalaz._


@Singleton
class UserAssignmentManagementService @Inject()(eventRetrieverService: EventRetrieverService,
                                                users: Users,
                                                companies: Companies,
                                                accounts: Accounts,
                                                orders: Orders)
  extends EventSupport with Logging {


  def processUserAssignmentEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    (for {
      event <- optionT(eventRetrieverService.retrieveEvent[UserAssignmentEvent](eventUrl))
      accountIdentifier <- optionT(process(event))
    } yield accountIdentifier).run

  }

  def processUserUnassignmentEvent(eventUrl: String): Future[Option[ResponseEvent]] = {
    (for {
      event <- optionT(eventRetrieverService.retrieveEvent[UserUnassignmentEvent](eventUrl))
      accountIdentifier <- optionT(process(event))
    } yield accountIdentifier).run
  }


  private def process(event: UserAssignmentEvent): Future[Option[ResponseEvent]] = StatelessEventSupport(event) {
    val result = event match {
      case UserAssignmentEvent(_, account, user) => companies.findByAccount(account).map(company => users.save(user.copy(companyId = Some(company.id))))
    }

    Future.successful(Some(
      result match {
        case Some(user) => ResponseEvent()
        case None => ResponseEvent(ErrorCode.USER_NOT_FOUND, Some("User not found"))
      }))

  }

  private def process(event: UserUnassignmentEvent): Future[Option[ResponseEvent]] = StatelessEventSupport(event) {
    val result = event match {
      case UserUnassignmentEvent(_, account, user) => companies.findByAccount(account).flatMap(company => users.delete(user))
    }

    Future.successful(Some(
      result match {
        case Some(user) => ResponseEvent()
        case None => ResponseEvent(ErrorCode.USER_NOT_FOUND, Some("User not found"))
      }))

  }

}
