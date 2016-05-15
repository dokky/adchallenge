package controllers

import javax.inject._

import ad.challenge.model.SubscriptionManagementModel._
import ad.challenge.model.SubscriptionManagementModelReads._
import play.api._
import play.api.http.ContentTypes
import play.api.libs.json.JsValue
import play.api.mvc._
import services._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SubscriptionManagementController @Inject()(oauth: OAuthSecurityService,
                                                 subscriptionManagement: SubscriptionManagementService,
                                                 eventRetrieverService: EventRetrieverService)
  extends Controller with ControllerHelper with Logging {

  def ping = Action {
    Ok("pong")
  }


  private def SecuredAsyncAction(block: => Future[Result]) = Action.async { implicit request =>
    oauth.checkOAuthSignature(request) match {
      case SignatureValidationResult.VALID => block
      case SignatureValidationResult.NOT_VALID =>
        logger.error(s"The oauth signature for request ${request.path} is invalid")
        Future.successful(Results.Unauthorized("OAuth signature could not be recognized"))
    }
  }

  import scalaz.OptionT._
  import scalaz.Scalaz._

  def create(eventUrl: String) = SecuredAsyncAction {
    (for {
      subscriptionOrder <- optionT(eventRetrieverService.retrieveEvent[SubscriptionOrder](eventUrl))
      accountIdentifier <- optionT(subscriptionManagement.process(subscriptionOrder))
    } yield accountIdentifier).run map {
      case Some(id: String) => accountCreatedResponse(id)
      case None => error("bla", "Failed to process request")
    }
  }

  def cancel(eventUrl: String) = play.mvc.Results.TODO


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
