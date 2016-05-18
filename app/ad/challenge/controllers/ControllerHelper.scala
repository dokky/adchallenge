package ad.challenge.controllers

import ad.challenge.model.ErrorCode
import ad.challenge.model.ErrorCode.ErrorCode
import ad.challenge.model.ErrorCode.ErrorCode
import ad.challenge.model.MarketplaceEventModel.ResponseEvent
import ad.challenge.services.{AuthenticationResult, AuthenticationService, Logging}
import play.api.http.ContentTypes
import play.api.mvc.Results._
import play.api.mvc.{Action, Result, Results}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait ControllerHelper extends Logging {

  def SecuredAsyncAction(auth: AuthenticationService)(block: => Future[Result]) = Action.async { implicit request =>
    logger.info(s"inspecting incoming request $request")
    auth.checkAuthentication(request) match {
      case AuthenticationResult.VALID => block.recover(defaultExceptionHandler)
      case AuthenticationResult.NOT_VALID =>
        logger.error(s"The oauth signature for request ${request.path} is invalid")
        Future.successful(Results.Unauthorized("OAuth signature could not be recognized"))
    }
  }

  def defaultExceptionHandler: PartialFunction[Throwable, Result] = {
    case e: Exception =>
      logger.error(s"Failed to process event", e)
      error(ErrorCode.UNKNOWN_ERROR, s"Failed to process event: $e")
  }


  def ok(message: String): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>true</success>
          |  <message>$message</message>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)

  def response(event: ResponseEvent): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>${event.success}</success>
          |  <message>${event.message}</message>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)


  def error(code: ErrorCode, message: String): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>false</success>
          |  <errorCode>$code</errorCode>
          |  <message>$message</message>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)


}
