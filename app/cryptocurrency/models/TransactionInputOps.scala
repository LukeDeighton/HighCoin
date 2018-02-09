package cryptocurrency.models

trait TransactionInputOps { self: Transaction.Input =>

  def transactionOutputPair(implicit blockchain: Blockchain): Option[TransactionOutputPair] =
    blockchain.allTransactions.find(_.hash.hex == outputRef.hash).map { transaction =>
      TransactionOutputPair(transaction, transaction.outputs(outputRef.outputIndex))
    }

  def transactionOutput(implicit blockchain: Blockchain): Option[Transaction.Output] =
    transactionOutputPair.map(_.output)
}
