package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.{ByteArray, ScalaJsContext}
import org.apache.commons.codec.digest.DigestUtils
import org.bitcoinj.core.{Base58, ECKey, Utils}

object ScalaContext {

  case class ScalaByteArray(bytes: Array[Byte]) extends ByteArray

  implicit val scalaContext: ScalaJsContext = new ScalaContext

  implicit def byteArrayToBytes(byteArray: ByteArray): Array[Byte] = byteArray match {
    case ScalaByteArray(bytes) => bytes
    case _ => throw new IllegalStateException("Must target Scala Platform")
  }

  implicit def bytesToByteArray(bytes: Array[Byte]): ByteArray = ScalaByteArray(bytes)
}

class ScalaContext extends ScalaJsContext {

  import ScalaContext.byteArrayToBytes
  import ScalaContext.bytesToByteArray

  def hexEncode(bytes: ByteArray): String = Utils.HEX.encode(bytes)

  def hexDecode(hexStr: String): ByteArray = Utils.HEX.decode(hexStr)

  def base58Encode(bytes: ByteArray): String = Base58.encode(bytes)

  def base58Decode(base58Str: String): ByteArray = Base58.decode(base58Str)

  def sha256(input: String): ByteArray = DigestUtils.sha256(input)

  def genECKey: (ByteArray, ByteArray) = {
    val key = new ECKey()
    (key.getPrivKeyBytes, key.getPubKey)
  }
}
