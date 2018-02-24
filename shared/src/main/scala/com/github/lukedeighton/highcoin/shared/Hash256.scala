package com.github.lukedeighton.highcoin.shared

object Sha256 {

  def digest(value: String)(implicit context: ScalaJsContext): Hash256 =
    new Hash256(context.sha256(value))
}

object Hash256 {

  def fromHex(value: String)(implicit context: ScalaJsContext): Hash256 =
    new Hash256(context.hexDecode(value))

  def fromBase58(value: String)(implicit context: ScalaJsContext): Hash256 =
    new Hash256(context.base58Decode(value))
}

class Hash256(val bytes: Array[Byte])(implicit context: ScalaJsContext) {

  def hex: String = context.hexEncode(bytes)

  def base58: String = context.base58Encode(bytes)
}
