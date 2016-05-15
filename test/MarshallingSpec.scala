import javax.inject.Inject

import ad.challenge.model.SubscriptionManagementModel.SubscriptionOrder
import org.scalatestplus.play._
import play.api.libs.json.Json
import services.{EventMarshallingService, Logging, OAuthSecurityService}

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

      import ad.challenge.model.SubscriptionManagementModelReads._
      val orderO = marshalling.unmarshal[SubscriptionOrder](Json.parse(event))
      orderO mustBe Some(SubscriptionOrder(_,_,_,_))
      val order = orderO.get
      order.meta.marketplace mustBe Some("ACME")
    }

  }


}
