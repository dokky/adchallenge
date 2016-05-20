package ad.challenge.model

import ad.challenge.model.AccountStatus.AccountStatus

object AccountStatus extends Enumeration {
  type AccountStatus = Value
  val ACTIVE, SUSPENDED, CANCELLED, FREE_TRIAL, FREE_TRIAL_EXPIRED = Value
}

object MarketplaceModel {

  case class Marketplace(partner: String, url: String)

  case class Company(id: String, name: Option[String], email: Option[String], country: Option[String], phone: Option[String], website: Option[String])

  case class User(id: String,
                  openId: String,
                  companyId: Option[String] = None,
                  firstName: Option[String] = None,
                  lastName: Option[String] = None,
                  email: Option[String] = None,
                  phone: Option[String] = None,
                  language: Option[String] = None
                  )

  case class Account(id: String, status: Option[AccountStatus])

  case class OrderItem(unit: String, quantity: Int)

  case class Order(edition: String, pricingDuration: String, items: Option[Seq[OrderItem]])

  case class Notice(noticeType: String)
}


object MarketplaceModelReads {

  import ad.challenge.model.MarketplaceModel._
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

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
      (__ \ "openId").read[String] ~
      (__ \ "companyId").readNullable[String] ~
      (__ \ "firstName").readNullable[String] ~
      (__ \ "lastName").readNullable[String] ~
      (__ \ "email").readNullable[String] ~
      (__ \ "phone").readNullable[String].map(_.map(_.replaceAll("[^0-9]", ""))) ~
      (__ \ "language").readNullable[String]
    ) (User.apply _)

  implicit val accountStatusReads = Reads.enumNameReads(AccountStatus)

  implicit val accountReads: Reads[Account] = (
    (__ \ "accountIdentifier").read[String] ~
      (__ \ "status").readNullable[AccountStatus]
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

}
