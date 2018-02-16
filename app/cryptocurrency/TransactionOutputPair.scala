package cryptocurrency

case class TransactionOutputPair(transaction: Transaction, output: Transaction.Output) {

  def makeOutputRef(): Transaction.OutputRef = {
    val outputIndex = transaction.outputs.indexOf(output)
    Transaction.OutputRef(transaction.hash.hex, outputIndex)
  }
}
