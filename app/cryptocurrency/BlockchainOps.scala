package cryptocurrency

import models.Transaction

trait BlockchainOps { self: Blockchain =>

  def getTransactionOutputs(address: String): Seq[Transaction.Output] =
    allTransactionOutputs.filter(_.address == address)

  def getTransactions(address: String): Seq[Transaction] =
    allTransactions.filter(_.outputs.exists(_.address == address))

  def findOutput(outputRef: Transaction.OutputRef): Option[Transaction.Output] =
    allTransactions
      .find(_.hash().hex == outputRef.transactionHash)
      .map { transaction =>
        transaction.outputs(outputRef.outputIndex)
      }
}
