package ad.challenge.controllers

import javax.inject._

import ad.challenge.model._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class ADChallengeSampleWebAppController @Inject()(users: Users,  companies: Companies,  accounts: Accounts,  orders: Orders) extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action { implicit request =>

    val result = for {
      openId <- request.session.get("openId")
      user <- users.findByOpenId(openId)
      companyId <- user.companyId
      company <- companies.findById(companyId)
      account <- accounts.findById(company.id)
    } yield Ok(views.html.index(s"AD Challenge Sample Web App. User: $user, Company: $company, Account: $account"))

    result.getOrElse(Unauthorized)
  }

}
