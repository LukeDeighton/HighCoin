package models

import cryptocurrency.{Blockchain, TransactionOps}
import models.{Transaction => Tx}
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

  def getSpendableTransactions(implicit blockchain: Blockchain): Seq[Transaction] =
    TransactionOps.getTransactions(address)

  def getTransactionOutputs(implicit blockchain: Blockchain): Seq[Tx.Output] =
    TransactionOps.getTransactionOutputs(address)

  def getBalance(implicit blockchain: Blockchain): BigDecimal =
    getTransactionOutputs.map(_.value).sum
}
