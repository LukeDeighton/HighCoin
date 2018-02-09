package cryptocurrency.models

trait BlockchainOps { self: Blockchain =>

  def getTransactionOutputs(address: String): Seq[Transaction.Output] =
    allTransactionOutputs.filter(_.address == address)

  def getTransactions(address: String): Seq[Transaction] =
    allTransactions.filter(_.outputs.exists(_.address == address))

  def getTransactionOutputPairs(address: String): Seq[TransactionOutputPair] =
    getTransactions(address).flatMap { transaction =>
      transaction
        .getOutputs(address)
        .map(TransactionOutputPair(transaction, _))
    }

  def getUnspentTransactionOutputPairs(address: String): Seq[TransactionOutputPair] = {
    var unspentPairs = getTransactionOutputPairs(address)
    allTransactionInputs.foreach { input =>
      input.transactionOutputPair(self).foreach { pair =>
        unspentPairs = unspentPairs.filter(_ == pair) //only works if no two tx's have same hash
      }
    }
    unspentPairs
  }

  def getUnspentTransactionOutputs(address: String): Seq[Transaction.Output] =
    getUnspentTransactionOutputPairs(address).map(_.output)
}
