package controllers

import akka.io.Udp.Message
import play.api.http.ContentTypes
import play.api.mvc.Result
import play.api.mvc.Results._

/**
  * Created by stanislavd on 14/05/2016.
  */
trait ControllerHelper {

  def ok(message: String): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>true</success>
          |  <message>$message</message>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)

  def error(e: ad.challenge.model.SubscriptionManagementModel.Error): Result =
    error(e.code, e.message)

  def error(code: String, message: String): Result =
    Ok(
      s"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<result>
          |  <success>false</success>
          |  <errorCode>$code</errorCode>
          |  <message>$message</message>
          |</result>""".stripMargin
    ).as(ContentTypes.XML)


}
