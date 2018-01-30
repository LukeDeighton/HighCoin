package cryptocurrency

import models.{Transaction => Tx}

object TransactionOps {

  def getTransactionOutputs(address: String)(implicit blockchain: Blockchain): Seq[Tx.Output] =
    blockchain.allTxOutputs.filter(_.address == address)

  def findOutput(outputRef: Tx.OutputRef)(implicit blockchain: Blockchain): Option[Tx.Output] =
    blockchain.allTransactions
      .find(_.hash().toHex == outputRef.transactionHash)
      .map { transaction =>
        transaction.outputs(outputRef.outputIndex)
      }


}
