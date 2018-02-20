package com.github.lukedeighton.highcoin.shared

import org.apache.commons.codec.digest.DigestUtils
import org.bitcoinj.core.{Base58, Utils}

object Sha256 {

  def digest(value: String): Hash256 = {
    new Hash256(DigestUtils.sha256(value))
  }
}

object Hash256 {

  def fromHex(value: String): Hash256 = new Hash256(Utils.HEX.decode(value))

  def fromBase58(value: String): Hash256 = new Hash256(Base58.decode(value))
}

class Hash256(val bytes: Array[Byte]) extends AnyVal {

  def hex: String = Utils.HEX.encode(bytes)

  def base58: String = Base58.encode(bytes)
}
