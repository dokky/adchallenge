package ad.challenge.controllers

import javax.inject._

import ad.challenge.model.ErrorCode
import ad.challenge.services._
import ad.challenge.services.security.AuthenticationService
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserAssignmentManagementController @Inject()(auth: AuthenticationService,
                                                   accessManagementService: UserAssignmentManagementService)
  extends Controller with ControllerHelper with Logging {


  def assign(eventUrl: String) = SecuredAsyncAction(auth) {
    accessManagementService.processUserAssignmentEvent(eventUrl) map {
      case Some(event) => response(event)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }

  def unassign(eventUrl: String) = SecuredAsyncAction(auth) {
    accessManagementService.processUserUnassignmentEvent(eventUrl) map {
      case Some(event) => response(event)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }

}
