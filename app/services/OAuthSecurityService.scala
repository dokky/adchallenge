package services

import java.net.URLDecoder
import java.util
import javax.inject.Inject

import org.asynchttpclient.oauth.OAuthSignatureCalculator
import org.asynchttpclient.uri.Uri
import org.asynchttpclient.util.Utf8UrlEncoder
import org.asynchttpclient.{Param, RequestBuilderBase, SignatureCalculator}
import play.api.Configuration
import play.api.libs.oauth.{ConsumerKey, RequestToken}
import play.api.libs.ws.WSSignatureCalculator

object SignatureValidationResult extends Enumeration {
  type SignatureValidationResult = Value
  val VALID, NOT_VALID = Value
}

class OAuthSecurityService  @Inject() (configuration: Configuration)  {

  private val oAuthConsumerKey = configuration.getString("ad.OAuthConsumerKey").get
  private val oAuthConsumerSecret = configuration.getString("ad.OAuthConsumerSecret").get

  private lazy val consumerKey = ConsumerKey(oAuthConsumerKey, oAuthConsumerSecret)

  lazy val oAuthCalculator = new OAuthWSSignatureCalculator(consumerKey, RequestToken("", ""))


  /**
    * The public AsyncHttpClient implementation of WSSignatureCalculator.
    */
  class OAuthWSSignatureCalculator(consumerKey: ConsumerKey, requestToken: RequestToken) extends WSSignatureCalculator with SignatureCalculator {

    import org.asynchttpclient.oauth.{ConsumerKey => AHCConsumerKey, RequestToken => AHCRequestToken}

    private val ahcConsumerKey = new AHCConsumerKey(consumerKey.key, consumerKey.secret)
    private val ahcRequestToken = new AHCRequestToken(requestToken.token, requestToken.secret)
    private val calculator = new OAuthSignatureCalculator(ahcConsumerKey, ahcRequestToken)

    override def calculateAndAddSignature(request: org.asynchttpclient.Request, requestBuilder: RequestBuilderBase[_]): Unit = {
      calculator.calculateAndAddSignature(request, requestBuilder)
    }

    def calculateSignature(method: String, uri: Uri, oauthTimestamp: Long, nonce: String, formParams: util.List[Param], queryParams: util.List[Param]) =
      calculator.calculateSignature(method, uri, oauthTimestamp, nonce, formParams, queryParams)
    }


  private val authRegex = ".*oauth_nonce=\"([^\"]*)\".*oauth_signature=\"([^\"]*)\".*oauth_timestamp=\"([^\"]*)\".*".r

  import services.SignatureValidationResult.SignatureValidationResult

  def checkOAuthSignature[T] (implicit request: play.api.mvc.Request[T]): SignatureValidationResult =
    request.headers.get(OAuthSignatureCalculator.HEADER_AUTHORIZATION) match {
      case Some(authorizationHeader) =>

        authorizationHeader match {
          case authRegex(nonce: String, providedSignature: String, timestamp: String) =>
            val params = request.queryString.map(query => new Param(Utf8UrlEncoder.encodeQueryElement(query._1), Utf8UrlEncoder.encodeQueryElement(query._2.head))).toSeq
            val protocol = if (request.secure) "https" else "http"
            val url = s"$protocol://${request.host}${request.path}"

            val expectedSignature = oAuthCalculator.calculateSignature(
              request.method,
              Uri.create(url),
              timestamp.toInt,
              nonce,
              new util.ArrayList(),
              scala.collection.JavaConversions.seqAsJavaList(params)
            )
            if (expectedSignature.equals(URLDecoder.decode(providedSignature, "UTF-8"))) SignatureValidationResult.VALID else SignatureValidationResult.NOT_VALID
          case _ => SignatureValidationResult.NOT_VALID
        }
      case None => SignatureValidationResult.NOT_VALID
    }

}
