import ad.challenge.model._
import ad.challenge.services.persistense.inmemory._
import ad.challenge.services.security.{OAuthSecurityService, AuthenticationService}
import com.google.inject.AbstractModule

class Module extends AbstractModule {

  override def configure() = {
    // to disable outh authentication security checks
    //    bind(classOf[AuthenticationService]).to(classOf[DisabledAuthenticationService])

    bind(classOf[AuthenticationService]).to(classOf[OAuthSecurityService])

    bind(classOf[Users]).to(classOf[UsersInMemoryStore])
    bind(classOf[Companies]).to(classOf[CompaniesInMemoryStore])
    bind(classOf[Accounts]).to(classOf[AccountsInMemoryStore])
    bind(classOf[Orders]).to(classOf[OrdersInMemoryStore])
  }
}
