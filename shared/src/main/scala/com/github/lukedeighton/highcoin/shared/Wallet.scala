package com.github.lukedeighton.highcoin.shared

import TransactionImplicits._

object Wallet {

  def create()(implicit context: ScalaJsContext): Wallet = {
    val (privateKeyBytes, publicKeyBytes) = context.genECKey
    val privateKey = context.base58Encode(privateKeyBytes)
    val publicKey = context.base58Encode(publicKeyBytes)
    this(Some(privateKey), publicKey)
  }

  def create(address: String): Wallet = this(None, address)
}

case class Wallet(signingKey: Option[String], address: String) {

  def signature: String = s"$address Signature" //TODO - use private key and public key

  def balance(implicit blockchain: Blockchain, context: ScalaJsContext): BigDecimal =
    blockchain
      .getUnconnectedOutputs(address)
      .price

  def send(recipientAddress: String, value: BigDecimal)
          (implicit blockchain: Blockchain, context: ScalaJsContext): Transaction = {
    val unspentOutputs = getSpendableTransactionOutputs(value)

    val inputs = getTransactionInputs(unspentOutputs)
    val outputs = getTransactionOutputs(value, unspentOutputs.price, recipientAddress)

    assertEqualPrice(inputs, outputs)

    Transaction(inputs, outputs)
  }

  private def getSpendableTransactionOutputs(value: BigDecimal)
                                            (implicit blockchain: Blockchain, context: ScalaJsContext): Seq[TransactionOutputPair] = {
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

  private def getTransactionInputs(unspentOutputPairs: Seq[TransactionOutputPair])
                                  (implicit context: ScalaJsContext): Seq[Transaction.Input] =
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

  private def assertEqualPrice(inputs: Seq[Transaction.Input], outputs: Seq[Transaction.Output])
                              (implicit blockchain: Blockchain, context: ScalaJsContext): Unit =
    if (inputs.price != outputs.price)
      throw new IllegalStateException("Spent inputs doesn't equal spendable outputs")
}
