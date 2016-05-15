package services

import ad.challenge.model.SubscriptionManagementModel.Event
import play.api.libs.json.{JsError, JsSuccess, JsValue, Reads}

class EventMarshallingService extends Logging {

  def unmarshal[T <: Event](jsonEvent: JsValue)(implicit reads: Reads[T]): Option[T] =
    jsonEvent.validate[T] match {
      case s:JsSuccess[T] => Some(s.get)
      case e:JsError =>
        logger.error(s"could not unmarshal event from json: $jsonEvent: " + JsError.toJson(e.errors))
        None
    }


}
