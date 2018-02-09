package cryptocurrency.models

case class TransactionOutputPair(transaction: Transaction, output: Transaction.Output) {

  def transactionOutputRef: Transaction.OutputRef = {
    val outputIndex = transaction.outputs.indexOf(output)
    Transaction.OutputRef(transaction.hash.hex, outputIndex)
  }
}
