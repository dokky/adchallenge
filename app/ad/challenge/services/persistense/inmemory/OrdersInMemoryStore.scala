package ad.challenge.services.persistense.inmemory

import ad.challenge.model.MarketplaceModel.Order
import ad.challenge.model.Orders

class OrdersInMemoryStore extends Orders {
  val orders = new java.util.concurrent.ConcurrentLinkedQueue[Order]

  override def save(order: Order): Order = {
    orders.add(order)
    order
  }
}
