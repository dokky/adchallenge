package ad.challenge.services.security

import ad.challenge.services.security.AuthenticationResult.AuthenticationResult
import play.api.mvc.Request

class DisabledAuthenticationService extends AuthenticationService {

  override def checkAuthentication[T](implicit request: Request[T]): AuthenticationResult = AuthenticationResult.VALID
}
