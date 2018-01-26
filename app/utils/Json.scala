package utils

import org.json4s._
import org.json4s.jackson.JsonMethods._

object Json {

  implicit val formats = org.json4s.DefaultFormats

  def asJson(model: Any): JValue = Extraction.decompose(model)

  def as[T : Manifest](jsonStr: String): T = {
    parse(jsonStr).extract[T]
  }
}
