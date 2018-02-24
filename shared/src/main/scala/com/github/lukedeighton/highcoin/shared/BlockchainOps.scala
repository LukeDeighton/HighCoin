package com.github.lukedeighton.highcoin.shared

trait BlockchainOps { self: Blockchain =>

  implicit val blockchain: Blockchain = self

  def getOutputs(address: String): Seq[Transaction.Output] =
    allTransactionOutputs.filter(_.address == address)

  def getTransactions(address: String): Seq[Transaction] =
    allTransactions.filter(_.outputs.exists(_.address == address))

  def getTransactionOutputs(address: String): Seq[TransactionOutputPair] =
    getTransactions(address).flatMap { transaction =>
      transaction
        .getOutputs(address)
        .map(TransactionOutputPair(transaction, _))
    }

  def getUnconnectedTransactionOutputs(address: String)
                                      (implicit context: ScalaJsContext): Seq[TransactionOutputPair] =
    getTransactionOutputs(address).filter { output =>
      val connectedInputs = findAllConnectedTxInputs(output)
      if (connectedInputs.size > 1)
        throw new IllegalStateException("Invalid output - cannot be connected to multiple inputs") //TODO move into validation - double spend
      connectedInputs.isEmpty
    }

  def getUnconnectedOutputs(address: String)
                           (implicit context: ScalaJsContext): Seq[Transaction.Output] =
    getUnconnectedTransactionOutputs(address).map(_.output)

  def findAllConnectedTxInputs(output: TransactionOutputPair)
                              (implicit context: ScalaJsContext): Seq[Transaction.Input] =
    allTransactionInputs.filter { input =>
      input.findAllConnectedTxOutputs.contains(output)
    }

  def findAllTransactions(txHashHex: String)
                         (implicit context: ScalaJsContext): Seq[Transaction] =
    allTransactions.filter(_.hash.hex == txHashHex)

}
