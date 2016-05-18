package ad.challenge.services.persistense.inmemory

import ad.challenge.model.MarketplaceModel.Order
import ad.challenge.model.Orders

class OrdersInMemoryStore extends Orders {
  override def save(order: Order): Order = order
}
