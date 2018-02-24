package com.github.lukedeighton.highcoin.shared

case class TransactionOutputPair(transaction: Transaction, output: Transaction.Output) {

  def makeOutputRef()(implicit context: ScalaJsContext): Transaction.OutputRef = {
    val outputIndex = transaction.outputs.indexOf(output)
    Transaction.OutputRef(transaction.hash.hex, outputIndex)
  }
}
