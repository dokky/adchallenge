package ad.challenge.controllers

import ad.challenge.services.{AuthenticationResult, AuthenticationService, Logging}
import play.api.http.ContentTypes
import play.api.mvc.Results._
import play.api.mvc.{Action, Result, Results}

import scala.concurrent.Future

trait ControllerHelper extends Logging {

  def SecuredAsyncAction(auth: AuthenticationService)(block: => Future[Result]) = Action.async { implicit request =>
    logger.info(s"inspecting incoming request $request")
    auth.checkAuthentication(request) match {
      case AuthenticationResult.VALID => block
      case AuthenticationResult.NOT_VALID =>
        logger.error(s"The oauth signature for request ${request.path} is invalid")
        Future.successful(Results.Unauthorized("OAuth signature could not be recognized"))
    }
  }

  def ok(message: String): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>true</success>
          |  <message>$message</message>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)

  def error(e: ad.challenge.model.MarketplaceEventModel.Error): Result =
    error(e.code, e.message)

  def error(code: String, message: String): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>false</success>
          |  <errorCode>$code</errorCode>
          |  <message>$message</message>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)


}
