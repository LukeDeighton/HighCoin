package cryptocurrency.models

trait TransactionInputOps { self: Transaction.Input =>

  def transactionOutputPair(implicit blockchain: Blockchain): Option[(Transaction, Transaction.Output)] =
    blockchain.allTransactions.find(_.hash.hex == outputRef.hash).map { transaction =>
      transaction -> transaction.outputs(outputRef.outputIndex)
    }

}
