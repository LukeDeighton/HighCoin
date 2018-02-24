package com.github.lukedeighton.highcoin.shared

trait TransactionInputOps { input: Transaction.Input =>

  def findAllConnectedTxOutputs(implicit blockchain: Blockchain, context: ScalaJsContext): Seq[TransactionOutputPair] =
    blockchain.findAllTransactions(input.outputRef.hash).map { transaction =>
      TransactionOutputPair(transaction, transaction.outputs(input.outputRef.outputIndex))
    }

  def findConnectedTxOutput(implicit blockchain: Blockchain, context: ScalaJsContext): Option[TransactionOutputPair] =
    findAllConnectedTxOutputs.headOption

  def findConnectedOutput(implicit blockchain: Blockchain, context: ScalaJsContext): Option[Transaction.Output] =
    findConnectedTxOutput.map(_.output)

  def getConnectedTxOutput(implicit blockchain: Blockchain, context: ScalaJsContext): TransactionOutputPair =
    findConnectedTxOutput.getOrElse(throw new IllegalStateException("Cannot find connected transaction output"))
}
