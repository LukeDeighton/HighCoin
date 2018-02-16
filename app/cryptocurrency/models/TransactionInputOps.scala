package cryptocurrency.models

trait TransactionInputOps { input: Transaction.Input =>

  def findAllConnectedTxOutputs(implicit blockchain: Blockchain): Seq[TransactionOutputPair] =
    blockchain.findAllTransactions(input.outputRef.hash).map { transaction =>
      TransactionOutputPair(transaction, transaction.outputs(input.outputRef.outputIndex))
    }

  def findConnectedTxOutput(implicit blockchain: Blockchain): Option[TransactionOutputPair] =
    findAllConnectedTxOutputs.headOption

  def findConnectedOutput(implicit blockchain: Blockchain): Option[Transaction.Output] =
    findConnectedTxOutput.map(_.output)

  def getConnectedTxOutput(implicit blockchain: Blockchain): TransactionOutputPair =
    findConnectedTxOutput.getOrElse(throw new IllegalStateException("Cannot find connected transaction output"))
}
