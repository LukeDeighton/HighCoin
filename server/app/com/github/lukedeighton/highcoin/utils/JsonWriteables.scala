package com.github.lukedeighton.highcoin.utils

import akka.util.ByteString
import org.json4s._
import org.json4s.jackson.JsonMethods._
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.mvc._

object JsonWriteables {

  implicit val jValueWriteable: Writeable[JValue] =
    Writeable(asByteString)(ContentTypeOf(Some(ContentTypes.JSON)))

  private def asByteString(json: JValue): ByteString = Codec.utf_8.encode(renderToString(json))

  private def renderToString(json: JValue) = pretty(json)
}
