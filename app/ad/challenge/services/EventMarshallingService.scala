package ad.challenge.services

import ad.challenge.model.MarketplaceEventModel.MarketplaceEvent
import play.api.libs.json.{JsError, JsSuccess, JsValue, Reads}

class EventMarshallingService extends Logging {

  def unmarshal[T <: MarketplaceEvent](jsonEvent: JsValue)(implicit reads: Reads[T]): Option[T] =
    jsonEvent.validate[T] match {
      case s: JsSuccess[T] => Some(s.get)
      case e: JsError =>
        logger.error(s"could not unmarshal event from json:\n $jsonEvent\n Errors:\n" + JsError.toJson(e.errors))
        None
    }


}
