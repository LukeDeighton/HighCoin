package cryptocurrency

import cryptocurrency.TransactionImplicits._
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

  def signature: String = s"$address Signature" //TODO - use private key and public key

  def balance(implicit blockchain: Blockchain): BigDecimal =
    blockchain
      .getUnconnectedOutputs(address)
      .price

  def send(value: BigDecimal, recipientAddress: String)(implicit blockchain: Blockchain): Transaction = {
    val unspentOutputs = getSpendableTransactionOutputs(value)

    val inputs = getTransactionInputs(unspentOutputs)
    val outputs = getTransactionOutputs(value, unspentOutputs.price, recipientAddress)

    assertEqualPrice(inputs, outputs)

    Transaction(inputs, outputs)
  }

  private def getSpendableTransactionOutputs(value: BigDecimal)(implicit blockchain: Blockchain): Seq[TransactionOutputPair] = {
    val unspentOutputs = blockchain.getUnconnectedTransactionOutputs(address)
    if (unspentOutputs.price < value)
      throw new IllegalStateException("Insufficient funds to spend transaction outputs")

    var spentValue: BigDecimal = 0
    unspentOutputs.takeWhile { outputPair =>
      val take = spentValue < value
      spentValue = spentValue + outputPair.output.value
      take
    }
  }

  private def getTransactionInputs(unspentOutputPairs: Seq[TransactionOutputPair]): Seq[Transaction.Input] =
    unspentOutputPairs.map { outputPair =>
      val outputRef = outputPair.makeOutputRef()
      Transaction.Input(signature, outputRef)
    }

  private def getTransactionOutputs(spendValue: BigDecimal, spendableValue: BigDecimal, recipientAddress: String): Seq[Transaction.Output] = {
    if (spendableValue < spendValue)
      throw new IllegalStateException("Insufficient funds to spend transaction outputs")

    val selfTransactionOutputOpt =
      if (spendValue != spendableValue) {
        val unspentValue = spendableValue - spendValue
        Some(Transaction.Output(unspentValue, address))
      } else {
        None
      }

    Seq(Transaction.Output(spendValue, recipientAddress)) ++ selfTransactionOutputOpt
  }

  private def assertEqualPrice(inputs: Seq[Transaction.Input], outputs: Seq[Transaction.Output])(implicit blockchain: Blockchain): Unit =
    if (inputs.price != outputs.price)
      throw new IllegalStateException("Spent inputs doesn't equal spendable outputs")
}
