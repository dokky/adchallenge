package ad.challenge.model


import ad.challenge.model.ErrorCode.ErrorCode
import ad.challenge.model.EventFlag.EventFlag
import ad.challenge.model.MarketplaceEventModel._
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

  case class UserAssignmentEvent(meta: EventMetadata, account: Account, user: User) extends MarketplaceEvent

  case class UserUnassignmentEvent(meta: EventMetadata, account: Account, user: User) extends MarketplaceEvent

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

  implicit val subscriptionCancelReads: Reads[SubscriptionCancelEvent] = (
    __.read[EventMetadata] ~
      (__ \ "payload" \ "account").read[Account]
    ) (SubscriptionCancelEvent.apply _)

  implicit val subscriptionChangeReads: Reads[SubscriptionChangeEvent] = (
    __.read[EventMetadata] ~
      (__ \ "payload" \ "account").read[Account] ~
      (__ \ "payload" \ "order").read[Order]
    ) (SubscriptionChangeEvent.apply _)

  implicit val noticeReads: Reads[Notice] = (__ \ "type").read[String].map(Notice)

  implicit val subscriptionNoticeReads: Reads[SubscriptionNoticeEvent] = (
    __.read[EventMetadata] ~
      (__ \ "payload" \ "account").read[Account] ~
      (__ \ "payload" \ "notice").read[Notice]
    ) (SubscriptionNoticeEvent.apply _)


  implicit val userAssignmentEventReads: Reads[UserAssignmentEvent] = (
    __.read[EventMetadata] ~
      (__ \ "payload" \ "account").read[Account] ~
      (__ \ "payload" \ "user").read[User]
    ) (UserAssignmentEvent.apply _)

  implicit val userUnassignmentEventReads: Reads[UserUnassignmentEvent] = (
    __.read[EventMetadata] ~
      (__ \ "payload" \ "account").read[Account] ~
      (__ \ "payload" \ "user").read[User]
    ) (UserUnassignmentEvent.apply _)


}
