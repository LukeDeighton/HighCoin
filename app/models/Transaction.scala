package models

import models.Transaction.{Input, Output}

case class Transaction(inputs: Seq[Input],
                       outputs: Seq[Output]) {

  def serialise: String = {
    val inputHashStr = inputs.size + inputs.foldLeft("")(_ + _.serialise)
    val outputHashStr = outputs.size + outputs.foldLeft("")(_ + _.serialise)
    inputHashStr + outputHashStr
  }

  def hash(): Hash256 = Sha256.digest(serialise)
}

object Transaction {

  def createCoin(value: BigDecimal, address: String): Transaction =
    Transaction(
      inputs = Seq.empty,
      outputs = Seq(Output(value, address)))

  case class Input(signature: String,
                   outputRef: OutputRef) {

    def serialise: String = signature + outputRef.serialise
  }

  case class OutputRef(transactionHash: String,
                       outputIndex: Int) {

    def serialise: String = transactionHash + outputIndex
  }

  case class Output(value: BigDecimal,
                    address: String) {

    def serialise: String = value + address
  }
}
