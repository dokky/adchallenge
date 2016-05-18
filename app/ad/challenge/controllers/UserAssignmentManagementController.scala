package ad.challenge.controllers

import javax.inject._

import ad.challenge.model.ErrorCode
import ad.challenge.services._
import play.api.http.ContentTypes
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserAssignmentManagementController @Inject()(oauth: OAuthSecurityService,
                                                   accessManagementService: UserAssignmentManagementService)
  extends Controller with ControllerHelper with Logging {


  def assign(eventUrl: String) = SecuredAsyncAction(oauth) {
    accessManagementService.processUserAssignmentEvent(eventUrl) map {
      case Some(event) => response(event)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }

  def unassign(eventUrl: String) = SecuredAsyncAction(oauth) {
    accessManagementService.processUserUnassignmentEvent(eventUrl) map {
      case Some(event) => response(event)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }

  def update(eventUrl: String) = SecuredAsyncAction(oauth) {
    accessManagementService.processUserUpdateEvent(eventUrl) map {
      case Some(event) => response(event)
      case None => error(ErrorCode.UNKNOWN_ERROR, "Failed to process request")
    }
  }

}
