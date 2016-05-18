package ad.challenge.services.persistense.inmemory

import ad.challenge.model.Companies
import ad.challenge.model.MarketplaceModel.Company

class CompaniesInMemoryStore extends Companies {

  override def findById(id: String): Option[Company] = None

  override def findByUuId(uuid: String): Seq[Company] = Seq()

  override def delete(company: Company) = {}

  override def save(company: Company): Company = company
}
