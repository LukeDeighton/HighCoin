package models

import java.util.Date

import models.Transaction.CoinCreation

object Block {

  def apply(index: Int,
            nonce: Int,
            transactions: Seq[Transaction],
            previousHash: Option[String],
            timestamp: Date = new Date()): Block = {

    this(index, nonce, sort(transactions), previousHash, timestamp)
  }

  private def sort(transactions: Seq[Transaction]) = transactions.sortBy(_.serialise)
}

case class Block(index: Int,
                 nonce: Int,
                 transactions: Seq[Transaction],
                 previousHash: Option[String],
                 timestamp: Date = new Date()) {

  def withTransaction(transaction: Transaction): Block = copy(transactions = Block.sort(transactions :+ transaction))

  def withNonce(nonce: Int): Block = copy(nonce = nonce)

  def serialise: String = index.toString + nonce.toString +
    transactions.size + transactions.foldLeft("")(_ + _.serialise) + previousHash.getOrElse("")

  def hash: Hash256 = Sha256.digest(this.toString)
}
