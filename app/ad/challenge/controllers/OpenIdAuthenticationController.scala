package ad.challenge.controllers

import javax.inject._

import ad.challenge.model.Users
import ad.challenge.services._
import play.api.libs.json._
import play.api.libs.openid.OpenIdClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class OpenIdAuthenticationController @Inject()(openIdClient: OpenIdClient, users: Users)
  extends Controller with ControllerHelper with Logging {


  def login(openIdUrl: String) = Action.async { implicit request =>
    val callbackUrl = "login"
    val realmUrl = routes.ADChallengeSampleWebAppController.index.absoluteURL()

    openIdClient.redirectURL(openIdUrl, callbackUrl, realm = Some(realmUrl))
      .map(url => Redirect(url))
      .recover {
        case e: Exception =>
          logger.error("exception happened during openId redirect phase", e)
          BadRequest(Json.obj("error" → s"Failed process openId url $openIdUrl \n    ${e.getMessage}"))
      }
  }

  def verify = Action.async { implicit request =>
    openIdClient.verifiedId(request).map { userInfo =>
      logger.info(s"User logged in $userInfo")
      users.findByOpenId(userInfo.id) match {
        case Some(user) => Redirect(routes.ADChallengeSampleWebAppController.index).withSession("openId" -> user.id)
        case None => Unauthorized.withNewSession
      }
    } recover {
      case e: Exception => logger.error("exception happened during openId verification", e)
        Unauthorized.withNewSession
    }
  }

  def logout = Action { implicit request =>
    Redirect(routes.ADChallengeSampleWebAppController.index).withNewSession
  }

}
