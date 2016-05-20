package ad.challenge.services.persistense.inmemory

import ad.challenge.model.Companies
import ad.challenge.model.MarketplaceModel.{Account, Company}
import scala.collection.JavaConverters._
import scala.language.postfixOps

class CompaniesInMemoryStore extends Companies {

  val companies = new java.util.concurrent.ConcurrentHashMap[String, Company] asScala

  override def findById(id: String): Option[Company] = companies.get(id)

  override def delete(company: Company): Option[Company] = companies.remove(company.id)

  override def save(company: Company): Company = {
    companies.put(company.id, company)
    company
  }

  override def findByAccount(account: Account): Option[Company] = findById(account.id)

}
