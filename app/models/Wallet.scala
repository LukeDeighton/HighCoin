package models

import org.bitcoinj.core.{Base58, ECKey}

object Wallet {

  def apply(): Wallet = {
    val key = new ECKey()
    val privateKey = Base58.encode(key.getPrivKeyBytes)
    val publicKey = Base58.encode(key.getPubKey)
    this(privateKey, publicKey)
  }
}

case class Wallet(signingKey: String, address: String)
