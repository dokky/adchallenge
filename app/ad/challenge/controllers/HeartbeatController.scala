package ad.challenge.controllers

import play.api.mvc.{Action, Controller}

class HeartbeatController extends Controller {
  def ping = Action {
    Ok("pong")
  }
}
