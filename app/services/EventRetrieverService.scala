package services

import javax.inject.{Inject, Singleton}

import play.api.http.ContentTypes
import play.api.libs.json.{JsValue, Reads}
import play.api.libs.ws.WSClient
import play.mvc.Http.HeaderNames
import play.api.libs.oauth.{ConsumerKey, OAuthCalculator, RequestToken}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class EventRetrieverService @Inject() (ws: WSClient, oauth: OAuthSecurityService, marshalling: EventMarshallingService) extends Logging {

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

  def retrieveEvent[T<: ad.challenge.model.SubscriptionManagementModel.Event](eventUrl: String)(implicit reads: Reads[T]): Future[Option[T]] = {
    val result = retrievePayload(eventUrl)
    result.map(_.flatMap(marshalling.unmarshal[T]))
  }
}