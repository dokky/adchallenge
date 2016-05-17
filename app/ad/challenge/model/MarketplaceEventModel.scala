package ad.challenge.model

import ad.challenge.model.MarketplaceEventModel.{EventMetadata, SubscriptionOrderEvent}
import ad.challenge.model.MarketplaceModel._


object MarketplaceEventModel {

  case class EventMetadata(flag: Option[String], marketplace: Marketplace)

  abstract class MarketplaceEvent(val meta: EventMetadata)

  case class SubscriptionOrderEvent(override final val meta: EventMetadata, creator: User, company: Company, order: Order) extends MarketplaceEvent(meta)

  case class SubscriptionCancelEvent(override final val meta: EventMetadata, account: Account) extends MarketplaceEvent(meta)

  //  ERROR
  case class Error(code: String, message: String)

}

object MarketplaceEventModelReads {

  import ad.challenge.model.MarketplaceModel._
  import ad.challenge.model.MarketplaceModelReads._
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  //  EVENTS

  implicit val eventMetadataReads: Reads[EventMetadata] = (
    (__ \ "flag").readNullable[String] ~
      (__ \ "marketplace").read[Marketplace]
    ) (EventMetadata.apply _)

  implicit val subscriptionOrderReads: Reads[SubscriptionOrderEvent] = (
    __.read[EventMetadata] ~
      (__ \ "creator").read[User] ~
      (__ \ "payload" \ "company").read[Company] ~
      (__ \ "payload" \ "order").read[Order]
    ) (SubscriptionOrderEvent.apply _)

}
