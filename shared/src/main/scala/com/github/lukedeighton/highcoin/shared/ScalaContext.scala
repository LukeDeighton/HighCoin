package com.github.lukedeighton.highcoin.shared

import org.apache.commons.codec.digest.DigestUtils
import org.bitcoinj.core.{Base58, ECKey, Utils}

object ScalaContext {

  implicit val scalaContext: ScalaJsContext = new ScalaContext
}

class ScalaContext extends ScalaJsContext {

  def hexEncode(bytes: Array[Byte]): String = Utils.HEX.encode(bytes)

  def hexDecode(hexStr: String): Array[Byte] = Utils.HEX.decode(hexStr)

  def base58Encode(bytes: Array[Byte]): String = Base58.encode(bytes)

  def base58Decode(base58Str: String): Array[Byte] = Base58.decode(base58Str)

  def sha256(input: String): Array[Byte] = DigestUtils.sha256(input)

  def genECKey: (Array[Byte], Array[Byte]) = {
    val key = new ECKey()
    (key.getPrivKeyBytes, key.getPubKey)
  }
}
