import ad.challenge.model.MarketplaceEventModel._
import ad.challenge.model.MarketplaceModel._
import ad.challenge.model.{AccountStatus, EventFlag}
import ad.challenge.services.EventMarshallingService
import org.scalatestplus.play._
import play.api.libs.json.Json

class MarshallingSpec extends PlaySpec with OneAppPerTest {

  val marshalling = new EventMarshallingService()

  "Marshalling Logic" should {

    "parse json and unmarshal to an object" in {
      val event =
        """
          |{
          |	"type": "SUBSCRIPTION_ORDER",
          |	"marketplace": {
          |		"partner": "ACME",
          |		"baseUrl": "https://acme.appdirect.com"
          |	},
          |	"applicationUuid": null,
          |	"flag": "STATELESS",
          |	"creator": {
          |		"uuid": "ec5d8eda-5cec-444d-9e30-125b6e4b67e2",
          |		"openId": "https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2",
          |		"email": "test-email+creator@appdirect.com",
          |		"firstName": "DummyCreatorFirst",
          |		"lastName": "DummyCreatorLast",
          |		"language": "fr",
          |		"address": null,
          |		"attributes": null
          |	},
          |	"payload": {
          |		"user": null,
          |		"company": {
          |			"uuid": "d15bb36e-5fb5-11e0-8c3c-00262d2cda03",
          |			"externalId": null,
          |			"name": "Example Company Name",
          |			"email": "company-email@example.com",
          |			"phoneNumber": "415-555-1212",
          |			"website": "http://www.example.com",
          |			"country": "CA"
          |		},
          |		"account": null,
          |		"addonInstance": null,
          |		"addonBinding": null,
          |		"order": {
          |			"editionCode": "BASIC",
          |			"addonOfferingCode": null,
          |			"pricingDuration": "MONTHLY",
          |			"items": [{
          |				"unit": "USER",
          |				"quantity": 10
          |			}, {
          |				"unit": "MEGABYTE",
          |				"quantity": 15
          |			}]
          |		},
          |		"notice": null,
          |		"configuration": {
          |			"domain": "mydomain"
          |		}
          |	},
          |	"returnUrl": "https://www.appdirect.com/finishprocure?token=dummyOrder",
          |	"links": []
          |}
          |
        """.stripMargin

      import ad.challenge.model.MarketplaceEventModelReads._
      val orderO = marshalling.unmarshal[SubscriptionOrderEvent](Json.parse(event))
      orderO mustBe Some(
        SubscriptionOrderEvent(
          EventMetadata(
            Some(EventFlag.STATELESS),
            Marketplace("ACME", "https://acme.appdirect.com")
          )
          ,
          User(
            "ec5d8eda-5cec-444d-9e30-125b6e4b67e2",
            "https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2",
            None,
            Some("DummyCreatorFirst"),
            Some("DummyCreatorLast"),
            Some("test-email+creator@appdirect.com"),
            None,
            Some("fr")
          ),
          Company(
            "d15bb36e-5fb5-11e0-8c3c-00262d2cda03",
            Some("Example Company Name"),
            Some("company-email@example.com"),
            Some("CA"),
            Some("4155551212"),
            Some("http://www.example.com")
          )
          ,
          Order(
            "BASIC",
            "MONTHLY",
            Some(List(
              OrderItem("USER", 10),
              OrderItem("MEGABYTE", 15)
            )))
        ))
    }

    "parse json and unmarshal to an subscription notice event" in {
      val event =
        """
          |{
          |	"type": "SUBSCRIPTION_NOTICE",
          |	"marketplace": {
          |		"partner": "ACME",
          |		"baseUrl": "https://acme.appdirect.com"
          |	},
          |	"applicationUuid": null,
          |	"flag": "STATELESS",
          |	"creator": null,
          |	"payload": {
          |		"user": null,
          |		"company": null,
          |		"account": {
          |			"accountIdentifier": "dummy-account",
          |			"status": "SUSPENDED",
          |			"parentAccountIdentifier": null
          |		},
          |		"addonInstance": null,
          |		"addonBinding": null,
          |		"order": null,
          |		"notice": {
          |			"type": "DEACTIVATED",
          |			"message": null
          |		},
          |		"configuration": {}
          |	},
          |	"returnUrl": null,
          |	"links": []
          |}
        """.stripMargin

      import ad.challenge.model.MarketplaceEventModelReads._
      val orderO = marshalling.unmarshal[SubscriptionNoticeEvent](Json.parse(event))
      orderO mustBe Some(
        SubscriptionNoticeEvent(
          EventMetadata(
            Some(EventFlag.STATELESS),
            Marketplace("ACME", "https://acme.appdirect.com")),
          Account("dummy-account", Some(AccountStatus.SUSPENDED)),
          Notice("DEACTIVATED"))
      )

    }

  }


}
