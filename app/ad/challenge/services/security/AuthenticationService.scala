package ad.challenge.services.security

import ad.challenge.services.security.AuthenticationResult.AuthenticationResult

object AuthenticationResult extends Enumeration {
  type AuthenticationResult = Value
  val VALID, NOT_VALID = Value
}

trait AuthenticationService {

  def checkAuthentication[T](implicit request: play.api.mvc.Request[T]): AuthenticationResult

}
