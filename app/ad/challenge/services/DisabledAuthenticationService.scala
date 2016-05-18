package ad.challenge.services

import ad.challenge.services.AuthenticationResult.AuthenticationResult
import play.api.mvc.Request

class DisabledAuthenticationService extends AuthenticationService {

  override def checkAuthentication[T](implicit request: Request[T]): AuthenticationResult = AuthenticationResult.VALID
}
