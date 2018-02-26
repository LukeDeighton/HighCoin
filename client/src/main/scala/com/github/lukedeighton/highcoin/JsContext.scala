package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.{ByteArray, ScalaJsContext}
import org.scalajs.dom

import scala.scalajs.js.typedarray.Uint8Array

object JsContext {

  case class JSByteArray(bytes: Uint8Array) extends ByteArray

  implicit val jsContext: ScalaJsContext = new JsContext
}

class JsContext extends ScalaJsContext {

  def hexEncode(bytes: ByteArray): String = ???

  def hexDecode(hexStr: String): ByteArray = ???

  def base58Encode(bytes: ByteArray): String = ???

  def base58Decode(base58Str: String): ByteArray = ???

  def sha256(input: String): ByteArray = {
    val digest = Sha256.digest(input)
    dom.console.log(digest)
    dom.console.log(digest.buffer)
    ???
  }

  def genECKey: (ByteArray, ByteArray) = ???
}
