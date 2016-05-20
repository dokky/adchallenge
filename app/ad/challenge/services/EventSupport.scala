package ad.challenge.services

import ad.challenge.model.MarketplaceEventModel.{MarketplaceEvent, ResponseEvent}
import ad.challenge.model.{ErrorCode, EventFlag}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


trait EventSupport extends Logging {

  def StatelessEventSupport(event: MarketplaceEvent)(block: => Future[Option[ResponseEvent]]): Future[Option[ResponseEvent]] = {
    logger.info(s"inspecting incoming event $event")
    event.meta.flag match {
      case Some(EventFlag.STATELESS) =>
        logger.info(s"stateless event $event")
        Future.successful(Some(ResponseEvent()))
      case _ => block.recover(defaultEventProcessingExceptionHandler(event))
    }
  }

  def defaultEventProcessingExceptionHandler(event: MarketplaceEvent): PartialFunction[Throwable, Option[ResponseEvent]] = {
    case e: Exception =>
      logger.error(s"Failed to process event: $event", e)
      Some(ResponseEvent(ErrorCode.UNKNOWN_ERROR, Some(s"Failed to process event: $e")))
  }

}
