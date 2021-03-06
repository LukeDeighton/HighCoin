package com.github.lukedeighton.highcoin.shared

object TransactionImplicits {

  implicit class TransactionInputs(inputs: Seq[Transaction.Input]) {

    def price(implicit blockchain: Blockchain, context: ScalaJsContext): BigDecimal = {
      inputs.flatMap(_.findConnectedOutput).price
    }
  }

  implicit class TransactionOutputs(outputs: Seq[Transaction.Output]) {

    def price: BigDecimal = {
      outputs.map(_.value).sum
    }
  }

  implicit class TransactionOutputPairs(outputPairs: Seq[TransactionOutputPair]) {

    def price: BigDecimal = {
      outputPairs.map(_.output).price
    }
  }
}
