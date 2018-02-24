package com.github.lukedeighton.highcoin.shared

trait ScalaJsContext {

  def hexEncode(bytes: Array[Byte]): String

  def hexDecode(hexStr: String): Array[Byte]

  def base58Encode(bytes: Array[Byte]): String

  def base58Decode(base58Str: String): Array[Byte]

  def sha256(input: String): Array[Byte]

  def genECKey: (Array[Byte], Array[Byte])
}
