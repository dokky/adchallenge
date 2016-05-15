package services

import javax.inject.Inject

import play.api.http.ContentTypes
import play.api.libs.json.JsValue
import play.api.libs.ws.WSClient
import play.mvc.Http.HeaderNames
import play.api.libs.oauth.{ConsumerKey, OAuthCalculator, RequestToken}

import scala.concurrent.Future

import   scala.concurrent.ExecutionContext.Implicits.global

class EventRetrieverService @Inject() (ws: WSClient, oauth: OAuthSecurityService) extends Logging {

  def retrievePayload(eventUrl: String): Future[Option[JsValue]] = {
    val result = ws.url(eventUrl)
      .withHeaders(HeaderNames.ACCEPT → ContentTypes.JSON)
      .sign(oauth.oAuthCalculator)
      .get()
      .map(
        response => response.status match {
          case 200 =>
            Some(response.json)
          case _ =>
            logger.error(s"$eventUrl returned [${response.status}] code")
            None
        }
      )

    result
  }

}
