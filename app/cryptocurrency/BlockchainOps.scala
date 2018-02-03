package cryptocurrency

import models.Transaction
import models.Transaction.OutputRef

trait BlockchainOps { self: Blockchain =>

  def getTransactionOutputs(address: String): Seq[Transaction.Output] =
    allTransactionOutputs.filter(_.address == address)

  def getTransactions(address: String): Seq[Transaction] =
    allTransactions.filter(_.outputs.exists(_.address == address))

  def findOutput(outputRef: Transaction.OutputRef): Option[Transaction.Output] =
    allTransactions
      .find(_.hash.hex == outputRef.hash)
      .map { transaction =>
        transaction.outputs(outputRef.outputIndex)
      }

  def getUnspentTransactionOutputs(address: String): Seq[Transaction.Output] =
    getTransactionOutputs(address).filterNot(hasTransactionOutputRef)

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
