package ad.challenge.services

import ad.challenge.services.AuthenticationResult.AuthenticationResult

object AuthenticationResult extends Enumeration {
  type AuthenticationResult = Value
  val VALID, NOT_VALID = Value
}

trait AuthenticationService {

  def checkAuthentication[T](implicit request: play.api.mvc.Request[T]): AuthenticationResult

}
