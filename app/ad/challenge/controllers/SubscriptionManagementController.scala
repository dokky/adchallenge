package ad.challenge.controllers

import javax.inject._

import ad.challenge.services._
import play.api.http.ContentTypes
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SubscriptionManagementController @Inject()(oauth: OAuthSecurityService,
                                                 subscriptionManagement: SubscriptionManagementService)
  extends Controller with ControllerHelper with Logging {

  def ping = Action {
    Ok("pong")
  }

  def create(eventUrl: String) = SecuredAsyncAction(oauth) {
    subscriptionManagement.processSubscriptionOrderEvent(eventUrl) map {
      case Some(id: String) => accountCreatedResponse(id)
      case None => error("bla", "Failed to process request")
    }
  }

  def cancel(eventUrl: String) = SecuredAsyncAction(oauth) {
    Future.successful(error("NOT_IMPLEMENTED", "Comming soon"))
  }

  def change(eventUrl: String) = SecuredAsyncAction(oauth) {
    Future.successful(error("NOT_IMPLEMENTED", "Comming soon"))
  }

  def status(eventUrl: String) = SecuredAsyncAction(oauth) {
    Future.successful(error("NOT_IMPLEMENTED", "Comming soon"))
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
