package cryptocurrency

import models.{Transaction => Tx}
import models.Transaction

//TODO change to transaction + blockchain as args?
class TransactionOps(blockchain: Blockchain) {

  def getTransactions(address: String): Seq[Transaction] =
    blockchain.chain
      .flatMap(_.transactions)
      .filter(_.outputs.exists(_.address == address))
//
//  private def getTransactionOutputs(): Seq[Tx.OutputRef] =
//    for {
//      block <- blockchain.chain
//      transaction <- block.transactions
//      output <- transaction.outputs
//    } yield output
//
//  private def getTransactionOutputRefs(transaction: Transaction): Seq[Tx.OutputRef] = transaction match {
//    case Tx.CoinTransfer(inputs, outputs) => inputs.map(_.outputRef)
//    case _                                => Seq.empty
//  }

//  private def isUnspentTransaction(transaction: Tx.CoinTransfer): Boolean = {
//    ???
//  }
}
