package com.github.lukedeighton.highcoin.shared

trait ScalaJsContext {

  def hexEncode(bytes: ByteArray): String

  def hexDecode(hexStr: String): ByteArray

  def base58Encode(bytes: ByteArray): String

  def base58Decode(base58Str: String): ByteArray

  def sha256(input: String):ByteArray

  def genECKey: (ByteArray, ByteArray)
}
