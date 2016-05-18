package ad.challenge.model


import ad.challenge.model.ErrorCode.ErrorCode
import ad.challenge.model.EventFlag.EventFlag
import ad.challenge.model.MarketplaceEventModel.{EventMetadata, SubscriptionOrderEvent}
import ad.challenge.model.MarketplaceModel._

object EventFlag extends Enumeration {
  type EventFlag = Value
  val STATELESS, NONE = Value
}

object ErrorCode extends Enumeration {
  type ErrorCode = Value
  val UNAUTHORIZED,
  NOT_IMPLEMENTED,
  UNKNOWN_ERROR,
  NONE
  = Value
}

object MarketplaceEventModel {

  case class EventMetadata(flag: Option[EventFlag], marketplace: Marketplace)

  sealed trait MarketplaceEvent {
    val meta: EventMetadata
  }

  case class SubscriptionOrderEvent(meta: EventMetadata, creator: User, company: Company, order: Order) extends MarketplaceEvent

  case class SubscriptionCancelEvent(meta: EventMetadata, account: Account) extends MarketplaceEvent

  case class SubscriptionNoticeEvent(meta: EventMetadata, account: Account, notice: Notice) extends MarketplaceEvent

  case class SubscriptionChangeEvent(meta: EventMetadata, account: Account, order: Order) extends MarketplaceEvent

  case class ResponseEvent(errorCode: ErrorCode = ErrorCode.NONE, message: Option[String] = None) {
    def success = errorCode == ErrorCode.NONE
  }

}

object MarketplaceEventModelReads {

  import ad.challenge.model.MarketplaceModel._
  import ad.challenge.model.MarketplaceModelReads._
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  //  EVENTS

  implicit val eventFlagReads = Reads.enumNameReads(EventFlag)

  implicit val eventMetadataReads: Reads[EventMetadata] = (
    (__ \ "flag").readNullable[EventFlag] ~
      (__ \ "marketplace").read[Marketplace]
    ) (EventMetadata.apply _)

  implicit val subscriptionOrderReads: Reads[SubscriptionOrderEvent] = (
    __.read[EventMetadata] ~
      (__ \ "creator").read[User] ~
      (__ \ "payload" \ "company").read[Company] ~
      (__ \ "payload" \ "order").read[Order]
    ) (SubscriptionOrderEvent.apply _)

}
