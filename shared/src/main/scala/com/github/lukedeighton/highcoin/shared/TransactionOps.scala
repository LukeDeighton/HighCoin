package com.github.lukedeighton.highcoin.shared

trait TransactionOps { transaction: Transaction =>

  def validate(implicit blockchain: Blockchain): Unit = {
    transaction.inputs.foreach { input =>
      val connectedOutput = input.findAllConnectedTxOutputs
      if (connectedOutput.isEmpty)
        throw new IllegalStateException("Invalid input - cannot find connected output")
      else if (connectedOutput.size > 1)
        throw new IllegalStateException("Invalid input - input connected to multiple outputs")
    }

    //assuming the validate function is called before the transaction has made it's way into the blockchain
    val sameHashTransactions = blockchain.findAllTransactions(transaction.hash.hex)
    if (sameHashTransactions.nonEmpty)
      throw new IllegalStateException("Invalid transaction - transaction already has same hash")
  }
}
