package ad.challenge.model


object SubscriptionManagementModel {

  case class Marketplace(partner: String, url: String)

  case class Company(id: String, name: Option[String], email: Option[String], country: Option[String], phone: Option[String], website: Option[String])

  case class User(id: String, firstName: Option[String] = None, lastName: Option[String] = None, email: Option[String] = None, phone: Option[String] = None, language: Option[String] = None)

  case class Account(id: String, status: Option[String])

  case class OrderItem(unit: String, quantity: Int)

  case class Order(edition: String, pricingDuration: String, items: Option[Seq[OrderItem]])

  case class EventMetadata(product: String, kind: String, flag: Option[String], marketplace: Marketplace)

  abstract class Event(val meta: EventMetadata)

  case class SubscriptionOrder(override final val meta: EventMetadata, creator: User, company: Company, order: Order) extends Event(meta)

  case class SubscriptionCancel(override final val meta: EventMetadata, account: Account) extends Event(meta)

  case class Error(code: String, message: String)

}

object SubscriptionManagementModelReads {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import ad.challenge.model.SubscriptionManagementModel._

  implicit val marketplaceReads: Reads[Marketplace] = (
    (__ \ "partner").read[String] ~
      (__ \ "baseUrl").read[String]
    ) (Marketplace.apply _)

  implicit val companyReads: Reads[Company] = (
    (__ \ "uuid").read[String] ~
      (__ \ "name").readNullable[String] ~
      (__ \ "email").readNullable[String] ~
      (__ \ "country").readNullable[String] ~
      (__ \ "phoneNumber").readNullable[String].map(_.map(_.replaceAll("[^0-9]", ""))) ~
      (__ \ "website").readNullable[String]
    ) (Company.apply _)

  implicit val userReads: Reads[User] = (
    (__ \ "uuid").read[String] ~
      (__ \ "firstName").readNullable[String] ~
      (__ \ "lastName").readNullable[String] ~
      (__ \ "email").readNullable[String] ~
      (__ \ "phone").readNullable[String].map(_.map(_.replaceAll("[^0-9]", ""))) ~
      (__ \ "language").readNullable[String]
    ) (User.apply _)

  implicit val accountReads: Reads[Account] = (
    (__ \ "accountIdentifier").read[String] ~
      (__ \ "status").readNullable[String]
    ) (Account.apply _)

  implicit val itemReads: Reads[OrderItem] = (
    (__ \ "unit").read[String] ~
      (__ \ "quantity").read[Int]
    ) (OrderItem.apply _)

  implicit val orderReads: Reads[Order] = (
    (__ \ "editionCode").read[String] ~
      (__ \ "pricingDuration").read[String] ~
      (__ \ "items").readNullable[Seq[OrderItem]]
    ) (Order.apply _)

  implicit val eventMetadataReads: Reads[EventMetadata] = (
    (__ \ "product").read[String] ~
      (__ \ "" \ "type").read[String] ~
      (__ \ "" \ "flag").readNullable[String] ~
      (__ \ "" \ "marketplace").read[Marketplace]
    ) (EventMetadata.apply _)

  implicit val subscriptionOrderReads: Reads[SubscriptionOrder] = (
    __.read[EventMetadata] ~
      (__ \ "" \ "creator").read[User] ~
      (__ \ "" \ "" \ "company").read[Company] ~
      (__ \ "" \ "" \ "order").read[Order]
    ) (SubscriptionOrder.apply _)


}
