package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.{ByteArray, ScalaJsContext}

import scala.scalajs.js.typedarray.Uint8Array

object JsContext {

  case class JsByteArray(bytes: Uint8Array) extends ByteArray

  implicit val jsContext: ScalaJsContext = new JsContext

  implicit def bytesToByteArray(bytes: Uint8Array): JsByteArray = JsByteArray(bytes)

  implicit def byteArrayToBytes(byteArray: ByteArray): Uint8Array = byteArray match {
    case JsByteArray(bytes) => bytes
    case _ => throw new IllegalStateException("Must target Javascript Platform")
  }
}

class JsContext extends ScalaJsContext {

  import JsContext.bytesToByteArray
  import JsContext.byteArrayToBytes

  def hexEncode(bytes: ByteArray): String = Hex.encode(bytes)

  def hexDecode(hex: String): ByteArray = Hex.decode(hex)

  def base58Encode(bytes: ByteArray): String = ???

  def base58Decode(base58Str: String): ByteArray = ???

  def sha256(input: String): ByteArray = Sha256.digest(input)

  def genECKey: (ByteArray, ByteArray) = ???
}
