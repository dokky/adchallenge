package services

import ad.challenge.model.SubscriptionManagementModel.Event
import play.api.libs.json.{JsError, JsSuccess, JsValue, Reads}

class EventMarshallingService extends Logging {

  def unmarshal[T <: Event](event: JsValue)(implicit reads: Reads[T]): Option[T] =
    event.validate[T] match {
      case s:JsSuccess => Some(s.get)
      case e:JsError =>
        logger.error(s"could not unmarshal object from json: $event: " + JsError.toJson(e.errors))
        None
    }


}
