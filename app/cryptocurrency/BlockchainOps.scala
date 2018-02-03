package cryptocurrency

import models.Transaction
import models.Transaction.OutputRef

trait BlockchainOps { self: Blockchain =>

  def getTransactionOutputs(address: String): Seq[Transaction.Output] =
    allTransactionOutputs.filter(_.address == address)

  def getTransactions(address: String): Seq[Transaction] =
    allTransactions.filter(_.outputs.exists(_.address == address))

  def getUnspentTransactionOutputPairs(address: String): Seq[(Transaction, Transaction.Output)] =
    getTransactions(address).flatMap { transaction =>
      transaction
        .getOutputs(address)
        .filterNot(hasTransactionOutputRef)
        .map(transaction -> _)
    }

  def getUnspentTransactionOutputs(address: String): Seq[Transaction.Output] =
    getUnspentTransactionOutputPairs(address).map(_._2)

  def hasTransactionOutputRef(output: Transaction.Output): Boolean =
    allTransactionInputs.exists { input =>
      findTransactionOutput(input.outputRef).isDefined
    }

  def findTransaction(outputRef: OutputRef): Option[Transaction] =
    allTransactions.find(_.hash.hex == outputRef.hash)

  def findTransactionOutput(outputRef: OutputRef): Option[Transaction.Output] =
    findTransaction(outputRef).flatMap { transaction =>
      transaction.outputs.lift(outputRef.outputIndex)
    }
}
