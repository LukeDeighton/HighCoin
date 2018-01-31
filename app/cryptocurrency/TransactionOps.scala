package cryptocurrency

import models.{Transaction, Transaction => Tx}

object TransactionOps {

  def getTransactionOutputs(address: String)(implicit blockchain: Blockchain): Seq[Tx.Output] =
    blockchain.allTxOutputs.filter(_.address == address)

  def getTransactions(address: String)(implicit blockchain: Blockchain): Seq[Transaction] =
    blockchain.allTransactions.filter(_.outputs.exists(_.address == address))

  def findOutput(outputRef: Tx.OutputRef)(implicit blockchain: Blockchain): Option[Tx.Output] =
    blockchain.allTransactions
      .find(_.hash().toHex == outputRef.transactionHash)
      .map { transaction =>
        transaction.outputs(outputRef.outputIndex)
      }


}
