package ad.challenge.controllers

import javax.inject._

import ad.challenge.model.ErrorCode
import ad.challenge.services._
import ad.challenge.services.security.AuthenticationService
import play.api.http.ContentTypes
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SubscriptionManagementController @Inject()(auth: AuthenticationService,
                                                 subscriptionManagement: SubscriptionManagementService)
  extends Controller with ControllerHelper with Logging {


  def create(eventUrl: String) = SecuredAsyncAction(auth) {
    subscriptionManagement.processSubscriptionOrderEvent(eventUrl) map {
      case Some(account) => accountCreatedResponse(account.id)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }

  def cancel(eventUrl: String) = SecuredAsyncAction(auth) {
    subscriptionManagement.processSubscriptionCancelEvent(eventUrl) map {
      case Some(event) => response(event)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }

  def change(eventUrl: String) = SecuredAsyncAction(auth) {
    subscriptionManagement.processSubscriptionChangeEvent(eventUrl) map {
      case Some(event) => response(event)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }

  def notice(eventUrl: String) = SecuredAsyncAction(auth) {
    subscriptionManagement.processSubscriptionNoticeEvent(eventUrl) map {
      case Some(event) => response(event)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }


  private def accountCreatedResponse(id: String): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>true</success>
          |  <message>Account creation successful</message>
          |  <accountIdentifier>$id</accountIdentifier>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)


}
