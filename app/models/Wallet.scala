package models

import cryptocurrency.{Blockchain, TransactionOps}
import org.bitcoinj.core.{Base58, ECKey}

object Wallet {

  def create(): Wallet = {
    val key = new ECKey()
    val privateKey = Base58.encode(key.getPrivKeyBytes)
    val publicKey = Base58.encode(key.getPubKey)
    this(Some(privateKey), publicKey)
  }

  def create(address: String): Wallet = this(None, address)
}

case class Wallet(signingKey: Option[String], address: String) {

  def getUnspentTransactions(blockchain: Blockchain) =
    new TransactionOps(blockchain).getTransactions(address)
}
