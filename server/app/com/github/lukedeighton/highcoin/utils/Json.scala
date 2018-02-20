package com.github.lukedeighton.highcoin.utils

import org.json4s._
import org.json4s.jackson.JsonMethods._
import play.api.mvc.AnyContent

object Json {

  implicit private val formats = org.json4s.DefaultFormats

  def asJson(model: Any): JValue = Extraction.decompose(model)

  def as[T : Manifest](anyContent: AnyContent): T = as[T](anyContent.asJson.get.toString())

  def as[T : Manifest](jsonStr: String): T = parse(jsonStr).extract[T]
}
