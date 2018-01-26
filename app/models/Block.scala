package models

import java.util.Date

case class Block(index: Int,
                 nonce: Int,
                 transactions: Seq[Transaction],
                 previousHash: Option[String],
                 timestamp: Date = new Date()) {

  def withTransaction(transaction: Transaction): Block = copy(transactions = transactions :+ transaction)

  def withNonce(nonce: Int): Block = copy(nonce = nonce)

  def serialise: String = index.toString + nonce.toString +
    transactions.size + transactions.foldLeft("")(_ + _.serialise) + previousHash.getOrElse("")

  def hash: Hash256 = Sha256.digest(this.toString)
}
