import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {

    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }

  "HomeController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("You are user number 1")
    }

  }

  "Subscription Management Controller" should {

    "ping-pong" in {
      contentAsString(route(app, FakeRequest(GET, "/ping")).get) mustBe "pong"
    }


    "Correct order processing lead to create new account" in {
//      val (_, _, order) = getNewOrderResult()
//      (order \ "success") (0).text mustBe "true"
//      (order \ "accountIdentifier") (0).text mustNot be(empty)
//      (order \ "message") (0).text mustNot be(empty)
    }

  }

}
