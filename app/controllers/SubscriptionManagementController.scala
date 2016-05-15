package controllers

import javax.inject._

import ad.challenge.model.SubscriptionManagementModel.SubscriptionOrder
import play.api._
import play.api.http.ContentTypes
import play.api.mvc._
import services._

import scala.concurrent.Future


@Singleton
class SubscriptionManagementController @Inject()(oauth: OAuthSecurityService,
                                                 subscriptionManagement: SubscriptionManagementService,
                                                 eventRetrieverService: EventRetrieverService,
                                                 marshalling: EventMarshallingService) extends Controller with ControllerHelper with Logging {

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


  def create(eventUrl: String) = SecuredAsyncAction {
    for {
      eventJson <- eventRetrieverService.retrievePayload(eventUrl)
      subscriptionOrder <- marshalling.unmarshal[SubscriptionOrder](eventJson.get) // todo remove .get
      accountIdentifier: String = subscriptionManagement.process(subscriptionOrder)
    } yield accountIdentifier map {
//      case id: String => accountCreated(id)
//      case e: Error => error(e)
      case _ => error("bla", "bla")
    }

  }

  def cancel(eventUrl: String) = play.mvc.Results.TODO


  private def accountCreated(id: String): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>true</success>
          |  <message>Account creation successful</message>
          |  <accountIdentifier>$id</accountIdentifier>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)



}
