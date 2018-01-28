package models

import cryptocurrency.Blockchain
import models.{Transaction => Tx}
import org.bitcoinj.core.{Base58, ECKey}

object Wallet {

  def create(): Wallet = {
    val key = new ECKey()
    val privateKey = Base58.encode(key.getPrivKeyBytes)
    val publicKey = Base58.encode(key.getPubKey)
    this(Some(privateKey), publicKey)
  }
}

case class Wallet(signingKey: Option[String], address: String) {

  def getTransactions(blockchain: Blockchain): Seq[Transaction] = {
    blockchain.chain
      .flatMap(_.transactions)
      .filter {
        case Tx.CoinCreation(_, addr) => addr == address
        case Tx.CoinTransfer(_, outputs) => outputs.exists(_.address == this.address)
      }
  }
}
